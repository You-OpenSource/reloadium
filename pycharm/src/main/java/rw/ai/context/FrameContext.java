// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.context;

import rw.ai.ui.AiIcons;
import javax.swing.Icon;
import com.jetbrains.python.debugger.PyStackFrame;
import com.intellij.psi.PsiNamedElement;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.vfs.VirtualFile;

public class FrameContext extends PsiContext
{
    public FrameContext(@NotNull final VirtualFile file, final PsiNamedElement element, final PyStackFrame stackFrame) {
        super(file, element);
        if (file == null) {
            $$$reportNull$$$0(0);
        }

    }
    
    FrameContext() {
    }
    
    @Override
    public String getPromptSeed() {
        final String ret =   this.getLineNumberedCode();
        return ret;
    }
    
    @Override
    public String getPromptHint() {
        return "Asking about current frame. Ask questions like \"Make this function faster\" or \"Reduce memory usage\"";
    }
    
    @Override
    public Icon getIcon() {
        return AiIcons.Context.Frame;
    }
    
    @Override
    public ContextType getType() {
        return ContextType.FRAME;
    }
    
    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "file", "rw/ai/context/FrameContext", "<init>"));
    }
}
