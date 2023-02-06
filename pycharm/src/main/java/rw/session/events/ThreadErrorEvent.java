package rw.session.events;

import com.google.gson.annotations.SerializedName;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileWrapper;
import rw.dialogs.DialogFactory;
import rw.service.CodeCompletionService;

public class ThreadErrorEvent extends FileError {
    private static final Logger LOGGER = Logger.getInstance(ThreadErrorEvent.class);

    public static final String ID = "ThreadError";

    @SerializedName("thread_id")
    private String threadId;

    @SerializedName("frame_id")
    private Long frameId;

    @Override
    public void handle() {
        LOGGER.info("Handling ThreadErrorEvent");
        this.handler.getStack().onThreadError(this);
        DialogFactory.get().showFirstThreadErrorDialog(this.handler.getProject());
        // todo still broken
        VirtualFile file = new VirtualFileWrapper(this.getLocalPath()).getVirtualFile(false);
        Document document = ReadAction.compute(() -> FileDocumentManager.getInstance().getDocument(file));
        int lineNumber = getLine() - 1;
        String errorLine = document.getText(TextRange.create(document.getLineStartOffset(lineNumber), document.getLineEndOffset(lineNumber)));
        String res = CodeCompletionService.INSTANCE.predictFix(errorLine).blockingFirst();
        this.handler.getSolutionHighlightManager().clearAll();
        this.handler.getSolutionHighlightManager().add(this.getLocalPath(), this.getLine(), this.getMsg(), res);
    }

    public String getThreadId() {
        return this.threadId;
    }

    public Long getFramenId() {
        return this.frameId;
    }
}
