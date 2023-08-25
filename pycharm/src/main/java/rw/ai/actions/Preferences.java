// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.actions;

import rw.ai.preferences.AiPreferencesConfigurable;
import com.intellij.openapi.options.ShowSettingsUtil;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.actionSystem.AnAction;

public class Preferences extends AnAction implements DumbAware
{
    public static String ID;
    
    public void update(@NotNull final AnActionEvent e) {
        if (e == null) {
            $$$reportNull$$$0(0);
        }
        e.getPresentation().setEnabled(true);
        super.update(e);
    }
    
    public void actionPerformed(@NotNull final AnActionEvent e) {
        if (e == null) {
            $$$reportNull$$$0(1);
        }
        ShowSettingsUtil.getInstance().showSettingsDialog(e.getProject(), (Class)AiPreferencesConfigurable.class);
    }
    
    static {
        Preferences.ID = "AiPreferences";
    }
    
    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        final String format = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        final Object[] args = { "e", "rw/ai/actions/Preferences", null };
        switch (n) {
            default: {
                args[2] = "update";
                break;
            }
            case 1: {
                args[2] = "actionPerformed";
                break;
            }
        }
        throw new IllegalArgumentException(String.format(format, args));
    }
}
