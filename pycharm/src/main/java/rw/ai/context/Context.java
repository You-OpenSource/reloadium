package rw.ai.context;

import com.intellij.util.IconUtil;
import java.awt.Component;
import javax.swing.Icon;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = ClassContext.class, name = "CLASS"), @JsonSubTypes.Type(value = FunctionContext.class, name = "FUNCTION"), @JsonSubTypes.Type(value = MethodContext.class, name = "METHOD"), @JsonSubTypes.Type(value = FrameContext.class, name = "FRAME"), @JsonSubTypes.Type(value = SelectionContext.class, name = "SELECTION"), @JsonSubTypes.Type(value = FrameErrorContext.class, name = "FRAME_ERROR"), @JsonSubTypes.Type(value = NoneContext.class, name = "NONE") })
public abstract class Context
{
    public abstract ContextType getType();
    
    @JsonIgnore
    public void setType(final Object type) {
    }
    
    public abstract String getPromptSeed();
    
    public abstract String getPromptHint();
    
    public abstract String getSeparatorText();
    
    public abstract Icon getIcon();
    
    public Icon getIconSmall(final Component component) {
        final Icon scaledIcon = IconUtil.scale(this.getIcon(), component, 0.6f);
        return scaledIcon;
    }
}
