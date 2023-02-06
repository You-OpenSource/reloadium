package rw.session.events;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileWrapper;
import rw.service.CodeCompletionService;

abstract public class FileError extends FileEvent {
    public static final String ID = "FileError";

    private Integer line;
    private String msg;

    @Override
    public void handle() {
        this.handler.getErrorHighlightManager().clearAll();
        this.handler.getErrorHighlightManager().add(this.getLocalPath(), this.line, this.msg);
        VirtualFile file = new VirtualFileWrapper(this.getLocalPath()).getVirtualFile(false);
        Document document = ReadAction.compute(() -> FileDocumentManager.getInstance().getDocument(file));
        int lineNumber = getLine() - 1;
        String errorLine = document.getText(TextRange.create(document.getLineStartOffset(lineNumber), document.getLineEndOffset(lineNumber)));
        String res = CodeCompletionService.INSTANCE.predictFix(errorLine).blockingFirst();
        this.handler.getSolutionHighlightManager().clearAll();
        this.handler.getSolutionHighlightManager().add(this.getLocalPath(), this.line, this.msg, res);
    }

    public Integer getLine() {
        return line;
    }
    public String getMsg() {
        return msg;
    }
}
