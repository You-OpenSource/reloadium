package rw.ai.chat.prompt;

import java.util.Iterator;
import javax.swing.Action;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;
import rw.ai.lang.AiBundle;
import javax.swing.Icon;
import rw.ai.context.Context;
import java.util.List;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.GridBagLayout;
import java.awt.Dimension;
import java.awt.event.MouseListener;
import rw.ai.context.ContextManager;
import javax.swing.SwingUtilities;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import rw.ai.ui.UiUtils;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.JPanel;

public class ContextButton extends JPanel
{
    private final Color hoverColor;
    private final Color pushColor;
    JLabel content;
    private PopupMenu popupMenu;
    
    public ContextButton() {
        this.content = new JLabel();
        this.popupMenu = null;
        this.hoverColor = UiUtils.addBrightnessThemeAware(this.getBackground(), 20);
        this.pushColor = UiUtils.addBrightnessThemeAware(this.getBackground(), 40);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                ContextButton.this.setBackground(ContextButton.this.pushColor);
                if (SwingUtilities.isLeftMouseButton(e)) {
                    ContextManager.get().setPreviousContextToCurrent();
                }
                else if (SwingUtilities.isRightMouseButton(e)) {
                    if (ContextButton.this.popupMenu == null) {
                        return;
                    }
                    final int popupMenuHeight = ContextButton.this.popupMenu.getPreferredSize().height;
                    final int newY = e.getY() - popupMenuHeight;
                    ContextButton.this.popupMenu.show(e.getComponent(), e.getX(), newY);
                }
                else if (SwingUtilities.isMiddleMouseButton(e)) {
                    ContextManager.get().setCurrentContextToNone();
                }
            }
            
            @Override
            public void mouseReleased(final MouseEvent e) {
                ContextButton.this.setBackground(ContextButton.this.hoverColor);
            }
            
            @Override
            public void mouseExited(final MouseEvent e) {
                ContextButton.this.setBackground(null);
                ContextButton.this.repaint();
            }
            
            @Override
            public void mouseEntered(final MouseEvent e) {
                ContextButton.this.setBackground(ContextButton.this.hoverColor);
                ContextButton.this.repaint();
            }
        });
        this.setFocusable(false);
        this.setPreferredSize(new Dimension(40, 60));
        this.setLayout(new GridBagLayout());
        this.add(this.content);
    }
    
    public void onContextChange(final List<Context> allContexts) {
        final Icon icon = ContextManager.get().getCurrentContext().getIcon();
        this.content.setIcon(icon);
        this.popupMenu = new PopupMenu(allContexts);
    }
    
    @Override
    public String getToolTipText() {
        return AiBundle.message("ai.context.button.tooltip", new Object[0]);
    }
    
    public PopupMenu getPopupMenu() {
        return this.popupMenu;
    }
    
    public static class PopupMenu extends JPopupMenu
    {
        public PopupMenu(final List<Context> contexts) {
            for (final Context c : contexts) {
                final JMenuItem menu = new JMenuItem(new AbstractAction(c.getSeparatorText(), c.getIconSmall(this)) {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        ContextManager.get().setCurrentContext(c);
                    }
                });
                this.add(menu);
            }
        }
    }
}
