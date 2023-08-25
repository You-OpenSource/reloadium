package rw.session.events;

import com.google.gson.annotations.SerializedName;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;
import rw.dialogs.DialogFactory;
import rw.service.CodeCompletionService;

public class ThreadErrorEvent extends FileError {
    public static final String ID = "ThreadError";
    private static final Logger LOGGER = Logger.getInstance(ThreadErrorEvent.class);
    @SerializedName("thread_id")
    final private String threadId;

    @SerializedName("frame_id")
    final private Long frameId;

    @Override
    public void handle() {
        LOGGER.info("Handling ThreadErrorEvent");
        this.handler.getThreadErrorManager().onThreadError(this);
        DialogFactory.get().showFirstThreadErrorDialog(this.handler.getProject());
        // todo still broken
        VirtualFile file = new VirtualFileWrapper(this.getPath()).getVirtualFile(false);
        Document document = ReadAction.compute(() -> FileDocumentManager.getInstance().getDocument(file));
        int lineNumber = getLine() - 1;
        String errorLine = document.getText(TextRange.create(document.getLineStartOffset(lineNumber), document.getLineEndOffset(lineNumber)));
        String res = CodeCompletionService.INSTANCE.predictFix(errorLine).blockingFirst();
        this.handler.getSolutionHighlightManager().clearAll();
        this.handler.getSolutionHighlightManager().add(this.getPath(), this.getLine(), this.getMsg(), res);
    }

    @VisibleForTesting
    public ThreadErrorEvent(@NotNull String path, @NotNull VirtualFile file, Integer line, String msg, String threadId, Long frameId) {
        super(path, file, line, msg);
        this.threadId = threadId;
        this.frameId = frameId;
    }

    public String getThreadId() {
        return this.threadId;
    }

    public Long getFramenId() {
        return this.frameId;
    }
}
