// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.cmds;

public class Message
{
    private final String role;
    private final String content;
    
    Message(final String role, final String content) {
        this.role = role;
        this.content = content;
    }
    
    public String getRole() {
        return this.role;
    }
    
    public String getContent() {
        return this.content;
    }
}
