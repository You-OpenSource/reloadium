// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.chat.prompt;

import rw.ai.ui.UiUtils;
import java.awt.Color;
import javax.swing.JComponent;
import java.awt.Dimension;
import rw.ai.messages.Message;
import rw.ai.context.Context;
import java.awt.Insets;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import rw.ai.context.ContextManager;
import javax.swing.BorderFactory;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBScrollPane;
import javax.swing.JPanel;

public class Prompt extends JPanel
{
    private static final String HIGH_CONTRAST_THEME_ID = "JetBrainsHighContrastTheme";
    private static final int MAX_HEIGHT = 200;
    private static final int TOP_BOTTOM_MARGIN = 5;
    private final PromptContent content;
    private final SubmitButton submitButton;
    private final ContextButton contextButton;
    private final Listener promptListener;
    private final JPanel wrapper;
    private JBScrollPane contentScroll;
    
    public Prompt(@NotNull final Project project, @NotNull final Listener promptListener) {
        if (project == null) {
            $$$reportNull$$$0(0);
        }
        if (promptListener == null) {
            $$$reportNull$$$0(1);
        }
        this.setBorder(BorderFactory.createEmptyBorder());
        this.promptListener = promptListener;
        this.wrapper = new JPanel();
        this.content = new PromptContent(project, this);
        this.contextButton = new ContextButton();
        this.onContextChange(ContextManager.get().getCurrentContext());
        this.content.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(final KeyEvent e) {
            }
            
            @Override
            public void keyPressed(final KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    if (!e.isControlDown() && !e.isShiftDown()) {
                        e.consume();
                        Prompt.this.submit(Prompt.this.content.getText());
                    }
                    else {
                        Prompt.this.content.replaceSelection("\n");
                        Prompt.this.content.revalidate();
                        Prompt.this.content.repaint();
                    }
                }
            }
            
            @Override
            public void keyReleased(final KeyEvent e) {
            }
        });
        this.submitButton = new SubmitButton(new SubmitButton.SubmitListener() {
            @Override
            public void onSubmit() {
                Prompt.this.submit(Prompt.this.content.getText());
            }
            
            @Override
            public void onCancel() {
                promptListener.onCancel();
            }
        });
        (this.contentScroll = new JBScrollPane((Component)this.content)).setVerticalScrollBarPolicy(20);
        this.contentScroll.setHorizontalScrollBarPolicy(31);
        this.contentScroll.setBorder(BorderFactory.createEmptyBorder());
        ContextManager.get().addContextListener(this::onContextChange);
        this.wrapper.setLayout(new BorderLayout());
        this.wrapper.add(this.contextButton, "West");
        this.wrapper.add((Component)this.contentScroll, "Center");
        this.wrapper.add(this.submitButton, "East");
        this.setLayout(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = 1;
        gbc.insets = new Insets(5, 0, 5, 0);
        this.add(this.wrapper, gbc);
    }
    
    private void onContextChange(final Context context) {
        this.contextButton.onContextChange(ContextManager.get().getAllContexts());
        this.content.setHint(context.getPromptHint());
    }
    
    public void submit(@NotNull String prompt) {
        if (prompt == null) {
            $$$reportNull$$$0(2);
        }
        if (this.submitButton.getState() == SubmitButton.State.STOP) {
            return;
        }
        prompt = prompt.strip();
        if (prompt.isEmpty()) {
            return;
        }
        this.submitButton.onSubmit();
        this.promptListener.onSubmit(prompt);
        this.content.setText("");
        this.repaint();
    }
    
    public void onMessageCompleted(@NotNull final Message message) {
        if (message == null) {
            $$$reportNull$$$0(3);
        }
        this.submitButton.setState(SubmitButton.State.SUBMIT);
    }
    
    @Override
    public Dimension getMinimumSize() {
        int height = this.content.getUI().getPreferredSize(this.content).height + 10;
        if (height > 200) {
            height = 200;
        }
        return new Dimension(super.getMinimumSize().width, height);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return this.getMinimumSize();
    }
    
    @Override
    public Color getBackground() {
        if (super.getBackground() == null) {
            return null;
        }
        if (UiUtils.isHighContrast()) {
            return UiUtils.addBrightness(super.getBackground(), 20);
        }
        return super.getBackground();
    }
    
    public ContextButton getContextButton() {
        return this.contextButton;
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
                args[0] = "promptListener";
                break;
            }
            case 2: {
                args[0] = "prompt";
                break;
            }
            case 3: {
                args[0] = "message";
                break;
            }
        }
        args[1] = "rw/ai/chat/prompt/Prompt";
        switch (n) {
            default: {
                args[2] = "<init>";
                break;
            }
            case 2: {
                args[2] = "submit";
                break;
            }
            case 3: {
                args[2] = "onMessageCompleted";
                break;
            }
        }
        throw new IllegalArgumentException(String.format(format, args));
    }
    
    public interface Listener
    {
        void onSubmit(@NotNull final String p0);
        
        void onCancel();
    }
}
