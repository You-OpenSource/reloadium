// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.context;

import com.intellij.psi.PsiNamedElement;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public abstract class PsiContext extends Context
{
    @NotNull
    private final PsiData psiData;
    
    public PsiContext(@NotNull final VirtualFile file, final PsiNamedElement element) {
        if (file == null) {
            $$$reportNull$$$0(0);
        }
        this.psiData = new PsiData(file, element);
    }
    
    public PsiContext() {
        this.psiData = null;
    }
    
    @NotNull
    public PsiData getPsiData() {
        final PsiData psiData = this.psiData;
        if (psiData == null) {
            $$$reportNull$$$0(1);
        }
        return psiData;
    }
    
    protected String getLineNumberedCode() {
        final String ret = ContextUtils.getLineNumberedCode(this.psiData.getText(), this.psiData.getLineOffset());
        return ret;
    }
    
    @Override
    public String getSeparatorText() {
        String ret =  this.psiData.getName() + this.psiData.getLineOffset() + 1;
        if (this.psiData.getFile() != null) {
            ret =   ret + ContextUtils.getShortFilename(this.psiData.getFile());
        }
        ret =  ret;
        return ret;
    }
    
    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        String format = null;
        switch (n) {
            default: {
                format = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                break;
            }
            case 1: {
                format = "@NotNull method %s.%s must not return null";
                break;
            }
        }
        int n2 = 0;
        switch (n) {
            default: {
                n2 = 3;
                break;
            }
            case 1: {
                n2 = 2;
                break;
            }
        }
        final Object[] args = new Object[n2];
        switch (n) {
            default: {
                args[0] = "file";
                break;
            }
            case 1: {
                args[0] = "rw/ai/context/PsiContext";
                break;
            }
        }
        switch (n) {
            default: {
                args[1] = "rw/ai/context/PsiContext";
                break;
            }
            case 1: {
                args[1] = "getPsiData";
                break;
            }
        }
        switch (n) {
            default: {
                args[2] = "<init>";
                break;
            }
            case 1: {
                break;
            }
        }
        final String format2 = String.format(format, args);
        RuntimeException ex = null;
        switch (n) {
            default: {
                ex = new IllegalArgumentException(format2);
                break;
            }
            case 1: {
                ex = new IllegalStateException(format2);
                break;
            }
        }
        throw ex;
    }
}
