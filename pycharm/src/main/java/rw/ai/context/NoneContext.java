package rw.ai.context;

import rw.ai.ui.AiIcons;
import javax.swing.Icon;

public class NoneContext extends Context
{
    @Override
    public ContextType getType() {
        return ContextType.NONE;
    }
    
    @Override
    public String getPromptSeed() {
        return "";
    }
    
    @Override
    public String getPromptHint() {
        return "Ask any questions";
    }
    
    @Override
    public Icon getIcon() {
        return AiIcons.Context.NoContext;
    }
    
    @Override
    public String getSeparatorText() {
        return " No Context";
    }
}
