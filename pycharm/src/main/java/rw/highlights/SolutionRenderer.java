package rw.highlights;

import com.intellij.diff.util.DiffGutterRenderer;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.util.TextRange;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rw.icons.Icons;

import java.awt.*;

public class SolutionRenderer implements EditorCustomElementRenderer {
    String solutionSuggestion;
    private final int offset;

    SolutionRenderer(String fixMessage, int offset) {
        this.solutionSuggestion = fixMessage;
        this.offset = offset;
    }

    @Override
    public int calcWidthInPixels(@NotNull Inlay inlay) {
        return 100;
    }

    @Override
    public int calcHeightInPixels(@NotNull Inlay inlay) {
        return inlay.getEditor().getLineHeight();
    }

    @Override
    public @Nullable GutterIconRenderer calcGutterIconRenderer(@NotNull Inlay inlay) {
        return new DiffGutterRenderer(Icons.ProductIcon, "FIX") {
            @Override
            protected void handleMouseClick() {
                Editor editor = inlay.getEditor();
                Document document = editor.getDocument();
                int line = inlay.getVisualPosition().line;
                int start = document.getLineStartOffset(line);
                int end = document.getLineEndOffset(line);
                document.deleteString(start, end);
                document.insertString(start, solutionSuggestion);
            }
        };
    }

    @Override
    public void paint(@NotNull Inlay inlay, @NotNull Graphics g, @NotNull Rectangle targetRegion, @NotNull TextAttributes textAttributes) {
        Editor editor = inlay.getEditor();
        Document document = editor.getDocument();

        int lineNumber = document.getLineNumber(inlay.getOffset());

        int offsetStart = document.getLineStartOffset(lineNumber);
        int offsetEnd = document.getLineEndOffset(lineNumber);

        String line = document.getText(new TextRange(offsetStart, offsetEnd));
        String indentation = line.substring(0, line.length() - line.stripLeading().length());

        Color textColor = JBColor.GREEN;
        g.setColor(textColor);

        EditorColorsScheme colorsScheme = editor.getColorsScheme();

        Font defaultFont = UIUtil.getFontWithFallback(colorsScheme.getEditorFontName(), Font.PLAIN, colorsScheme.getEditorFontSize());
        FontMetrics metrics = g.getFontMetrics(defaultFont);

        int fontSize = colorsScheme.getEditorFontSize() - 1;
        if (fontSize < 11) {
            fontSize = 11;
        }

        Font font = UIUtil.getFontWithFallback(colorsScheme.getEditorFontName(), Font.PLAIN, fontSize);
        g.setFont(font);

        Point p = targetRegion.getLocation();

        int currentX = p.x + metrics.stringWidth(indentation);
        int currentY = p.y;

        g.drawString(this.solutionSuggestion, currentX, currentY + editor.getAscent() + 3);


    }
}
