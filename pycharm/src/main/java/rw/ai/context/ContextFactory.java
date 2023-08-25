// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.context;

import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.vfs.VirtualFile;
import rw.stack.ThreadError;
import com.jetbrains.python.debugger.PyStackFrame;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import rw.util.DebugUtils;
import com.jetbrains.python.psi.PyClass;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.python.psi.PyFunction;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import java.util.ArrayList;
import java.util.List;
import rw.handler.RunConfHandler;
import org.jetbrains.annotations.Nullable;
import com.intellij.psi.PsiElement;

public class ContextFactory
{
    public static List<Context> createContext(@Nullable final PsiElement element, @Nullable final RunConfHandler handler) {
        if (element == null) {
            return new ArrayList<Context>();
        }
        final PsiFile file = element.getContainingFile();
        final Document document = FileDocumentManager.getInstance().getDocument(file.getVirtualFile());
        if (document == null) {
            return new ArrayList<Context>();
        }
        final List<Context> ret = new ArrayList<Context>();
        final PyFunction function = (PyFunction)PsiTreeUtil.getParentOfType(element, (Class)PyFunction.class);
        final PyClass klass = (PyClass)PsiTreeUtil.getParentOfType(element, (Class)PyClass.class);
        if (klass != null) {
            ret.add(new ClassContext(file.getVirtualFile(), klass));
        }
        if (function != null) {
            PyStackFrame stackFrame = null;
            if (handler != null && handler.getDebugSession() != null) {
                stackFrame = DebugUtils.getTopStackFrame(handler.getDebugSession());
            }
            final PyClass containingClass = function.getContainingClass();
            if (containingClass != null) {
                ret.add(new MethodContext(file.getVirtualFile(), function, containingClass));
            }
            else {
                ret.add(new FunctionContext(file.getVirtualFile(), function));
            }
            if (stackFrame != null && stackFrame.getSourcePosition() != null && stackFrame.getSourcePosition().getFile().equals(file.getVirtualFile()) && stackFrame.getName().equals(function.getName())) {
                final ThreadError threadError = handler.getThreadErrorManager().getActiveError();
                if (threadError != null) {
                    ret.add(new FrameErrorContext(file.getVirtualFile(), (PsiNamedElement)function, threadError));
                }
                else {
                    ret.add(new FrameContext(file.getVirtualFile(), (PsiNamedElement)function, stackFrame));
                }
            }
        }
        return ret;
    }
    
    public static Context createNone() {
        return new NoneContext();
    }
    
    public static Context createForSelection(@Nullable final VirtualFile file, @NotNull final String code, final int firstLine, final int lastLine) {
        if (code == null) {
            $$$reportNull$$$0(0);
        }
        return new SelectionContext(file, code, firstLine, lastLine);
    }
    
    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "code", "rw/ai/context/ContextFactory", "createForSelection"));
    }
}
