package rw.ai;

import rw.ai.messages.MessageUtils;
import com.intellij.openapi.application.ApplicationManager;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;

public class Startup implements StartupActivity.DumbAware
{
    public void runActivity(@NotNull final Project project) {
        if (project == null) {
            $$$reportNull$$$0(0);
        }
        if (ApplicationManager.getApplication().isUnitTestMode()) {
            return;
        }
        MessageUtils.get();
    }
    
    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "project", "rw/ai/Startup", "runActivity"));
    }
}
