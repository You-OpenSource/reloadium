// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.actions;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import rw.ai.chat.ChatFileType;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.actionSystem.AnAction;

class NewTmpChatAction extends AnAction implements DumbAware
{
    public void update(@NotNull final AnActionEvent e) {
        if (e == null) {
            $$$reportNull$$$0(0);
        }
        e.getPresentation().setEnabledAndVisible(true);
    }
    
    public void actionPerformed(@NotNull final AnActionEvent e) {
        if (e == null) {
            $$$reportNull$$$0(1);
        }
        final Project project = e.getProject();
        if (project == null) {
            return;
        }
        final LightVirtualFile tempFile = new LightVirtualFile("*tmp.chat", ChatFileType.INSTANCE, (CharSequence)"");
        ChatFileType.open(e.getProject(), (VirtualFile)tempFile);
    }
    
    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        final String format = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        final Object[] args = { "e", "rw/ai/actions/NewTmpChatAction", null };
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
