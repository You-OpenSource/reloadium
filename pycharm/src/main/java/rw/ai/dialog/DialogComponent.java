// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.dialog;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.Point;
import javax.swing.JViewport;
import javax.swing.Box;
import java.awt.LayoutManager;
import java.awt.Container;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.JScrollBar;
import com.intellij.openapi.application.ApplicationManager;
import rw.ai.messages.MessageView;
import java.awt.Component;
import rw.ai.context.ContextSeparator;
import org.jetbrains.annotations.NotNull;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.openapi.project.Project;
import rw.ai.chat.prompt.Prompt;
import javax.swing.JPanel;

public class DialogComponent extends JPanel
{
    Prompt.Listener promptListener;
    Project project;
    private Prompt prompt;
    private JBScrollPane outputPane;
    private OutputContent outputContent;
    private JScrollBar verticalScrollBar;

    public DialogComponent(@NotNull final Project project, final Prompt.Listener promptListener) {
        if (project == null) {
            $$$reportNull$$$0(0);
        }
        this.promptListener = promptListener;
        this.project = project;
        this.createUIComponents();
    }
    
    public void addContextSeparator(@NotNull final ContextSeparator separator) {
        if (separator == null) {
            $$$reportNull$$$0(1);
        }
        this.outputContent.add(separator.getComponent(), this.outputContent.getComponents().length);
    }
    
    public void addMessage(@NotNull final MessageView messageView) {
        if (messageView == null) {
            $$$reportNull$$$0(2);
        }
        this.outputContent.add(messageView, this.outputContent.getComponents().length);
        this.scrollToBottom();
        this.outputContent.revalidate();
        this.outputContent.repaint();
    }
    
    private void scrollToBottom() {
        ApplicationManager.getApplication().invokeLater(() -> {
            verticalScrollBar = this.outputPane.getVerticalScrollBar();
            verticalScrollBar.setValue(verticalScrollBar.getMaximum());
        });
    }
    
    public Prompt getPrompt() {
        return this.prompt;
    }
    
    private void createUIComponents() {
        this.setBorder(BorderFactory.createEmptyBorder());
        this.prompt = new Prompt(this.project, this.promptListener);
        (this.outputContent = new OutputContent()).setLayout(new BoxLayout(this.outputContent, 1));
        this.outputContent.add(Box.createVerticalGlue());
        this.outputContent.setBorder(BorderFactory.createEmptyBorder());
        (this.outputPane = new JBScrollPane((Component)this.outputContent)).setBorder(BorderFactory.createEmptyBorder());
        this.outputPane.setVerticalScrollBarPolicy(20);
        this.outputPane.setDoubleBuffered(true);

        final ChangeListener viewportChangeListener = e -> {
            JScrollBar verticalScrollbar = this.outputPane.getVerticalScrollBar();
            JViewport viewport;
            int currentHeight;
            int currentPosition;
            int maxPosition;
            int extent;
            viewport = (JViewport)e.getSource();
            currentHeight = viewport.getViewSize().height;
            try {
                currentPosition = verticalScrollBar.getValue();
                maxPosition = verticalScrollBar.getMaximum();
                extent = verticalScrollBar.getModel().getExtent();

            if (currentHeight != maxPosition && currentPosition + extent >= maxPosition) {
                viewport.setViewPosition(new Point(0, currentHeight - viewport.getExtentSize().height));
            }
            }catch (Exception ee){
                System.out.println(ee.toString());
            }
            return;
        };
        this.outputPane.getViewport().addChangeListener(viewportChangeListener);
        this.setLayout(new GridBagLayout());
        this.add((Component)this.outputPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        this.add(this.prompt, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, 15, 1, new Insets(0, 0, 0, 0), 0, 0));
        this.outputContent.repaint();
        this.revalidate();
        this.repaint();
    }
    
    public void clear() {
        this.outputContent.removeAll();
        this.outputContent.add(Box.createVerticalGlue());
    }
    
    public OutputContent getOutputContent() {
        return this.outputContent;
    }
    
    public void onLoad() {
        this.revalidate();
        this.scrollToBottom();
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
                args[0] = "separator";
                break;
            }
            case 2: {
                args[0] = "messageView";
                break;
            }
        }
        args[1] = "rw/ai/dialog/DialogComponent";
        switch (n) {
            default: {
                args[2] = "<init>";
                break;
            }
            case 1: {
                args[2] = "addContextSeparator";
                break;
            }
            case 2: {
                args[2] = "addMessage";
                break;
            }
        }
        throw new IllegalArgumentException(String.format(format, args));
    }
}
