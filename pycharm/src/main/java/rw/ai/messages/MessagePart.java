// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.messages;

import javax.swing.JPanel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import rw.ai.messages.code.CodeViewer;
import rw.ai.messages.ai.AiMarkdown;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.Disposable;

public abstract class MessagePart implements Disposable
{
    public abstract View getView();
    
    public abstract Model getModel();
    
    public abstract void setContent(@NotNull final String p0);
    
    public abstract void onFinished();
    
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes({ @JsonSubTypes.Type(value = Markdown.Model.class, name = "MARKDOWN"), @JsonSubTypes.Type(value = AiMarkdown.Model.class, name = "MARKDOWN"), @JsonSubTypes.Type(value = CodeViewer.Model.class, name = "CODE") })
    public abstract static class Model
    {
        String content;
        
        public Model() {
            this.content = "";
        }
        
        public String getContent() {
            return this.content;
        }
        
        public void setContent(final String content) {
            this.content = content;
        }
        
        public abstract MessagePartType getType();
        
        @JsonIgnore
        public void setType(final Object type) {
        }
    }
    
    public abstract static class View extends JPanel
    {
        public abstract void setContent(@NotNull final String p0);
    }
}
