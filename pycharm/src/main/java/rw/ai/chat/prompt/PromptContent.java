// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.chat.prompt;

import javax.swing.text.BadLocationException;
import javax.swing.text.AttributeSet;
import javax.swing.text.AbstractDocument;
import javax.swing.text.PlainDocument;
import java.awt.Graphics;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.Clipboard;
import kotlin.text.StringsKt;
import java.io.IOException;
import java.awt.datatransfer.UnsupportedFlavorException;
import rw.audit.RwSentry;
import java.awt.datatransfer.DataFlavor;
import java.awt.Toolkit;
import java.awt.event.MouseListener;
import javax.swing.SwingUtilities;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.BorderFactory;
import javax.swing.text.Document;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.project.Project;
import java.awt.Color;
import javax.swing.JTextArea;

public class PromptContent extends JTextArea
{
    public static int MARGIN;
    private final Color hintColor;
    Prompt owner;
    Project project;
    private String hint;
    
    public PromptContent(@NotNull final Project project, final Prompt owner) {
        if (project == null) {
            $$$reportNull$$$0(0);
        }
        this.project = project;
        this.owner = owner;
        this.hint = "";
        this.hintColor = new Color(this.getForeground().getRed(), this.getForeground().getGreen(), this.getForeground().getBlue(), 100);
        this.setDocument(new CustomDocument(this));
        this.setLineWrap(true);
        this.setWrapStyleWord(true);
        this.setBorder(BorderFactory.createEmptyBorder(PromptContent.MARGIN, PromptContent.MARGIN, PromptContent.MARGIN, PromptContent.MARGIN));
        this.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(final CaretEvent e) {
                owner.getParent().revalidate();
                owner.getParent().repaint();
            }
        });
        final CustomContextMenu contextMenu = new CustomContextMenu(this);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    contextMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }
    
    public void pasteCode() {
        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        final Transferable contents = clipboard.getContents(null);
        if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            String modifiedText = "";
            String clipboardText = null;
            try {
                clipboardText = (String)contents.getTransferData(DataFlavor.stringFlavor);
            }
            catch (UnsupportedFlavorException | IOException ex2) {
                final Exception ex;
                final Exception e = ex;
                RwSentry.get().captureException(e, true);
                return;
            }
            modifiedText = StringsKt.trimIndent(clipboardText).strip();
            this.replaceSelection(  modifiedText);
            this.revalidate();
            this.repaint();
        }
    }
    
    public void setHint(@NotNull final String hint) {
        if (hint == null) {
            $$$reportNull$$$0(1);
        }
        this.hint = hint;
        this.repaint();
    }
    
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if (this.getText().isEmpty() && !this.isFocusOwner()) {
            g.setColor(this.hintColor);
            final int x = this.getInsets().left;
            final int y = this.getInsets().top;
            g.drawString(this.hint, x, y + g.getFontMetrics().getAscent());
        }
    }
    
    static {
        PromptContent.MARGIN = 12;
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
                args[0] = "hint";
                break;
            }
        }
        args[1] = "rw/ai/chat/prompt/PromptContent";
        switch (n) {
            default: {
                args[2] = "<init>";
                break;
            }
            case 1: {
                args[2] = "setHint";
                break;
            }
        }
        throw new IllegalArgumentException(String.format(format, args));
    }
    
    private class CustomDocument extends PlainDocument
    {
        PromptContent owner;
        
        public CustomDocument(final PromptContent owner) {
            this.owner = owner;
        }
        
        @Override
        protected void insertUpdate(final DefaultDocumentEvent chng, final AttributeSet attr) {
            super.insertUpdate(chng, attr);
            this.owner.owner.repaint();
        }
        
        @Override
        protected void removeUpdate(final DefaultDocumentEvent chng) {
            super.removeUpdate(chng);
            this.owner.owner.repaint();
        }
        
        @Override
        public void insertString(final int offs, final String str, final AttributeSet a) throws BadLocationException {
            super.insertString(offs, str, a);
        }
    }
}
