// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.messages;

import javax.swing.text.html.StyleSheet;
import com.vladsch.flexmark.util.ast.Node;
import javax.swing.text.BadLocationException;
import rw.audit.RwSentry;
import javax.swing.text.html.HTMLDocument;
import com.vladsch.flexmark.util.data.DataHolder;
import com.vladsch.flexmark.html.HtmlRenderer;
import java.util.List;
import com.vladsch.flexmark.ext.emoji.EmojiExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.footnotes.FootnoteExtension;
import com.vladsch.flexmark.ext.definition.DefinitionExtension;
import com.vladsch.flexmark.ext.abbreviation.AbbreviationExtension;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSetter;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.util.data.MutableDataSet;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import java.awt.LayoutManager;
import java.awt.Container;
import javax.swing.BoxLayout;
import java.net.URISyntaxException;
import java.io.IOException;
import java.awt.Component;
import javax.swing.JOptionPane;
import java.awt.Desktop;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import rw.ai.ui.UiUtils;
import java.awt.Color;
import javax.swing.JEditorPane;
import org.jetbrains.annotations.NotNull;

public class Markdown extends MessagePart
{
    protected View view;
    protected Model model;
    
    public Markdown() {
        this.view = new View();
        this.model = new Model();
    }
    
    public Markdown(@NotNull final Model model) {
        if (model == null) {
            $$$reportNull$$$0(0);
        }
        this.model = model;
        this.view = new View();
        this.setContent(model.getContent());
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
            $$$reportNull$$$0(1);
        }
        this.getModel().setContent(content);
        this.getView().setContent(content);
    }
    
    @Override
    public void onFinished() {
    }
    
    public void dispose() {
    }
    
    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        final String format = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        final Object[] args = new Object[3];
        switch (n) {
            default: {
                args[0] = "model";
                break;
            }
            case 1: {
                args[0] = "content";
                break;
            }
        }
        args[1] = "rw/ai/messages/Markdown";
        switch (n) {
            default: {
                args[2] = "<init>";
                break;
            }
            case 1: {
                args[2] = "setContent";
                break;
            }
        }
        throw new IllegalArgumentException(String.format(format, args));
    }
    
    public static class View extends MessagePart.View
    {
        private final JEditorPane content;
        Color codeBackgroundColor;
        private Color textColor;
        
        public View() {
            this.textColor = UiUtils.getDefaultEditorForeground();
            this.content = new JEditorPane();
            this.setOpaque(false);
            this.content.setContentType("text/html");
            this.content.setEditable(false);
            this.content.setOpaque(false);
            this.content.addHyperlinkListener(new HyperlinkListener() {
                @Override
                public void hyperlinkUpdate(final HyperlinkEvent e) {
                    if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                        if (Desktop.isDesktopSupported()) {
                            try {
                                Desktop.getDesktop().browse(e.getURL().toURI());
                            }
                            catch (IOException | URISyntaxException ex3) {
                                final Exception ex2;
                                final Exception ex = ex2;
                                JOptionPane.showMessageDialog((Component)null, ex.getMessage(), "Error", 0);
                            }
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Desktop is not supported.", "Error", 0);
                        }
                    }
                }
            });
            this.setLayout(new BoxLayout(this, 2));
            this.add(this.content);
            final EditorColorsManager editorColorsManager = EditorColorsManager.getInstance();
            final EditorColorsScheme editorColorsScheme = editorColorsManager.getGlobalScheme();
            this.codeBackgroundColor = editorColorsScheme.getDefaultBackground();
        }
        
        public void setTextColor(final Color color) {
            this.textColor = color;
        }
        
        public JEditorPane getContent() {
            return this.content;
        }
        
        @Override
        public void setContent(@NotNull final String content) {
            if (content == null) {
                $$$reportNull$$$0(0);
            }
            final MutableDataSet options = new MutableDataSet();
            options.setFrom((MutableDataSetter)ParserEmulationProfile.GITHUB_DOC);
            options.set(Parser.EXTENSIONS, (Object)List.of(AbbreviationExtension.create(), DefinitionExtension.create(), FootnoteExtension.create(), TablesExtension.create(), EmojiExtension.create()));
            options.set(HtmlRenderer.SOFT_BREAK, (Object)"<br />\n");
            final Parser parser = Parser.builder((DataHolder)options).build();
            final HtmlRenderer renderer = HtmlRenderer.builder((DataHolder)options).build();
            final Node document = (Node)parser.parse(content);
            final String textColorStr = String.format("rgb(%d, %d, %d)", this.textColor.getRed(), this.textColor.getGreen(), this.textColor.getBlue());
            final String codeBackgroundStr = String.format("rgb(%d, %d, %d)", this.codeBackgroundColor.getRed(), this.codeBackgroundColor.getGreen(), this.codeBackgroundColor.getBlue());
            final String html =   String.format("<div style=\"color:%s\">", textColorStr) + renderer.render(document);
            final HTMLDocument doc = (HTMLDocument)this.content.getDocument();
            final StyleSheet styleSheet = doc.getStyleSheet();
            styleSheet.addRule("p { margin-top: 2px; margin-bottom: 2px; }");
            styleSheet.addRule(String.format("code {\n      background-color: %s;\n      padding-left: 5px;\n      padding-right: 5px;\n    }\n", codeBackgroundStr));
            try {
                doc.setInnerHTML(doc.getDefaultRootElement(), html);
                this.repaint();
            }
            catch (BadLocationException | IOException ex2) {
                final Exception ex;
                final Exception e = ex;
                RwSentry.get().captureException(e, true);
            }
        }
        
        private static /* synthetic */ void $$$reportNull$$$0(final int n) {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "content", "rw/ai/messages/Markdown$View", "setContent"));
        }
    }
    
    public static class Model extends MessagePart.Model
    {
        @Override
        public MessagePartType getType() {
            return MessagePartType.MARKDOWN;
        }
    }
}
