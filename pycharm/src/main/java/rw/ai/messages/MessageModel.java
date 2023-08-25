// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.messages;

import org.jetbrains.annotations.Nullable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import rw.ai.context.NoneContext;
import java.util.HashMap;
import org.jetbrains.annotations.NotNull;
import java.util.Map;
import rw.ai.context.Context;
import rw.ai.messages.ai.AiMessageModel;
import rw.ai.messages.re.ReMessageExceededQuota;
import rw.ai.messages.re.ReMessageStarRepo;
import rw.ai.messages.re.ReMessageTmpChat;
import rw.ai.messages.re.ReMessageBadKey;
import rw.ai.messages.user.UserMessageModel;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = UserMessageModel.class, name = "USER"), @JsonSubTypes.Type(value = ReMessageBadKey.Model.class, name = "RE_BAD_KEY"), @JsonSubTypes.Type(value = ReMessageTmpChat.Model.class, name = "RE_TMP_CHAT"), @JsonSubTypes.Type(value = ReMessageStarRepo.Model.class, name = "RE_STAR_REPO"), @JsonSubTypes.Type(value = ReMessageExceededQuota.Model.class, name = "RE_EXCEEDED_QUOTA"), @JsonSubTypes.Type(value = AiMessageModel.class, name = "AI") })
public abstract class MessageModel
{
    private final Context context;
    protected String rawContent;
    Map<Integer, MessagePart.Model> content;
    
    public MessageModel(@NotNull final Context context) {
        if (context == null) {
            $$$reportNull$$$0(0);
        }
        this.content = new HashMap<Integer, MessagePart.Model>();
        this.context = context;
        this.rawContent = "";
    }
    
    public MessageModel() {
        this.context = new NoneContext();
    }
    
    public Context getContext() {
        return this.context;
    }
    
    public void setContent(@NotNull final Integer id, @NotNull final MessagePart.Model model) {
        if (id == null) {
            $$$reportNull$$$0(1);
        }
        if (model == null) {
            $$$reportNull$$$0(2);
        }
        this.content.put(id, model);
    }
    
    public Map<Integer, MessagePart.Model> getContent() {
        return this.content;
    }
    
    public void append(@NotNull final String chunk) {
        if (chunk == null) {
            $$$reportNull$$$0(3);
        }
        this.setRawContent( this.rawContent + chunk);
    }
    
    public String getRawContent() {
        return this.rawContent;
    }
    
    public void setRawContent(@NotNull final String rawContent) {
        if (rawContent == null) {
            $$$reportNull$$$0(4);
        }
        this.rawContent = rawContent;
    }
    
    public String getAiContent() {
        return this.rawContent;
    }
    
    public abstract MessageType getType();
    
    @JsonIgnore
    public void setType(final String type) {
    }
    
    @Nullable
    public abstract String getRole();
    
    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        final String format = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        final Object[] args = new Object[3];
        switch (n) {
            default: {
                args[0] = "context";
                break;
            }
            case 1: {
                args[0] = "id";
                break;
            }
            case 2: {
                args[0] = "model";
                break;
            }
            case 3: {
                args[0] = "chunk";
                break;
            }
            case 4: {
                args[0] = "rawContent";
                break;
            }
        }
        args[1] = "rw/ai/messages/MessageModel";
        switch (n) {
            default: {
                args[2] = "<init>";
                break;
            }
            case 1:
            case 2: {
                args[2] = "setContent";
                break;
            }
            case 3: {
                args[2] = "append";
                break;
            }
            case 4: {
                args[2] = "setRawContent";
                break;
            }
        }
        throw new IllegalArgumentException(String.format(format, args));
    }
}
