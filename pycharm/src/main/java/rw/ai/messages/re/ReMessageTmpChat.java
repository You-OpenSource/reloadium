package rw.ai.messages.re;

import rw.ai.messages.MessageType;
import rw.ai.context.Context;
import javax.swing.Box;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.Container;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import com.intellij.openapi.vfs.VirtualFileWrapper;
import com.intellij.openapi.fileChooser.FileSaverDialog;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.application.ApplicationManager;
import java.io.IOException;
import rw.audit.RwSentry;
import rw.ai.chat.ChatFileType;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.fileChooser.FileSaverDescriptor;
import com.intellij.ide.IdeBundle;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import rw.ai.lang.AiBundle;
import rw.ai.messages.MessageView;
import rw.ai.messages.MessageModel;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

public class ReMessageTmpChat extends ReMessage
{
    VirtualFile file;
    
    public ReMessageTmpChat(@NotNull final Project project, @NotNull final VirtualFile file, @NotNull final Model model, @NotNull final View view) {
        super(project, model, view);
        if (project == null) {
            $$$reportNull$$$0(0);
        }
        if (file == null) {
            $$$reportNull$$$0(1);
        }
        if (model == null) {
            $$$reportNull$$$0(2);
        }
        if (view == null) {
            $$$reportNull$$$0(3);
        }
        this.file = file;
        this.setRawContent(AiBundle.message("ai.remessage.tmp.chat", new Object[0]));
        this.getView().addButton();

        this.getView().addAiPreferencesActionListner(e -> {
            FileSaverDialog saver = FileChooserFactory.getInstance().createSaveFileDialog(new FileSaverDescriptor(IdeBundle.message("dialog.title.save.as", new Object[0]), IdeBundle.message("label.choose.target.file", new Object[0]), new String[0]), project);
            VirtualFileWrapper fileWrapper = saver.save(ProjectUtil.guessProjectDir(project),  project.getName().toLowerCase()+ ChatFileType.EXTENSION);
            if (fileWrapper != null) {
                VirtualFile newFile = fileWrapper.getVirtualFile(true);
                if (newFile != null) {
                    byte[] content;
                    try {
                        content = file.contentsToByteArray();
                    }
                    catch (IOException ex) {
                        RwSentry.get().captureException(ex, false);
                        content = new byte[0];
                    }
                    byte[] finalContent1 = content;
                    ApplicationManager.getApplication().runWriteAction(() -> {
                        try {
                            newFile.setBinaryContent(finalContent1);
                        }
                        catch (IOException ex2) {
                            RwSentry.get().captureException(ex2, false);
                        }
                        FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
                        fileEditorManager.openFile(newFile, true);
                        fileEditorManager.closeFile(file);
                    });
                }
            }
        });
    }
    
    @Override
    public View getView() {
        return (View)super.getView();
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
                args[0] = "file";
                break;
            }
            case 2: {
                args[0] = "model";
                break;
            }
            case 3: {
                args[0] = "view";
                break;
            }
        }
        args[1] = "rw/ai/messages/re/ReMessageTmpChat";
        args[2] = "<init>";
        throw new IllegalArgumentException(String.format(format, args));
    }
    
    public static class View extends ReMessage.View
    {
        JButton aiPreferences;
        
        public View(@NotNull final Project project) {
            super(project);
            if (project == null) {
                $$$reportNull$$$0(0);
            }
            (this.aiPreferences = new JButton("Save as")).setOpaque(false);
        }
        
        void addAiPreferencesActionListner(final ActionListener actionListener) {
            this.aiPreferences.addActionListener(actionListener);
        }
        
        public void addButton() {
            final JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, 0));
            panel.add(this.aiPreferences);
            panel.add(Box.createHorizontalGlue());
            panel.setOpaque(false);
            this.msgContainer.add(panel);
        }
        
        private static /* synthetic */ void $$$reportNull$$$0(final int n) {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "project", "rw/ai/messages/re/ReMessageTmpChat$View", "<init>"));
        }
    }
    
    public static class Model extends ReMessage.Model
    {
        public Model(@NotNull final Context context) {
            super(context);
            if (context == null) {
                $$$reportNull$$$0(0);
            }
        }
        
        public Model() {
        }
        
        @Override
        public MessageType getType() {
            return MessageType.RE_TMP_CHAT;
        }
        
        private static /* synthetic */ void $$$reportNull$$$0(final int n) {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "context", "rw/ai/messages/re/ReMessageTmpChat$Model", "<init>"));
        }
    }
}
