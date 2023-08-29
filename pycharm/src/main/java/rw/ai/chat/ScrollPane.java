package rw.ai.chat;

import javax.swing.JScrollBar;
import java.awt.Component;
import com.intellij.ui.components.JBScrollPane;

class ScrollPane extends JBScrollPane
{
    ScrollPane(final Component view) {
        super(view);
        this.setOpaque(false);
        this.getViewport().setOpaque(false);
        this.setHorizontalScrollBarPolicy(31);
        final JScrollBar verticalScrollBar = this.getVerticalScrollBar();
        verticalScrollBar.setOpaque(false);
        verticalScrollBar.setEnabled(false);
    }
}
