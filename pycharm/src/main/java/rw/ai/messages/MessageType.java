package rw.ai.messages;

public enum MessageType
{
    USER, 
    AI, 
    RE_BAD_KEY, 
    RE_EXCEEDED_QUOTA, 
    RE_TMP_CHAT, 
    RE_STAR_REPO, 
    RE;
    
    private static /* synthetic */ MessageType[] $values() {
        return new MessageType[] { MessageType.USER, MessageType.AI, MessageType.RE_BAD_KEY, MessageType.RE_EXCEEDED_QUOTA, MessageType.RE_TMP_CHAT, MessageType.RE_STAR_REPO, MessageType.RE };
    }
    
    static {
//        $VALUES = $values();
    }
}
