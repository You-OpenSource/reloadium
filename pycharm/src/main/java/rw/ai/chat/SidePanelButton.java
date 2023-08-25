// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.chat;

import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import rw.ai.ui.UiUtils;
import java.awt.Insets;
import com.intellij.util.ui.JBUI;
import java.awt.Dimension;
import java.awt.Color;
import java.util.Map;
import javax.swing.JButton;

abstract class SidePanelButton extends JButton
{
    protected static Map<State, Integer> STATE_TO_COLOR_OFFSET;
    private final Color backgroundColor;
    private State state;
    
    SidePanelButton() {
        this.state = State.DEFAULT;
        this.setHorizontalAlignment(2);
        final int margin = 10;
        this.setPreferredSize(new Dimension(SidePanel.WIDTH - margin * 2, 45));
        this.setContentAreaFilled(false);
        this.setMinimumSize(this.getPreferredSize());
        this.setMargin((Insets)JBUI.insets(0, margin));
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, this.getPreferredSize().height));
        this.setOpaque(false);
        this.setFocusPainted(false);
        this.setFocusable(false);
        this.addMouseListener(this.getMouseListener());
        this.backgroundColor = UiUtils.getDefaultEditorBackground();
    }
    
    protected MouseListener getMouseListener() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                SidePanelButton.this.state = State.PRESSED;
                SidePanelButton.this.repaint();
            }
            
            @Override
            public void mouseReleased(final MouseEvent e) {
                SidePanelButton.this.state = State.HOVER;
                SidePanelButton.this.repaint();
            }
            
            @Override
            public void mouseEntered(final MouseEvent e) {
                SidePanelButton.this.state = State.HOVER;
                SidePanelButton.this.repaint();
            }
            
            @Override
            public void mouseExited(final MouseEvent e) {
                SidePanelButton.this.state = State.DEFAULT;
                SidePanelButton.this.repaint();
            }
        };
    }
    
    @Override
    public void paint(final Graphics g) {
        final Graphics2D g2 = (Graphics2D)g.create();
        g2.setColor(this.getColor());
        final int offset = 5;
        g2.fillRect(offset, offset, this.getWidth() - offset * 2, this.getHeight() - offset * 2);
        g2.dispose();
        super.paint(g);
    }
    
    public Color getColor() {
        final int offset = SidePanelButton.STATE_TO_COLOR_OFFSET.get(this.state);
        return UiUtils.addBrightnessThemeAware(this.backgroundColor, offset);
    }
    
    static {
        SidePanelButton.STATE_TO_COLOR_OFFSET = Map.ofEntries(Map.entry(State.DEFAULT, 0), Map.entry(State.HOVER, 20), Map.entry(State.PRESSED, 40));
    }
    
    protected enum State
    {
        HOVER, 
        PRESSED, 
        DEFAULT;
        
        private static /* synthetic */ State[] $values() {
            return new State[] { State.HOVER, State.PRESSED, State.DEFAULT };
        }
        

    }
}
