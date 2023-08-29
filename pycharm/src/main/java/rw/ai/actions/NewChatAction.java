package rw.ai.actions;

import com.intellij.psi.PsiElement;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import org.jetbrains.annotations.Nullable;
import rw.ai.chat.ChatFileType;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.NonNls;
import com.intellij.psi.PsiDirectory;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.actionSystem.AnActionEvent;
import rw.ai.ui.AiIcons;
import rw.ai.lang.AiBundle;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiFile;
import com.intellij.ide.actions.CreateFromTemplateAction;

class NewChatAction extends CreateFromTemplateAction<PsiFile> implements DumbAware
{
    Project project;
    
    public NewChatAction() {
        super(AiBundle.message("ai.new.chat.file", new Object[0]), AiBundle.message("ai.new.chat.file.description", new Object[0]), AiIcons.ChatGptSmall);
    }
    
    public void update(@NotNull final AnActionEvent e) {
        if (e == null) {
            $$$reportNull$$$0(0);
        }
        e.getPresentation().setEnabledAndVisible(true);
    }
    
    @NlsContexts.Command
    protected String getActionName(final PsiDirectory directory, @NonNls @NotNull final String newName, @NonNls final String templateName) {
        if (newName == null) {
            $$$reportNull$$$0(1);
        }
        return AiBundle.message("ai.new.chat.file.action", newName);
    }
    
    public void beforeActionPerformedUpdate(@NotNull final AnActionEvent e) {
        if (e == null) {
            $$$reportNull$$$0(2);
        }
        super.beforeActionPerformedUpdate(e);
        this.project = e.getProject();
    }
    
    @Nullable
    protected PsiFile createFile(final String name, final String templateName, final PsiDirectory dir) {
        final PsiFile file = dir.createFile(  name+ChatFileType.EXTENSION);
        ChatFileType.open(this.project, file.getVirtualFile());
        return file;
    }
    
    protected void buildDialog(final Project project, final PsiDirectory directory, final CreateFileFromTemplateDialog.Builder builder) {
        builder.setTitle(AiBundle.message("ai.new.chat.file.title", new Object[0])).addKind(AiBundle.message("ai.new.chat.file", new Object[0]), AiIcons.ChatGptSmall, "AiChat");
    }
    
    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        final String format = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        final Object[] args = new Object[3];
        switch (n) {
            default: {
                args[0] = "e";
                break;
            }
            case 1: {
                args[0] = "newName";
                break;
            }
        }
        args[1] = "rw/ai/actions/NewChatAction";
        switch (n) {
            default: {
                args[2] = "update";
                break;
            }
            case 1: {
                args[2] = "getActionName";
                break;
            }
            case 2: {
                args[2] = "beforeActionPerformedUpdate";
                break;
            }
        }
        throw new IllegalArgumentException(String.format(format, args));
    }
}
