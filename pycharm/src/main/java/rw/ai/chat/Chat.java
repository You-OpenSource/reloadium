// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.chat;

import java.util.Iterator;
import com.intellij.psi.codeStyle.CodeStyleSettingsChangeEvent;
import com.intellij.openapi.util.Key;
import java.beans.PropertyChangeListener;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.openapi.fileEditor.FileEditorState;
import rw.ai.lang.AiBundle;
import org.jetbrains.annotations.Nullable;
import javax.swing.JComponent;
import com.intellij.openapi.application.ApplicationManager;
import rw.ai.dialog.Dialog;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import rw.ai.dialog.DialogManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.codeStyle.CodeStyleSettingsListener;
import com.intellij.openapi.fileEditor.FileEditor;

public class Chat implements FileEditor, CodeStyleSettingsListener, DumbAware
{
    private static final Logger LOGGER;
    static int SMALL_MODE_WIDTH;
    private final DialogManager dialogManager;
    private final ChatComponent component;
    private final VirtualFile file;
    
    public Chat(@NotNull final Project project, @NotNull final VirtualFile file) {
        if (project == null) {
            $$$reportNull$$$0(0);
        }
        if (file == null) {
            $$$reportNull$$$0(1);
        }
        this.dialogManager = new DialogManager(project, file);
        this.file = file;
        this.component = new ChatComponent(new SidePanel.Listener() {
            @Override
            public void onNewDialogPressed() {
                Chat.this.newDialog();
                Chat.this.closeSidePanelIfNeeded();
            }
            
            @Override
            public void onRenameDialog(final Integer id, final String newName) {
                final Dialog dialog = Chat.this.dialogManager.getDialog(id);
                dialog.getModel().setName(newName);
                Chat.this.save();
            }
            
            @Override
            public void onDeleteDialog(final Integer id) {
                final Dialog dialog = Chat.this.dialogManager.getDialog(id);
                if (Chat.this.dialogManager.getDialogs().size() == 1) {
                    dialog.clear();
                    dialog.getView().repaint();
                    Chat.this.component.getSidePanel().onDialogRenamed(dialog);
                }
                else {
                    Chat.this.component.onDialogDeleted(dialog);
                    Chat.this.dialogManager.deleteDialog(dialog);
                    Chat.this.component.onActiveDialogChanged(Chat.this.dialogManager.getActiveDialog());
                }
                Chat.this.save();
            }
            
            @Override
            public void onDialogPressed(final Integer id) {
                final Dialog dialog = Chat.this.dialogManager.getDialog(id);
                Chat.this.component.onActiveDialogChanged(dialog);
                Chat.this.dialogManager.setActiveDialog(dialog);
                Chat.this.closeSidePanelIfNeeded();
                Chat.this.save();
            }
            
            @Override
            public void onOpenChanged(final boolean open) {
                Chat.this.save();
            }
        });
        this.load();
        if (this.isTmpChat()) {
            this.component.getSidePanel().setOpen(false);
        }
    }
    
    private void closeSidePanelIfNeeded() {
        if (ApplicationManager.getApplication().isUnitTestMode()) {
            return;
        }
        if (this.component.getWidth() < Chat.SMALL_MODE_WIDTH) {
            this.component.getSidePanel().setOpen(false);
        }
    }
    
    @NotNull
    public ChatComponent getComponent() {
        final ChatComponent component = this.component;
        if (component == null) {
            $$$reportNull$$$0(2);
        }
        return component;
    }
    
    @Nullable
    public JComponent getPreferredFocusedComponent() {
        return null;
    }
    
    @NotNull
    public String getName() {
        final String message = AiBundle.message("ai.chat.name", new Object[0]);
        if (message == null) {
            $$$reportNull$$$0(3);
        }
        return message;
    }
    
    public void setState(@NotNull final FileEditorState state) {
        if (state == null) {
            $$$reportNull$$$0(4);
        }
        final int a = 1;
    }
    
    public boolean isTmpChat() {
        return this.file instanceof LightVirtualFile;
    }
    
    public boolean isModified() {
        return false;
    }
    
    public boolean isValid() {
        return true;
    }
    
    public void addPropertyChangeListener(@NotNull final PropertyChangeListener listener) {
        if (listener == null) {
            $$$reportNull$$$0(5);
        }
    }
    
    public void removePropertyChangeListener(@NotNull final PropertyChangeListener listener) {
        if (listener == null) {
            $$$reportNull$$$0(6);
        }
    }
    
    @NotNull
    public VirtualFile getFile() {
        final VirtualFile file = this.file;
        if (file == null) {
            $$$reportNull$$$0(7);
        }
        return file;
    }
    
    public void dispose() {
        this.dialogManager.dispose();
    }
    
    @Nullable
    public <T> T getUserData(@NotNull final Key<T> key) {
        if (key == null) {
            $$$reportNull$$$0(8);
        }
        return null;
    }
    
    public <T> void putUserData(@NotNull final Key<T> key, @Nullable final T value) {
        if (key == null) {
            $$$reportNull$$$0(9);
        }
    }
    
    public void codeStyleSettingsChanged(@NotNull final CodeStyleSettingsChangeEvent event) {
        if (event == null) {
            $$$reportNull$$$0(10);
        }
        this.updateEditor();
    }
    
    private void updateEditor() {
    }
    
    public void load() {
        Chat.LOGGER.info(String.format("Loading \"%s\"", this.getFile()));
        final DialogManager.FileContent fileContent = this.dialogManager.load(this);
        if (fileContent == null) {
            this.newDialog();
        }
        else {
            for (final Dialog d : this.dialogManager.getDialogs().values()) {
                this.component.onNewDialog(d);
            }
            this.component.onActiveDialogChanged(this.dialogManager.getActiveDialog());
            this.loadUiState(fileContent.uiState);
        }
    }
    
    private void newDialog() {
        if (this.dialogManager.getActiveDialog() != null && this.dialogManager.getActiveDialog().getModel().getMessages().isEmpty()) {
            return;
        }
        final Dialog dialog = this.dialogManager.newDialog(this);
        this.component.onNewDialog(dialog);
        this.component.onActiveDialogChanged(dialog);
        this.dialogManager.setActiveDialog(dialog);
        this.save();
    }
    
    public void save() {
        Chat.LOGGER.info(String.format("Saving to \"%s\"", this.getFile()));
        this.dialogManager.save(this);
    }
    
    public void loadUiState(final UiState state) {
        this.component.getSidePanel().setOpen(state.sidePanelOpen);
    }
    
    public UiState getUiState() {
        final UiState uiState = new UiState();
        uiState.sidePanelOpen = this.component.getSidePanel().isOpen();
        return uiState;
    }
    
    public DialogManager getDialogManager() {
        return this.dialogManager;
    }
    
    static {
        LOGGER = Logger.getInstance((Class)Chat.class);
        Chat.SMALL_MODE_WIDTH = 900;
    }
    
    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        String format = null;
        switch (n) {
            default: {
                format = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                break;
            }
            case 2:
            case 3:
            case 7: {
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
            case 2:
            case 3:
            case 7: {
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
            case 2:
            case 3:
            case 7: {
                args[0] = "rw/ai/chat/Chat";
                break;
            }
            case 4: {
                args[0] = "state";
                break;
            }
            case 5:
            case 6: {
                args[0] = "listener";
                break;
            }
            case 8:
            case 9: {
                args[0] = "key";
                break;
            }
            case 10: {
                args[0] = "event";
                break;
            }
        }
        switch (n) {
            default: {
                args[1] = "rw/ai/chat/Chat";
                break;
            }
            case 2: {
                args[1] = "getComponent";
                break;
            }
            case 3: {
                args[1] = "getName";
                break;
            }
            case 7: {
                args[1] = "getFile";
                break;
            }
        }
        switch (n) {
            default: {
                args[2] = "<init>";
                break;
            }
            case 2:
            case 3:
            case 7: {
                break;
            }
            case 4: {
                args[2] = "setState";
                break;
            }
            case 5: {
                args[2] = "addPropertyChangeListener";
                break;
            }
            case 6: {
                args[2] = "removePropertyChangeListener";
                break;
            }
            case 8: {
                args[2] = "getUserData";
                break;
            }
            case 9: {
                args[2] = "putUserData";
                break;
            }
            case 10: {
                args[2] = "codeStyleSettingsChanged";
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
            case 2:
            case 3:
            case 7: {
                ex = new IllegalStateException(format2);
                break;
            }
        }
        throw ex;
    }
    
    public static class UiState
    {
        public boolean sidePanelOpen;
    }
}
