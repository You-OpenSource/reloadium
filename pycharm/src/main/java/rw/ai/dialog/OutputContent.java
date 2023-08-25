// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.dialog;

import javax.swing.Icon;
import java.awt.Component;
import com.intellij.util.IconUtil;
import rw.ai.ui.AiIcons;
import java.awt.Graphics;
import javax.swing.JPanel;

public class OutputContent extends JPanel
{
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Icon originalIcon = AiIcons.Welcome;
        final int panelWidth = this.getParent().getWidth();
        final int panelHeight = this.getParent().getHeight();
        final int iconWidth = originalIcon.getIconWidth();
        final int iconHeight = originalIcon.getIconHeight();
        final int margin = panelWidth / 20;
        Icon scaledIcon;
        if (panelWidth < iconWidth + 2 * margin || panelHeight < iconHeight) {
            final float scale = Math.min((panelWidth - 2 * margin) / (float)iconWidth, panelHeight / (float)iconHeight);
            scaledIcon = IconUtil.scale(originalIcon, (Component)this, scale);
        }
        else {
            scaledIcon = originalIcon;
        }
        final int x = margin + (panelWidth - scaledIcon.getIconWidth() - 2 * margin) / 2;
        final int y = (panelHeight - scaledIcon.getIconHeight()) / 2;
        scaledIcon.paintIcon(this, g, x, y);
    }
}
