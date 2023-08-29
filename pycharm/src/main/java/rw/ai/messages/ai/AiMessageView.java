package rw.ai.messages.ai;

import rw.ai.ui.UiUtils;
import java.awt.Component;
import rw.ai.ui.AiIcons;
import javax.swing.Icon;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.project.Project;
import java.awt.Color;
import rw.ai.messages.MessageView;

public class AiMessageView extends MessageView
{
    final Color ERROR_COLOR;
    
    public AiMessageView(@NotNull final Project project) {
        super(project);
        if (project == null) {
            $$$reportNull$$$0(0);
        }

        this.ERROR_COLOR = new Color(255, 50, 50);
    }
    
    @Override
    protected Icon getIcon() {
        return AiIcons.ChatGptBigSquare;
    }
    
    public void onComplete() {
        for (final Component c : this.msgContainer.getComponents()) {
            if (c instanceof AiMarkdown.View) {
                ((AiMarkdown.View)c).disableBlinking();
            }
        }
    }
    
    public void makeErrored() {
        this.setTextColor(this.ERROR_COLOR);
    }
    
    @Override
    protected int getColorOffset() {
        if (UiUtils.isHighContrast()) {
            return 15;
        }
        return 10;
    }
    
    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "project", "rw/ai/messages/ai/AiMessageView", "<init>"));
    }
}
