// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.ui;

import javax.swing.UIManager;
import java.awt.Color;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.ui.ColorUtil;
import com.intellij.openapi.editor.colors.EditorColorsManager;

public class UiUtils
{
    public static boolean isDark() {
        final EditorColorsScheme current = EditorColorsManager.getInstance().getGlobalScheme();
        return ColorUtil.isDark(current.getDefaultBackground());
    }
    
    private static int addBrightness(final int color, final int delta) {
        int ret = color + delta;
        if (ret < 0) {
            ret = 0;
        }
        if (ret > 255) {
            ret = 255;
        }
        return ret;
    }
    
    public static Color addBrightness(final Color color, final int delta) {
        final int red = addBrightness(color.getRed(), delta);
        final int green = addBrightness(color.getGreen(), delta);
        final int blue = addBrightness(color.getBlue(), delta);
        return new Color(red, green, blue);
    }
    
    public static Color getDefaultEditorBackground() {
        final EditorColorsManager editorColorsManager = EditorColorsManager.getInstance();
        final EditorColorsScheme editorColorsScheme = editorColorsManager.getGlobalScheme();
        final Color ret = editorColorsScheme.getDefaultBackground();
        return ret;
    }
    
    public static Color getDefaultEditorForeground() {
        final EditorColorsManager editorColorsManager = EditorColorsManager.getInstance();
        final EditorColorsScheme editorColorsScheme = editorColorsManager.getGlobalScheme();
        final Color ret = editorColorsScheme.getDefaultForeground();
        return ret;
    }
    
    public static Color addBrightnessThemeAware(final Color color, int delta) {
        final Color defaultBackgroundColor = UIManager.getColor("Panel.background");
        if (!ColorUtil.isDark(defaultBackgroundColor)) {
            delta = -delta;
        }
        return addBrightness(color, delta);
    }
    
    public static boolean isHighContrast() {
        final EditorColorsManager editorColorsManager = EditorColorsManager.getInstance();
        final EditorColorsScheme colorsScheme = editorColorsManager.getGlobalScheme();
        final boolean ret = colorsScheme.getDisplayName().equalsIgnoreCase("high contrast");
        return ret;
    }
}
