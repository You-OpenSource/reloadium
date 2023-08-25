// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.context;

import rw.ai.ui.AiIcons;
import javax.swing.Icon;
import com.intellij.psi.PsiNamedElement;
import com.jetbrains.python.psi.PyClass;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.vfs.VirtualFile;

public class ClassContext extends PsiContext
{
    public ClassContext(@NotNull final VirtualFile file, final PyClass element) {
        super(file, (PsiNamedElement)element);
        if (file == null) {
            $$$reportNull$$$0(0);
        }

    }
    
    public ClassContext() {
    }
    
    @Override
    public String getPromptSeed() {
        final String ret =  this.getLineNumberedCode();
        return ret;
    }
    
    @Override
    public String getPromptHint() {
        return String.format("Asking about \"%s\" class. Ask questions like \"Refactor this class\" or \"Add type hints\"", this.getPsiData().getName());
    }
    
    @Override
    public Icon getIcon() {
        return AiIcons.Context.Class;
    }
    
    @Override
    public ContextType getType() {
        return ContextType.CLASS;
    }
    
    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "file", "rw/ai/context/ClassContext", "<init>"));
    }
}
