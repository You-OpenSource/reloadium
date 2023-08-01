// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.chat;

import rw.ai.ui.AiIcons;
import javax.swing.Icon;
import com.intellij.openapi.util.NlsSafe;
import org.jetbrains.annotations.NonNls;
import com.intellij.openapi.fileEditor.impl.EditorWindow;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.fileTypes.FileType;

public class ChatFileType implements FileType
{
    public static final FileType INSTANCE;
    public static String EXTENSION;
    
    private ChatFileType() {
    }
    
    public static void open(@NotNull final Project project, @NotNull final VirtualFile file) {
        if (project == null) {
            $$$reportNull$$$0(0);
        }
        if (file == null) {
            $$$reportNull$$$0(1);
        }
        final FileEditorManagerEx fileEditorManager = FileEditorManagerEx.getInstanceEx(project);
        if (fileEditorManager.getCurrentWindow() == null) {
            fileEditorManager.openFile(file, true);
            return;
        }
        try {
            EditorWindow targetWindow = fileEditorManager.getNextWindow(fileEditorManager.getCurrentWindow());
            if (targetWindow == fileEditorManager.getCurrentWindow() || targetWindow == null) {
                targetWindow = fileEditorManager.getCurrentWindow().split(1, true, file, true);
            }
            if (targetWindow == null) {
                fileEditorManager.openFile(file, true);
                return;
            }
            fileEditorManager.openFileWithProviders(file, true, targetWindow);
        }
        catch (Exception e) {
            fileEditorManager.openFile(file, true);
        }
    }
    
    @NotNull
    public String getDefaultExtension() {
        final String extension = ChatFileType.EXTENSION;
        if (extension == null) {
            $$$reportNull$$$0(2);
        }
        return extension;
    }
    
    @NotNull
    @NonNls
    public String getName() {
        return "Chat";
    }
    
    @NotNull
    @NlsSafe
    public String getDescription() {
        return "ChatGpt";
    }
    
    public Icon getIcon() {
        return AiIcons.ChatGptSmall;
    }
    
    public boolean isBinary() {
        return false;
    }
    
    static {
        INSTANCE = (FileType)new ChatFileType();
        ChatFileType.EXTENSION = "chat";
    }
    
    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        String format = null;
        switch (n) {
            default: {
                format = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                break;
            }
            case 2: {
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
            case 2: {
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
            case 1: {
                args[0] = "file";
                break;
            }
            case 2: {
                args[0] = "rw/ai/chat/ChatFileType";
                break;
            }
        }
        switch (n) {
            default: {
                args[1] = "rw/ai/chat/ChatFileType";
                break;
            }
            case 2: {
                args[1] = "getDefaultExtension";
                break;
            }
        }
        switch (n) {
            default: {
                args[2] = "open";
                break;
            }
            case 2: {
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
            case 2: {
                ex = new IllegalStateException(format2);
                break;
            }
        }
        throw ex;
    }
}
