// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.messages.ai;

import java.awt.event.ActionEvent;
import javax.swing.text.JTextComponent;
import javax.swing.text.BadLocationException;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import javax.swing.text.DefaultCaret;

class BlinkingCaret extends DefaultCaret implements ActionListener
{
    private static final int BLINK_RATE = 500;
    private boolean showing;
    private Timer blinkTimer;
    
    public BlinkingCaret() {
        this.showing = true;
        (this.blinkTimer = new Timer(500, this)).start();
        this.setVisible(true);
    }
    
    @Override
    public void setVisible(final boolean e) {
        super.setVisible(e);
        this.showing = true;
    }
    
    @Override
    protected synchronized void damage(final Rectangle r) {
        if (r != null) {
            this.x = r.x;
            this.y = r.y;
            this.width = r.width;
            this.height = r.height;
            super.damage(r);
            this.repaint();
        }
    }
    
    @Override
    protected void adjustVisibility(final Rectangle nloc) {
    }
    
    @Override
    public void paint(final Graphics g) {
        if (this.showing && this.isVisible()) {
            final JTextComponent comp = this.getComponent();
            if (comp != null) {
                try {
                    if (this.getDot() == 0) {
                        return;
                    }
                    final Rectangle r = comp.modelToView(this.getDot());
                    g.setColor(comp.getCaretColor());
                    g.fillRect(r.x, r.y, 5, r.height);
                }
                catch (BadLocationException ex) {}
            }
        }
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
        this.showing = !this.showing;
        this.repaint();
    }
    
    public void blinkOn() {
        this.showing = true;
    }
}
