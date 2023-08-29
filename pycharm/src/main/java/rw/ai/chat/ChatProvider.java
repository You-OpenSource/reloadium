package rw.ai.chat;

import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.fileEditor.impl.DefaultPlatformFileEditorProvider;

public final class ChatProvider implements DefaultPlatformFileEditorProvider, DumbAware
{
    public static final String PROVIDER_ID = "ChatProvider";
    
    public boolean accept(@NotNull final Project project, @NotNull final VirtualFile file) {
        if (project == null) {
            $$$reportNull$$$0(0);
        }
        if (file == null) {
            $$$reportNull$$$0(1);
        }
        final String extension = file.getExtension();
        return extension != null && extension.equals(ChatFileType.EXTENSION);
    }
    
    @NotNull
    public FileEditor createEditor(@NotNull final Project project, @NotNull final VirtualFile file) {
        if (project == null) {
            $$$reportNull$$$0(2);
        }
        if (file == null) {
            $$$reportNull$$$0(3);
        }
        final Chat chat;
        final FileEditor ret = (FileEditor)(chat = new Chat(project, file));
        if (chat == null) {
            $$$reportNull$$$0(4);
        }
        return (FileEditor)chat;
    }
    
    @NotNull
    public String getEditorTypeId() {
        return "ChatProvider";
    }
    
    @NotNull
    public FileEditorPolicy getPolicy() {
        final FileEditorPolicy hide_OTHER_EDITORS = FileEditorPolicy.HIDE_OTHER_EDITORS;
        if (hide_OTHER_EDITORS == null) {
            $$$reportNull$$$0(5);
        }
        return hide_OTHER_EDITORS;
    }
    
    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        String format = null;
        switch (n) {
            default: {
                format = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                break;
            }
            case 4:
            case 5: {
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
            case 4:
            case 5: {
                n2 = 2;
                break;
            }
        }
        final Object[] args = new Object[n2];
        switch (n) {
            default: {
                args[0] = "project";
                break;
            }
            case 1:
            case 3: {
                args[0] = "file";
                break;
            }
            case 4:
            case 5: {
                args[0] = "rw/ai/chat/ChatProvider";
                break;
            }
        }
        switch (n) {
            default: {
                args[1] = "rw/ai/chat/ChatProvider";
                break;
            }
            case 4: {
                args[1] = "createEditor";
                break;
            }
            case 5: {
                args[1] = "getPolicy";
                break;
            }
        }
        switch (n) {
            default: {
                args[2] = "accept";
                break;
            }
            case 2:
            case 3: {
                args[2] = "createEditor";
                break;
            }
            case 4:
            case 5: {
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
            case 4:
            case 5: {
                ex = new IllegalStateException(format2);
                break;
            }
        }
        throw ex;
    }
}
