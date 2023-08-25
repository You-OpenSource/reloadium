// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.messages.user;

import rw.ai.ui.UiUtils;
import rw.ai.ui.AiIcons;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.project.Project;
import rw.ai.messages.MessageView;

public class UserMessageView extends MessageView
{
    public UserMessageView(@NotNull final Project project) {
        super(project);
        if (project == null) {
            $$$reportNull$$$0(0);
        }
    }
    
    @Override
    protected Icon getIcon() {
        return AiIcons.UserBig;
    }
    
    @Override
    protected int getColorOffset() {
        if (UiUtils.isHighContrast()) {
            return 5;
        }
        return -5;
    }
    
    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "project", "rw/ai/messages/user/UserMessageView", "<init>"));
    }
}
