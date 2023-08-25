// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.dialog;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.Nullable;
import java.util.Iterator;
import org.apache.commons.io.FileUtils;
import com.intellij.openapi.command.WriteCommandAction;
import java.io.IOException;
import rw.audit.RwSentry;
import java.nio.charset.StandardCharsets;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.testFramework.LightVirtualFile;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import rw.ai.chat.Chat;
import java.util.HashMap;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import java.util.Map;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.Disposable;

public class DialogManager implements Disposable
{
    public static final String FILE_VERSION = "0.2.0";
    private static final Logger LOGGER;
    private final Map<Integer, Dialog> dialogs;
    private final VirtualFile file;
    private final Project project;
    private Integer idCounter;
    private Dialog activeDialog;
    
    public DialogManager(@NotNull final Project project, @NotNull final VirtualFile file) {
        if (project == null) {
            $$$reportNull$$$0(0);
        }
        if (file == null) {
            $$$reportNull$$$0(1);
        }
        this.dialogs = new HashMap<Integer, Dialog>();
        this.project = project;
        this.file = file;
        this.idCounter = -1;
        this.activeDialog = null;
    }
    
    private synchronized void doSave(@NotNull final Chat chat) {
        if (chat == null) {
            $$$reportNull$$$0(2);
        }
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
            objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            final FileContent fileContent = new FileContent();
            fileContent.version = "0.2.0";
            fileContent.activeDialogId = this.activeDialog.model.getId();
            fileContent.uiState = chat.getUiState();
            for (final Dialog d : this.dialogs.values()) {
                fileContent.dialogs.add(d.getModel().getClone());
            }
            final String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString((Object)fileContent);
            if (this.file instanceof LightVirtualFile) {
                ApplicationManager.getApplication().invokeLater(() -> WriteCommandAction.runWriteCommandAction(this.project, () -> {
                    try {
                        this.file.setBinaryContent(jsonString.getBytes(StandardCharsets.UTF_8));
                    }
                    catch (IOException e) {
                        RwSentry.get().captureException(e, false);
                    }
                }));
            }
            else {
                FileUtils.writeStringToFile(this.file.toNioPath().toFile(), jsonString, "utf-8");
            }
        }
        catch (IOException e2) {
            RwSentry.get().captureException(e2, false);
        }
    }
    
    public void save(@NotNull final Chat chat) {
        if (chat == null) {
            $$$reportNull$$$0(3);
        }
        ApplicationManager.getApplication().invokeLater(() -> this.doSave(chat));
    }
    
    @NotNull
    public Dialog dialogFactory(final Integer id, @NotNull final Chat chat, @Nullable final Dialog.Model model) {
        if (chat == null) {
            $$$reportNull$$$0(4);
        }
        final Dialog dialog2;
        final Dialog dialog = dialog2 = new Dialog(this.project, this, id, model, new Dialog.Listener() {
            @Override
            public void onChanged() {
                DialogManager.this.save(chat);
            }
            
            @Override
            public void onRenamed(@NotNull final Dialog dialog, @NotNull final String name) {
                if (dialog == null) {
                    $$$reportNull$$$0(0);
                }
                if (name == null) {
                    $$$reportNull$$$0(1);
                }
                chat.getComponent().getSidePanel().onDialogRenamed(dialog);
            }
            
            private /* synthetic */ void $$$reportNull$$$0(final int n) {
                final String format = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                final Object[] args = new Object[3];
                switch (n) {
                    default: {
                        args[0] = "dialog";
                        break;
                    }
                    case 1: {
                        args[0] = "name";
                        break;
                    }
                }
                args[1] = "rw/ai/dialog/DialogManager$1";
                args[2] = "onRenamed";
                throw new IllegalArgumentException(String.format(format, args));
            }
        });
        if (dialog2 == null) {
            $$$reportNull$$$0(5);
        }
        return dialog2;
    }
    
    @Nullable
    public FileContent load(@NotNull final Chat chat) {
        if (chat == null) {
            $$$reportNull$$$0(6);
        }
        if (this.file instanceof LightVirtualFile) {
            return null;
        }
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            final String jsonString = FileUtils.readFileToString(this.file.toNioPath().toFile(), "utf-8");
            if (jsonString.isBlank()) {
                return null;
            }
            final JsonNode rawContent = objectMapper.readTree(jsonString);
            final String version = rawContent.get("version").asText();
            if (!version.equals("0.2.0")) {
                DialogManager.LOGGER.info(String.format("Incompatible chat file version, got %s, needed %s", version, "0.2.0"));
                return null;
            }
            final FileContent fileContent = (FileContent)objectMapper.readValue(jsonString, (Class)FileContent.class);
            for (final Dialog.Model d : fileContent.dialogs) {
                try {
                    final Dialog dialog = this.dialogFactory(d.getId(), chat, d);
                    this.dialogs.put(d.getId(), dialog);
                    if (this.idCounter >= d.getId()) {
                        continue;
                    }
                    this.idCounter = d.getId();
                }
                catch (Exception e) {
                    RwSentry.get().captureException(e, false);
                }
            }
            this.setActiveDialog(this.dialogs.get(fileContent.activeDialogId));
            return fileContent;
        }
        catch (Exception e2) {
            RwSentry.get().captureException(e2, false);
            return null;
        }
    }
    
    public VirtualFile getFile() {
        return this.file;
    }
    
    public Dialog newDialog(@NotNull final Chat chat) {
        if (chat == null) {
            $$$reportNull$$$0(7);
        }
        ++this.idCounter;
        final Dialog ret = this.dialogFactory(this.idCounter, chat, null);
        this.dialogs.put(this.idCounter, ret);
        this.activeDialog = ret;
        if (this.file instanceof LightVirtualFile) {
            ret.addReMessageTmpChat();
        }
        return ret;
    }
    
    public Map<Integer, Dialog> getDialogs() {
        return this.dialogs;
    }
    
    public Dialog getDialog(final Integer id) {
        return this.dialogs.get(id);
    }
    
    public void deleteDialog(final Dialog dialog) {
        this.dialogs.remove(dialog.getModel().getId());
        final Dialog newActive = this.dialogs.values().stream().toList().get(this.dialogs.values().size() - 1);
        this.setActiveDialog(newActive);
    }
    
    public Dialog getActiveDialog() {
        return this.activeDialog;
    }
    
    public void setActiveDialog(final Dialog dialog) {
        this.activeDialog = dialog;
        if (!this.activeDialog.isLoaded()) {
            this.activeDialog.loadMessages();
        }
    }
    
    public void dispose() {
        this.dialogs.values().forEach(Dialog::dispose);
    }
    
    static {
        LOGGER = Logger.getInstance((Class)DialogManager.class);
    }
    
    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        String format = null;
        switch (n) {
            default: {
                format = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                break;
            }
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
            case 1: {
                args[0] = "file";
                break;
            }
            case 2:
            case 3:
            case 4:
            case 6:
            case 7: {
                args[0] = "chat";
                break;
            }
            case 5: {
                args[0] = "rw/ai/dialog/DialogManager";
                break;
            }
        }
        switch (n) {
            default: {
                args[1] = "rw/ai/dialog/DialogManager";
                break;
            }
            case 5: {
                args[1] = "dialogFactory";
                break;
            }
        }
        switch (n) {
            default: {
                args[2] = "<init>";
                break;
            }
            case 2: {
                args[2] = "doSave";
                break;
            }
            case 3: {
                args[2] = "save";
                break;
            }
            case 4: {
                args[2] = "dialogFactory";
                break;
            }
            case 5: {
                break;
            }
            case 6: {
                args[2] = "load";
                break;
            }
            case 7: {
                args[2] = "newDialog";
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
            case 5: {
                ex = new IllegalStateException(format2);
                break;
            }
        }
        throw ex;
    }
    
    public static class FileContent
    {
        public String version;
        public Integer activeDialogId;
        public Chat.UiState uiState;
        public List<Dialog.Model> dialogs;
        
        FileContent() {
            this.dialogs = new ArrayList<Dialog.Model>();
        }
    }
}
