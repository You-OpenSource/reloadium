// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.chat.prompt;

import com.intellij.icons.AllIcons;
import javax.swing.Action;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;

public class CustomContextMenu extends JPopupMenu
{
    public CustomContextMenu(final PromptContent textArea) {
        final JMenuItem cutMenuItem = new JMenuItem(new AbstractAction("Cut") {
            @Override
            public void actionPerformed(final ActionEvent e) {
                textArea.cut();
            }
        });
        cutMenuItem.setIcon(AllIcons.Actions.MenuCut);
        final JMenuItem copyMenuItem = new JMenuItem(new AbstractAction("Copy") {
            @Override
            public void actionPerformed(final ActionEvent e) {
                textArea.copy();
            }
        });
        copyMenuItem.setIcon(AllIcons.Actions.Copy);
        final JMenuItem pasteMenuItem = new JMenuItem(new AbstractAction("Paste") {
            @Override
            public void actionPerformed(final ActionEvent e) {
                textArea.paste();
            }
        });
        pasteMenuItem.setIcon(AllIcons.Actions.MenuPaste);
        final JMenuItem pasteCodeMenuItem = new JMenuItem(new AbstractAction("Paste Code") {
            @Override
            public void actionPerformed(final ActionEvent e) {
                textArea.pasteCode();
            }
        });
        pasteCodeMenuItem.setIcon(AllIcons.Actions.MenuPaste);
        this.add(cutMenuItem);
        this.add(copyMenuItem);
        this.add(pasteMenuItem);
        this.add(pasteCodeMenuItem);
    }
}
