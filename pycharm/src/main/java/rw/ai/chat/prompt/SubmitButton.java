// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.chat.prompt;

import rw.ai.ui.AiIcons;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import rw.ai.ui.UiUtils;
import javax.swing.BorderFactory;
import java.awt.Color;
import javax.swing.JLabel;

public class SubmitButton extends JLabel
{
    private final Color hoverColor;
    private final Color pushColor;
    SubmitListener submitListener;
    private State state;
    
    SubmitButton(final SubmitListener submitListener) {
        this.setState(State.SUBMIT);
        this.submitListener = submitListener;
        this.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));
        this.setOpaque(true);
        this.hoverColor = UiUtils.addBrightnessThemeAware(this.getBackground(), 20);
        this.pushColor = UiUtils.addBrightnessThemeAware(this.getBackground(), 40);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                SubmitButton.this.setBackground(SubmitButton.this.pushColor);
                switch (SubmitButton.this.state) {
                    case SUBMIT: {
                        submitListener.onSubmit();
                        break;
                    }
                    case STOP: {
                        submitListener.onCancel();
                        break;
                    }
                }
            }
            
            @Override
            public void mouseReleased(final MouseEvent e) {
                SubmitButton.this.setBackground(SubmitButton.this.hoverColor);
            }
            
            @Override
            public void mouseExited(final MouseEvent e) {
                SubmitButton.this.setBackground(null);
                SubmitButton.this.repaint();
            }
            
            @Override
            public void mouseEntered(final MouseEvent e) {
                SubmitButton.this.setBackground(SubmitButton.this.hoverColor);
                SubmitButton.this.repaint();
            }
        });
    }
    
    public void onSubmit() {
        this.setState(State.STOP);
    }
    
    public State getState() {
        return this.state;
    }
    
    public void setState(final State state) {
        this.state = state;
        switch (this.state) {
            case SUBMIT: {
                this.setIcon(AiIcons.SubmitDark);
                break;
            }
            case STOP: {
                this.setIcon(AiIcons.StopDark);
                break;
            }
        }
    }
    
    public enum State
    {
        SUBMIT, 
        STOP;
        
        private static /* synthetic */ State[] $values() {
            return new State[] { State.SUBMIT, State.STOP };
        }
        

    }
    
    interface SubmitListener
    {
        void onSubmit();
        
        void onCancel();
    }
}
