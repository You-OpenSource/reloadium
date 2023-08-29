package rw.ai.intents;

import com.intellij.util.IncorrectOperationException;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import rw.ai.chat.ChatFileType;
import com.intellij.openapi.editor.ex.util.EditorUtil;
import com.intellij.psi.PsiFile;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import rw.ai.lang.AiBundle;
import rw.ai.context.ContextManager;
import rw.ai.context.ContextType;
import java.util.Map;

public class CreateChatFromContextIntent extends BaseChatIntent
{
    private static Map<ContextType, String> contextTypeToText;
    
    @NotNull
    public String getFamilyName() {
        final String message = AiBundle.message("ai.ask.ai", CreateChatFromContextIntent.contextTypeToText.get(ContextManager.get().getCurrentContext().getType()));
        if (message == null) {
            $$$reportNull$$$0(0);
        }
        return message;
    }
    
    public boolean isAvailable(@NotNull final Project project, final Editor editor, final PsiFile file) {
        if (project == null) {
            $$$reportNull$$$0(1);
        }
        if (editor == null) {
            return false;
        }
        if (!EditorUtil.isRealFileEditor(editor)) {
            return false;
        }
        final boolean ret = ContextManager.get().getCurrentContext().getType() != ContextType.NONE;
        return ret;
    }
    
    public void invoke(@NotNull final Project project, final Editor editor, final PsiFile file) throws IncorrectOperationException {
        if (project == null) {
            $$$reportNull$$$0(2);
        }
        final LightVirtualFile tempFile = new LightVirtualFile("*tmp.chat", ChatFileType.INSTANCE, (CharSequence)"");
        ChatFileType.open(project, (VirtualFile)tempFile);
    }
    
    static {
        CreateChatFromContextIntent.contextTypeToText = Map.ofEntries(Map.entry(ContextType.FUNCTION, "function"), Map.entry(ContextType.CLASS, "class"), Map.entry(ContextType.FRAME, "frame"), Map.entry(ContextType.FRAME_ERROR, "frame error"), Map.entry(ContextType.SELECTION, "selection"), Map.entry(ContextType.METHOD, "method"), Map.entry(ContextType.NONE, "anything"));
    }
    
    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        String format = null;
        switch (n) {
            default: {
                format = "@NotNull method %s.%s must not return null";
                break;
            }
            case 1:
            case 2: {
                format = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                break;
            }
        }
        int n2 = 0;
        switch (n) {
            default: {
                n2 = 2;
                break;
            }
            case 1:
            case 2: {
                n2 = 3;
                break;
            }
        }
        final Object[] args = new Object[n2];
        switch (n) {
            default: {
                args[0] = "rw/ai/intents/CreateChatFromContextIntent";
                break;
            }
            case 1:
            case 2: {
                args[0] = "project";
                break;
            }
        }
        switch (n) {
            default: {
                args[1] = "getFamilyName";
                break;
            }
            case 1:
            case 2: {
                args[1] = "rw/ai/intents/CreateChatFromContextIntent";
                break;
            }
        }
        switch (n) {
            case 1: {
                args[2] = "isAvailable";
                break;
            }
            case 2: {
                args[2] = "invoke";
                break;
            }
        }
        final String format2 = String.format(format, args);
        RuntimeException ex = null;
        switch (n) {
            default: {
                ex = new IllegalStateException(format2);
                break;
            }
            case 1:
            case 2: {
                ex = new IllegalArgumentException(format2);
                break;
            }
        }
        throw ex;
    }
}
