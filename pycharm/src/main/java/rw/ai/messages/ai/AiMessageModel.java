package rw.ai.messages.ai;

import rw.ai.messages.MessageType;
import org.jetbrains.annotations.NotNull;
import rw.ai.context.Context;
import rw.ai.messages.MessageModel;

public class AiMessageModel extends MessageModel
{
    public AiMessageModel(@NotNull final Context context) {
        super(context);
        if (context == null) {
            $$$reportNull$$$0(0);
        }

    }
    
    public AiMessageModel() {
    }
    
    @Override
    public String getRole() {
        return "assistant";
    }
    
    @Override
    public MessageType getType() {
        return MessageType.AI;
    }
    
    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "context", "rw/ai/messages/ai/AiMessageModel", "<init>"));
    }
}
