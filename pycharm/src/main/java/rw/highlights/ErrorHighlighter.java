package rw.highlights;

import com.intellij.codeInsight.daemon.impl.HintRenderer;
import com.intellij.diff.util.DiffGutterRenderer;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileWrapper;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import rw.icons.Icons;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

import static com.intellij.codeInsight.hint.HintUtil.ERROR_COLOR_KEY;
import static com.intellij.openapi.editor.colors.EditorColorsUtil.getGlobalOrDefaultColor;

public class ErrorHighlighter {
    Highlighter highlighter;
    VirtualFile file;
    int line;
    String msg;
    Project project;
    List<Inlay<? extends EditorCustomElementRenderer>> inlays;

    public ErrorHighlighter(Project project, @NotNull VirtualFile file, int line, String msg) {
        this.file = file;
        this.msg = msg;
        this.project = project;
        this.line = line;

        this.inlays = new ArrayList<>();

        this.highlighter = new Highlighter(this.project, file, line, getGlobalOrDefaultColor(ERROR_COLOR_KEY), 0, false);
    }

    synchronized public void show() {
        this.highlighter.show();

        Document document = ReadAction.compute(() -> FileDocumentManager.getInstance().getDocument(this.file));

        int line = this.line - 1;
        int startOffset = document.getLineStartOffset(line);
        int endOffset = document.getLineEndOffset(line);
        String lineContent = document.getText(new TextRange(startOffset, endOffset));
        int lineStartOffset = StringUtils.indexOf(lineContent, StringUtils.stripStart(lineContent, null));


        for (Editor e : EditorFactory.getInstance().getEditors(document)) {
            InlayModel inlayModel = e.getInlayModel();

            ErrorRenderer renderer = new ErrorRenderer(e, this.msg);
            int offset = e.logicalPositionToOffset(new LogicalPosition(line, 0));
            Inlay<EditorCustomElementRenderer> inlay = inlayModel.addBlockElement(offset, true, false, 10000, renderer);
            this.inlays.add(inlay);

            LogicalPosition highlightPosition = new LogicalPosition(line, lineStartOffset);

            Point p = e.logicalPositionToXY(highlightPosition);
            if (!e.getScrollingModel().getVisibleArea().contains(p)) {
                Timer timer = new Timer(500, t -> {
                    e.getCaretModel().moveToLogicalPosition(new LogicalPosition(line, lineStartOffset));
                    e.getScrollingModel().scrollToCaret(ScrollType.CENTER);
                });
                timer.setRepeats(false); // execute the task only once
                timer.start();
            }
        }
    }

    synchronized public void hide() {
        this.highlighter.hide();
        for (Inlay<? extends EditorCustomElementRenderer> i : this.inlays) {
            i.dispose();
        }
    }
}
