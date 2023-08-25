// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.context;

import rw.ai.ui.AiIcons;
import javax.swing.Icon;
import com.intellij.psi.PsiNamedElement;
import com.jetbrains.python.psi.PyFunction;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.vfs.VirtualFile;

public class FunctionContext extends PsiContext
{
    public FunctionContext(@NotNull final VirtualFile file, final PyFunction element) {
        super(file, (PsiNamedElement)element);
        if (file == null) {
            $$$reportNull$$$0(0);
        }

    }
    
    public FunctionContext() {
    }
    
    @Override
    public String getPromptSeed() {
        final String ret =   this.getLineNumberedCode();
        return ret;
    }
    
    @Override
    public String getPromptHint() {
        return String.format("Asking about \"%s\" function. Ask questions like \"Refactor this function\" or \"Add type hints\"", this.getPsiData().getName());
    }
    
    @Override
    public Icon getIcon() {
        return AiIcons.Context.Function;
    }
    
    @Override
    public ContextType getType() {
        return ContextType.FUNCTION;
    }
    
    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "file", "rw/ai/context/FunctionContext", "<init>"));
    }
}
