package rw.ai.chat;

import rw.ai.dialog.Dialog;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import java.awt.LayoutManager;
import java.awt.Container;
import javax.swing.BoxLayout;
import org.jetbrains.annotations.NotNull;
import javax.swing.JPanel;

public class ChatComponent extends JPanel
{
    SidePanel sidePanel;
    JPanel mainPanel;
    
    ChatComponent(@NotNull final SidePanel.Listener sidePanelListener) {
        if (sidePanelListener == null) {
            $$$reportNull$$$0(0);
        }
        this.sidePanel = new SidePanel(sidePanelListener);
        (this.mainPanel = new JPanel()).setLayout(new BoxLayout(this.mainPanel, 2));
        this.mainPanel.setBorder(BorderFactory.createEmptyBorder());
        this.setLayout(new GridBagLayout());
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        gbc.weighty = 1.0;
        gbc.fill = 3;
        gbc.anchor = 17;
        this.add(this.sidePanel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = 1;
        gbc.anchor = 13;
        this.add(this.mainPanel, gbc);
    }
    
    public void onNewDialog(@NotNull final Dialog dialog) {
        if (dialog == null) {
            $$$reportNull$$$0(1);
        }
        this.sidePanel.onNewDialog(dialog);
    }
    
    public void onDialogDeleted(@NotNull final Dialog dialog) {
        if (dialog == null) {
            $$$reportNull$$$0(2);
        }
        this.sidePanel.onDialogDeleted(dialog);
    }
    
    public void onActiveDialogChanged(@NotNull final Dialog dialog) {
        if (dialog == null) {
            $$$reportNull$$$0(3);
        }
        this.mainPanel.removeAll();
        this.mainPanel.add(dialog.getView());
        this.sidePanel.onActiveDialogChanged(dialog);
        this.mainPanel.revalidate();
        this.mainPanel.repaint();
    }
    
    public SidePanel getSidePanel() {
        return this.sidePanel;
    }
    
    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        final String format = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        final Object[] args = new Object[3];
        switch (n) {
            default: {
                args[0] = "sidePanelListener";
                break;
            }
            case 1:
            case 2:
            case 3: {
                args[0] = "dialog";
                break;
            }
        }
        args[1] = "rw/ai/chat/ChatComponent";
        switch (n) {
            default: {
                args[2] = "<init>";
                break;
            }
            case 1: {
                args[2] = "onNewDialog";
                break;
            }
            case 2: {
                args[2] = "onDialogDeleted";
                break;
            }
            case 3: {
                args[2] = "onActiveDialogChanged";
                break;
            }
        }
        throw new IllegalArgumentException(String.format(format, args));
    }
}
