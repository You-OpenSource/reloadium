package rw.ai.context;

import rw.ai.ui.UiUtils;
import javax.swing.JLabel;
import java.awt.Component;
import javax.swing.Box;
import java.awt.LayoutManager;
import java.awt.Container;
import javax.swing.BoxLayout;
import org.jetbrains.annotations.NotNull;
import javax.swing.JPanel;

public class ContextSeparator
{
    private final Context context;
    JPanel component;
    
    public ContextSeparator(@NotNull final Context context) {
        if (context == null) {
            $$$reportNull$$$0(0);
        }
        this.context = context;
        (this.component = new JPanel()).setLayout(new BoxLayout(this.component, 0));
        this.component.add(Box.createHorizontalGlue());
        final JLabel imageLabel = new JLabel(context.getIconSmall(this.component));
        imageLabel.setAlignmentY(0.5f);
        this.component.add(imageLabel);
        this.component.add(new JLabel(this.context.getSeparatorText()));
        this.component.add(Box.createHorizontalGlue());
        this.component.setOpaque(true);
        this.component.setBackground(UiUtils.addBrightnessThemeAware(this.component.getBackground(), -25));
    }
    
    public Context getContext() {
        return this.context;
    }
    
    public JPanel getComponent() {
        return this.component;
    }
    
    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "context", "rw/ai/context/ContextSeparator", "<init>"));
    }
}
