package rw.highlights;

import com.intellij.codeInsight.daemon.impl.HintRenderer;
import com.intellij.diff.util.DiffGutterRenderer;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileWrapper;
import rw.icons.Icons;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.intellij.codeInsight.hint.HintUtil.ERROR_COLOR_KEY;
import static com.intellij.openapi.editor.colors.EditorColorsUtil.getGlobalOrDefaultColor;

public class ErrorHighlighter {
    Highlighter highlighter;
    File file;
    int line;
    String msg;
    Project project;
    List<Inlay<? extends EditorCustomElementRenderer>> inlays;
    String fixSuggestion;

    ErrorHighlighter(Project project, File file, int line, String msg, String fixSuggestion) {
        this.file = file;
        this.msg = msg;
        this.project = project;
        this.line = line;
        this.fixSuggestion = fixSuggestion;

        this.inlays = new ArrayList<>();

        this.highlighter = new Highlighter(this.project, file, line, getGlobalOrDefaultColor(ERROR_COLOR_KEY), 0, false);
    }

    public void show() {
        this.highlighter.show();

        VirtualFile file = new VirtualFileWrapper(this.file).getVirtualFile(false);
        assert file != null;
        Document document = ReadAction.compute(() -> FileDocumentManager.getInstance().getDocument(file));

        for (Editor e : EditorFactory.getInstance().getEditors(document)) {
            InlayModel inlayModel = e.getInlayModel();

            ErrorRenderer renderer = new ErrorRenderer(this.msg, this.fixSuggestion);
            SolutionRenderer solutionRenderer = new SolutionRenderer(this.fixSuggestion, this.line);
            ApplicationManager.getApplication().invokeLater(() -> {
                int offset = e.logicalPositionToOffset(new LogicalPosition(this.line-1, 0));
                Inlay<EditorCustomElementRenderer> inlay = inlayModel.addBlockElement(offset, true, false, 100, renderer);
                Inlay<EditorCustomElementRenderer> solution = inlayModel.addBlockElement(offset + 1, true, false, 100, solutionRenderer);
                this.inlays.add(inlay);
                this.inlays.add(solution);
            });
        }
    }

    public void hide() {
        this.highlighter.hide();

        ApplicationManager.getApplication().invokeLater(() -> {
            for (Inlay<? extends EditorCustomElementRenderer> i : this.inlays) {
                i.dispose();
            }
        });
    }
}
