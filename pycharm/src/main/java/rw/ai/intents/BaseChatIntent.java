package rw.ai.intents;

import rw.ai.ui.AiIcons;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.util.Iconable;
import com.intellij.codeInsight.intention.IntentionAction;

public abstract class BaseChatIntent implements IntentionAction, Iconable
{
    @NotNull
    public String getText() {
        final String familyName = this.getFamilyName();
        if (familyName == null) {
            $$$reportNull$$$0(0);
        }
        return familyName;
    }
    
    public boolean startInWriteAction() {
        return false;
    }
    
    public Icon getIcon(final int flags) {
        return AiIcons.ChatGptSmall;
    }
    
    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", "rw/ai/intents/BaseChatIntent", "getText"));
    }
}
