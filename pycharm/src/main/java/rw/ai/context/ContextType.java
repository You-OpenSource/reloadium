package rw.ai.context;

public enum ContextType
{
    CLASS, 
    FUNCTION, 
    METHOD, 
    FRAME, 
    SELECTION, 
    FRAME_ERROR, 
    NONE;
    
    private static /* synthetic */ ContextType[] $values() {
        return new ContextType[] { ContextType.CLASS, ContextType.FUNCTION, ContextType.METHOD, ContextType.FRAME, ContextType.SELECTION, ContextType.FRAME_ERROR, ContextType.NONE };
    }
    
//    static {
//        $VALUES = $values();
//    }
}
