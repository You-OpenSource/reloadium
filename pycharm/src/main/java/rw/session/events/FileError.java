package rw.session.events;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileWrapper;
import rw.service.CodeCompletionService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;

abstract public class FileError extends FileEvent {
    public static final String ID = "FileError";

    final private Integer line;
    final private String msg;

    @Override
    public void handle() {
        this.handler.getErrorHighlightManager().add(this.getFile(), this.line, this.msg);
    }

    @VisibleForTesting
    public FileError(@NotNull String path, @NotNull VirtualFile file, Integer line, String msg) {
        super(path, file);
        this.line = line;
        this.msg = msg;
//        VirtualFile file = new VirtualFileWrapper(this.getPath()).getVirtualFile(false);
        Document document = ReadAction.compute(() -> FileDocumentManager.getInstance().getDocument(file));
        int lineNumber = getLine() - 1;
        String errorLine = document.getText(TextRange.create(document.getLineStartOffset(lineNumber), document.getLineEndOffset(lineNumber)));
        String res = CodeCompletionService.INSTANCE.predictFix(errorLine).blockingFirst();
        this.handler.getSolutionHighlightManager().clearAll();
        this.handler.getSolutionHighlightManager().add(this.getPath(), this.line, this.msg, res);
    }

    public Integer getLine() {
        return line;
    }

    public String getMsg() {
        return msg;
    }
}
