package rw.ai.context;

import rw.ai.ui.AiIcons;
import javax.swing.Icon;
import rw.stack.ThreadError;
import org.jetbrains.annotations.Nullable;
import com.intellij.psi.PsiNamedElement;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.vfs.VirtualFile;

public class FrameErrorContext extends PsiContext
{
    private final String errorMsg;
    private final int errorLine;
    
    public FrameErrorContext(@NotNull final VirtualFile file, @Nullable final PsiNamedElement element, final ThreadError threadError) {
        super(file, element);
        if (file == null) {
            $$$reportNull$$$0(0);
        }

        this.errorMsg = threadError.getMsg();
        this.errorLine = threadError.getLine();
    }
    
    public FrameErrorContext() {
        this.errorLine = 0;
        this.errorMsg = "";
    }
    
    @Override
    public String getPromptSeed() {
        String ret =  this.getLineNumberedCode();
        ret = ret + String.format("Line %s ", this.errorLine) + String.format("raised an error `%s`", this.errorMsg);
        return ret;
    }
    
    @Override
    public String getPromptHint() {
        return "Asking about current error. Ask questions like \"How to fix this\" or \"Explain this error\"";
    }
    
    public int getErrorLine() {
        return this.errorLine;
    }
    
    public String getErrorMsg() {
        return this.errorMsg;
    }
    
    @Override
    public Icon getIcon() {
        return AiIcons.Context.FrameError;
    }
    
    @Override
    public ContextType getType() {
        return ContextType.FRAME_ERROR;
    }
    
    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "file", "rw/ai/context/FrameErrorContext", "<init>"));
    }
}
