package rw.highlights;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.VisibleForTesting;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SolutionHighlightManager {
    Map<File, List<SolutionHighlighter>> all;

    @VisibleForTesting
    public static SolutionHighlightManager singleton;

    Project project;

    @VisibleForTesting
    public SolutionHighlightManager(Project project) {
        this.all = new HashMap<>();
        this.project = project;
    }

    public void add(File file, int line, String msg, String fixSuggestion) {
        if (!this.all.containsKey(file)) {
            this.all.put(file, new ArrayList<>());
        }

        if (line < 0) {
            return;
        }

        SolutionHighlighter highlighter = new SolutionHighlighter(this.project, file, line, msg, fixSuggestion);
        this.all.get(file).add(highlighter);

        highlighter.show();
    }

    public void clearFile(File file) {
        List<SolutionHighlighter> highlighters = this.all.get(file);
        if (highlighters == null) {
            return;
        }

        for (SolutionHighlighter h : highlighters) {
            h.hide();
        }
        highlighters.clear();
    }

    public void clearAll() {
        for (File f : this.all.keySet()) {
            this.clearFile(f);
        }
        this.all.clear();
    }

    public void activate() {
        for (List<SolutionHighlighter> hs : this.all.values()) {
            for (SolutionHighlighter h : hs) {
                h.show();
            }
        }
    }

    public void deactivate() {
        for (List<SolutionHighlighter> hs : this.all.values()) {
            for (SolutionHighlighter h : hs) {
                h.hide();
            }
        }
    }
}
