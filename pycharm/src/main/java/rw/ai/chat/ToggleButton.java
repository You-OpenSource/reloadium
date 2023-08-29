package rw.ai.chat;

import java.awt.event.ActionEvent;
import com.intellij.icons.AllIcons;
import org.jetbrains.annotations.NotNull;
import javax.swing.JButton;

class ToggleButton extends JButton
{
    ToggleListener toggleListener;
    private boolean open;
    
    ToggleButton(@NotNull final ToggleListener toggleListener) {
        if (toggleListener == null) {
            $$$reportNull$$$0(0);
        }
        this.toggleListener = toggleListener;
        this.open = true;
        this.setIcon(AllIcons.Diff.ApplyNotConflictsRight);
        this.addActionListener(e -> this.setOpen(!this.open));
        this.setFocusPainted(false);
        this.setFocusable(false);
    }
    
    public boolean isOpen() {
        return this.open;
    }
    
    public void setOpen(final boolean open) {
        this.open = open;
        if (this.open) {
            this.setIcon(AllIcons.Diff.ApplyNotConflictsRight);
        }
        else {
            this.setIcon(AllIcons.Diff.ApplyNotConflictsLeft);
        }
        this.toggleListener.onChangeOpen(open);
    }
    
    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "toggleListener", "rw/ai/chat/ToggleButton", "<init>"));
    }
    
    interface ToggleListener
    {
        void onChangeOpen(final boolean p0);
    }
}
