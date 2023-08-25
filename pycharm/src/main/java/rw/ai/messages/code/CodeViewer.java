// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.messages.code;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorKind;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.util.ui.JBInsets;
import com.intellij.util.ui.UIUtil;
import ee.carlrobert.openai.client.completion.CompletionEventListener;
import ee.carlrobert.openai.client.completion.CompletionRequest;
import ee.carlrobert.openai.client.completion.ErrorDetails;
import ee.carlrobert.openai.client.completion.chat.ChatCompletionClient;
import ee.carlrobert.openai.client.completion.chat.ChatCompletionModel;
import ee.carlrobert.openai.client.completion.chat.request.ChatCompletionMessage;
import ee.carlrobert.openai.client.completion.chat.request.ChatCompletionRequest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rw.ai.context.ContextUtils;
import rw.ai.dialog.Dialog;
import rw.ai.messages.MessagePart;
import rw.ai.messages.MessagePartType;
import rw.ai.openai.ClientFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

public class CodeViewer extends MessagePart implements Disposable {
    private static final Logger LOGGER;
    private static final int PREDICTION_THRESHOLD = 200;
    static Map<String, String> LANG_TO_EXT;
    private final boolean gotExtensionFromLanguage;
    Model model;
    View view;
    private boolean predictedPartial;
    private boolean predicted;

    public CodeViewer(@NotNull final Project project, @Nullable String language) {
        if (project == null) {
            $$$reportNull$$$0(0);
        }
        (this.model = new Model()).setLanguage(language);
        this.view = new View(project);
        if (language == null) {
            language = "unknown";
        }
        String extension = CodeViewer.LANG_TO_EXT.get(language);
        this.gotExtensionFromLanguage = (extension != null);
        if (extension == null) {
            extension = "py";
        }
        this.setExtension(extension);
    }

    public CodeViewer(@NotNull final Project project, @NotNull final Model model) {
        if (project == null) {
            $$$reportNull$$$0(1);
        }
        if (model == null) {
            $$$reportNull$$$0(2);
        }
        this.model = model;
        this.view = new View(project);
        this.gotExtensionFromLanguage = true;
        this.setExtension(model.getExtension());
        this.setContent(model.getContent());
    }

    public void setExtension(final String extension) {
        this.model.setExtension(extension);
        this.view.create(extension);
        this.view.setContent(this.model.getContent());
    }

    @Override
    public View getView() {
        return this.view;
    }

    @Override
    public Model getModel() {
        return this.model;
    }

    @Override
    public void setContent(@NotNull final String content) {
        if (content == null) {
            $$$reportNull$$$0(3);
        }
        this.view.setContent(content);
        this.model.setContent(content);
    }

    public void setContentWithExtensionPrediction(@NotNull final String content) {
        if (content == null) {
            $$$reportNull$$$0(4);
        }
        this.setContent(content);
        if (!this.predictedPartial && content.length() > 200) {
            this.predictedPartial = true;
            this.predictExtension();
        }
    }

    private void predictExtension() {
        if (this.gotExtensionFromLanguage) {
            return;
        }
        CodeViewer.LOGGER.info("Predicting code extension");
        final ChatCompletionClient client = ClientFactory.getChatCompletionClient();
        final String systemMsg = "You'll be predicting file extensions based on the content syntax.\nYou should return your prediction in just one word.\nExamples would be: \"py\", \"java\", \"kt\", \"json, \"js\".\nThere might be some strings in the code with file extensions. Ignore them. \nIgnore errors, just write predicted extension\n";
        String content = this.model.getContent();
        content = content.replaceAll("\".*?\"", "\"\"");
        content = content.replaceAll("'.*?'", "''");
        content = content.replaceAll("#.*\\n", "");
        final String prompt = systemMsg + content;
        final List<ChatCompletionMessage> messages = List.of(new ChatCompletionMessage("user", prompt));
        ChatCompletionRequest.Builder builder = new ChatCompletionRequest.Builder((List) messages);
        builder = builder.setModel(ChatCompletionModel.GPT_3_5);
        final ChatCompletionRequest request = builder.build();
        final StringBuilder ret = new StringBuilder();
        final CompletionEventListener eventListener = (CompletionEventListener) new CompletionEventListener() {
            public void onMessage(final String message) {
                ret.append(message);
            }

            public void onError(final ErrorDetails error) {
//                super.onError(error);
                CodeViewer.LOGGER.warn(String.format("Got error when predicting extension \"%s\"", error.getMessage()));
            }

            public void onComplete(final StringBuilder messageBuilder) {
                String predicted = ret.toString().strip();
                predicted = predicted.replace("\\.", "");
                predicted = predicted.strip();
                CodeViewer.LOGGER.info(String.format("Prediction result: \"%s\"", predicted));
                if (CodeViewer.this.model.getExtension().equals(predicted)) {
                    return;
                }
                final String finalPredicted = predicted;
                UIUtil.invokeLaterIfNeeded(() -> CodeViewer.this.setExtension(finalPredicted));
            }
        };
        client.stream((CompletionRequest) request, eventListener);
    }

    @Override
    public void onFinished() {
        if (!this.predicted) {
            this.predictExtension();
            this.predicted = true;
        }
    }

    public void dispose() {
        this.view.dispose();
    }

    static {
        LOGGER = Logger.getInstance((Class) Dialog.class);
        CodeViewer.LANG_TO_EXT = Map.ofEntries(Map.entry("java", "java"), Map.entry("python", "py"), Map.entry("kotlin", "kt"), Map.entry("javascript", "js"), Map.entry("typescript", "ts"), Map.entry("json", "json"), Map.entry("txt", "txt"), Map.entry("sql", "sql"), Map.entry("xml", "xml"), Map.entry("yaml", "yaml"), Map.entry("html", "html"), Map.entry("css", "css"), Map.entry("yml", "yaml"));
    }

    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        final String format = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        final Object[] args = new Object[3];
        switch (n) {
            default: {
                args[0] = "project";
                break;
            }
            case 2: {
                args[0] = "model";
                break;
            }
            case 3:
            case 4: {
                args[0] = "content";
                break;
            }
        }
        args[1] = "rw/ai/messages/code/CodeViewer";
        switch (n) {
            default: {
                args[2] = "<init>";
                break;
            }
            case 3: {
                args[2] = "setContent";
                break;
            }
            case 4: {
                args[2] = "setContentWithExtensionPrediction";
                break;
            }
        }
        throw new IllegalArgumentException(String.format(format, args));
    }

    public static class Model extends MessagePart.Model {
        String extension;
        String language;

        public Model() {
            this.extension = null;
            this.language = null;
        }

        public String getExtension() {
            return this.extension;
        }

        public void setExtension(@NotNull final String extension) {
            if (extension == null) {
                $$$reportNull$$$0(0);
            }
            this.extension = extension;
        }

        @Override
        public MessagePartType getType() {
            return MessagePartType.CODE;
        }

        public String getLanguage() {
            return this.language;
        }

        public void setLanguage(@Nullable final String language) {
            this.language = language;
        }

        private static /* synthetic */ void $$$reportNull$$$0(final int n) {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "extension", "rw/ai/messages/code/CodeViewer$Model", "setExtension"));
        }
    }

    public static class View extends MessagePart.View implements Disposable {
        static final int EDITOR_WIDTH = 400;
        @NotNull
        Project project;
        private JButton copy;
        private JPanel actions;
        private EditorImpl editor;
        private Document document;
        private LightVirtualFile file;

        public View(@NotNull final Project project) {
            if (project == null) {
                $$$reportNull$$$0(0);
            }
            this.project = project;
        }

        private void create(@NotNull final String extension) {
            if (extension == null) {
                $$$reportNull$$$0(1);
            }
            SwingUtilities.invokeLater(() -> {
                this.dispose();
                this.setLayout(new GridBagLayout());
                this.file = new LightVirtualFile("code." + extension, "py");
                this.document = FileDocumentManager.getInstance().getDocument((VirtualFile) this.file);
                assert this.document != null;
                this.document.addDocumentListener((DocumentListener) new DocumentListener() {
                    public void documentChanged(@NotNull final DocumentEvent event) {
                        if (event == null) {
                            $$$reportNull$$$0(0);
                        }
                        View.this.resizeToContent();
                    }

                    private /* synthetic */ void $$$reportNull$$$0(final int n) {
                        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "event", "rw/ai/messages/code/CodeViewer$View$1", "documentChanged"));
                    }
                });
                this.editor = (EditorImpl) EditorFactory.getInstance().createEditor(this.document, this.project, (VirtualFile) this.file, true, EditorKind.MAIN_EDITOR);
                this.editor.getComponent().setAutoscrolls(false);
                this.editor.setVerticalScrollbarVisible(false);
                this.editor.getScrollPane().getHorizontalScrollBar().setEnabled(false);
                final JViewport viewport = this.editor.getScrollPane().getViewport();
                final Point currentPosition = viewport.getViewPosition();
                this.editor.getScrollPane().getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
                    @Override
                    public void adjustmentValueChanged(final AdjustmentEvent e) {
                        viewport.setViewPosition(currentPosition);
                    }
                });
                (this.actions = new JPanel(new GridBagLayout())).setOpaque(false);
                this.setOpaque(false);
                (this.copy = new ActionButton("Copy", "Copied!", new ActionButton.ClickedListener() {
                    @Override
                    public void clicked(final MouseEvent e) {
                        final CopyPasteManager copyPasteManager = CopyPasteManager.getInstance();
                        final Transferable transferable = new StringSelection(View.this.document.getText());
                        copyPasteManager.setContents(transferable);
                    }
                })).setOpaque(false);
                GridBagConstraints c = new GridBagConstraints();
                c.anchor = 17;
                c.gridx = 0;
                this.actions.add(this.copy, c);
                c = new GridBagConstraints();
                c.anchor = 13;
                c.gridx = 1;
                c.fill = 2;
                c.weightx = 1.0;
                final JPanel filler = new JPanel();
                filler.setOpaque(false);
                this.actions.add(filler, c);
                this.setIgnoreRepaint(true);
                this.removeAll();
                this.add(this.editor.getComponent(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 11, 2, (Insets) new JBInsets(0, 0, 0, 0), 0, 0));
                this.add(this.actions, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, 11, 2, (Insets) new JBInsets(0, 0, 0, 0), 0, 0));
                this.setIgnoreRepaint(false);
                this.revalidate();
                this.repaint();
            });
        }

        @Override
        public void setContent(@NotNull final String content) {
            if (content == null) {
                $$$reportNull$$$0(2);
            }
            final String cleanContent = ContextUtils.stripLineNumbers(content.strip());
            ApplicationManager.getApplication().invokeLater(() -> WriteCommandAction.runWriteCommandAction(this.project, () -> {
                this.document.setText((CharSequence) cleanContent);
                this.resizeToContent();
                this.editor.getComponent().revalidate();
                this.editor.getComponent().repaint();
            }));
        }

        public void resizeToContent() {
            int lineCount = this.document.getLineCount();
            if (lineCount < 1) {
                lineCount = 1;
            }
            final int height = this.editor.getLineHeight() * lineCount;
            this.editor.getComponent().setMinimumSize(new Dimension(400, height));
            this.editor.getComponent().setPreferredSize(new Dimension(400, height));
        }

        public void dispose() {
            if (this.editor == null) {
                return;
            }
            EditorFactory.getInstance().releaseEditor((Editor) this.editor);
        }

        public Document getDocument() {
            return this.document;
        }

        public LightVirtualFile getFile() {
            return this.file;
        }

        private static /* synthetic */ void $$$reportNull$$$0(final int n) {
            final String format = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
            final Object[] args = new Object[3];
            switch (n) {
                default: {
                    args[0] = "project";
                    break;
                }
                case 1: {
                    args[0] = "extension";
                    break;
                }
                case 2: {
                    args[0] = "content";
                    break;
                }
            }
            args[1] = "rw/ai/messages/code/CodeViewer$View";
            switch (n) {
                default: {
                    args[2] = "<init>";
                    break;
                }
                case 1: {
                    args[2] = "create";
                    break;
                }
                case 2: {
                    args[2] = "setContent";
                    break;
                }
            }
            throw new IllegalArgumentException(String.format(format, args));
        }
    }
}
