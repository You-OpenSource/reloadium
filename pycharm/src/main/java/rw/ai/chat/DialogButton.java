package rw.ai.chat;

import java.awt.event.ActionEvent;
import java.awt.Graphics;
import rw.ai.ui.UiUtils;
import java.awt.Color;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import com.intellij.icons.AllIcons;
import rw.ai.ui.AiIcons;
import com.intellij.ui.ColorUtil;
import org.jetbrains.annotations.NotNull;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class DialogButton extends SidePanelButton
{
    static final Integer ACTIVE_OFFSET;
    private Integer id;
    private boolean active;
    private JPopupMenu popupMenu;
    private Listener listener;
    private JMenuItem rename;
    private JMenuItem delete;
    
    DialogButton(@NotNull final String name, final Integer id, final Listener listener) {
        if (name == null) {
            $$$reportNull$$$0(0);
        }
        this.listener = listener;
        this.id = id;
        this.setText(name);
        if (ColorUtil.isDark(super.getColor())) {
            this.setIcon(AiIcons.Dialog);
        }
        else {
            this.setIcon(AiIcons.DialogDark);
        }
        this.active = false;
        this.popupMenu = new JPopupMenu();
        final RenameDialog[] renameDialog = new RenameDialog[1];
        (this.rename = new JMenuItem("Rename", AllIcons.Actions.Edit)).addActionListener(e -> {
            renameDialog[0] = new RenameDialog(this.getText());
            renameDialog[0].show();
            if (renameDialog[0].getExitCode() == 0) {
                this.doRename(renameDialog[0].getNewName());
            }
            return;
        });
        (this.delete = new JMenuItem("Delete", AllIcons.Vcs.Remove)).addActionListener(e -> this.doDelete());
        this.popupMenu.add(this.rename);
        this.popupMenu.add(this.delete);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                if (e.isPopupTrigger()) {
                    DialogButton.this.popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
                else if (e.getButton() == 2) {
                    DialogButton.this.doDelete();
                }
            }
        });
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setActive(final boolean active) {
        this.active = active;
        this.repaint();
    }
    
    @Override
    public Color getColor() {
        if (!this.active) {
            return super.getColor();
        }
        final Integer offset = DialogButton.ACTIVE_OFFSET;
        return UiUtils.addBrightnessThemeAware(super.getColor(), offset);
    }
    
    public JPopupMenu getPopupMenu() {
        return this.popupMenu;
    }
    
    public void doRename(@NotNull final String name) {
        if (name == null) {
            $$$reportNull$$$0(1);
        }
        this.listener.onRenamed(name);
        this.setText(name);
    }
    
    public void doDelete() {
        this.listener.onDeleted();
    }
    
    static {
        ACTIVE_OFFSET = 30;
    }
    
    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        final String format = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        final Object[] args = { "name", "rw/ai/chat/DialogButton", null };
        switch (n) {
            default: {
                args[2] = "<init>";
                break;
            }
            case 1: {
                args[2] = "doRename";
                break;
            }
        }
        throw new IllegalArgumentException(String.format(format, args));
    }
    
    interface Listener
    {
        void onRenamed(final String p0);
        
        void onDeleted();
    }
}
