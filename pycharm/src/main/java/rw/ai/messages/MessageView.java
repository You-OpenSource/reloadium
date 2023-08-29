package rw.ai.messages;

import javax.swing.UIManager;
import rw.ai.ui.UiUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.awt.Dimension;
import javax.swing.Icon;
import java.util.HashMap;
import javax.swing.Box;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.Container;
import javax.swing.BoxLayout;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import java.awt.Insets;
import javax.swing.border.EmptyBorder;
import com.intellij.util.ui.JBUI;
import com.intellij.ui.Gray;
import org.jetbrains.annotations.NotNull;
import java.util.Map;
import javax.swing.JLabel;
import java.awt.Color;
import com.intellij.openapi.project.Project;
import javax.swing.JPanel;

public abstract class MessageView extends JPanel
{
    private static final int WIDTH = 500;
    protected JPanel gutter;
    protected JPanel msgContainer;
    protected Project project;
    Color textColor;
    JLabel iconLabel;
    Map<Integer, MessagePart> parts;
    boolean active;
    String lastCodeExtension;
    MessagePart previousPart;
    Color activeColor;
    Color droppedColor;
    
    public MessageView(@NotNull final Project project) {
        if (project == null) {
            $$$reportNull$$$0(0);
        }
        this.project = project;
        this.textColor = (Color)Gray._182;
        this.lastCodeExtension = null;
        this.previousPart = null;
        final Icon icon = this.getIcon();
        (this.iconLabel = new JLabel(icon)).setBorder(BorderFactory.createCompoundBorder(new EmptyBorder((Insets)JBUI.insets(10)), this.iconLabel.getBorder()));
        (this.gutter = new JPanel()).setLayout(new BoxLayout(this.gutter, 1));
        this.gutter.add(this.iconLabel);
        this.gutter.add(Box.createVerticalGlue());
        this.gutter.setOpaque(false);
        (this.msgContainer = new JPanel()).setOpaque(false);
        this.msgContainer.setLayout(new BoxLayout(this.msgContainer, 1));
        this.msgContainer.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        this.setLayout(new BoxLayout(this, 0));
        this.add(this.gutter);
        this.add(this.msgContainer);
        this.parts = new HashMap<Integer, MessagePart>();
        this.setOpaque(true);
        this.active = true;
        this.activeColor = this.getActiveColor();
        this.droppedColor = this.getDroppedColor();
    }
    
    @Override
    public Dimension getMaximumSize() {
        int totalHeight = 0;
        for (final Component child : this.getComponents()) {
            totalHeight += child.getPreferredSize().height;
        }
        return new Dimension((int)super.getMaximumSize().getWidth(), totalHeight);
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, (int)super.getPreferredSize().getHeight());
    }
    
    protected abstract Icon getIcon();
    
    @JsonProperty
    public String getType() {
        return this.getClass().getName();
    }
    
    @Override
    public Color getBackground() {
        if (this.active) {
            return this.activeColor;
        }
        return this.droppedColor;
    }
    
    public void setActive(final boolean active) {
        this.active = active;
        this.repaint();
    }
    
    public Project getProject() {
        return this.project;
    }
    
    protected Color getTextColor() {
        return this.textColor;
    }
    
    public void setTextColor(final Color color) {
        this.textColor = color;
    }
    
    public JPanel getMsgContainer() {
        return this.msgContainer;
    }
    
    protected Color getDroppedColor() {
        if (UiUtils.isHighContrast()) {
            return UiUtils.addBrightness(this.activeColor, 15);
        }
        return UiUtils.addBrightness(this.activeColor, -30);
    }
    
    protected abstract int getColorOffset();
    
    protected Color getActiveColor() {
        final Color defaultBackgroundColor = UIManager.getColor("Panel.background");
        final int offset = this.getColorOffset();
        return UiUtils.addBrightnessThemeAware(defaultBackgroundColor, offset);
    }
    
    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "project", "rw/ai/messages/MessageView", "<init>"));
    }
}
