// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.messages.user;

import rw.ai.messages.MessageType;
import org.jetbrains.annotations.NotNull;
import rw.ai.context.Context;
import rw.ai.messages.MessageModel;

public class UserMessageModel extends MessageModel
{
    public UserMessageModel(@NotNull final Context context) {
        super(context);
        if (context == null) {
            $$$reportNull$$$0(0);
        }
    }
    
    public UserMessageModel() {
    }
    
    @Override
    public String getAiContent() {
        String seed = "";
        final String extraPrompt = "Don't add line numbers to your code blocks";
        if (!this.getContext().getPromptSeed().isEmpty()) {
            seed =  this.getContext().getPromptSeed();
        }
        final String ret = seed + extraPrompt + this.rawContent;
        return ret;
    }
    
    @Override
    public MessageType getType() {
        return MessageType.USER;
    }
    
    @Override
    public String getRole() {
        return "user";
    }
    
    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "context", "rw/ai/messages/user/UserMessageModel", "<init>"));
    }
}
