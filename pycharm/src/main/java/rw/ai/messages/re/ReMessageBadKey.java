package rw.ai.messages.re;

import rw.ai.messages.MessageType;
import rw.ai.context.Context;
import javax.swing.Box;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.Container;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import rw.ai.preferences.AiPreferencesConfigurable;
import com.intellij.openapi.options.ShowSettingsUtil;
import rw.ai.lang.AiBundle;
import rw.ai.messages.MessageView;
import rw.ai.messages.MessageModel;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.project.Project;

public class ReMessageBadKey extends ReMessage
{
    public ReMessageBadKey(@NotNull final Project project, @NotNull final Model model, @NotNull final View view) {
        super(project, model, view);
        if (project == null) {
            $$$reportNull$$$0(0);
        }
        if (model == null) {
            $$$reportNull$$$0(1);
        }
        if (view == null) {
            $$$reportNull$$$0(2);
        }
        this.setRawContent(AiBundle.message("ai.remessage.bad.api", new Object[0]));
        this.getView().addButton();
        this.getView().addButtonListener(e -> ShowSettingsUtil.getInstance().showSettingsDialog(this.getView().getProject(), (Class)AiPreferencesConfigurable.class));
    }
    
    @Override
    public View getView() {
        return (View)super.getView();
    }
    
    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        final String format = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        final Object[] args = new Object[3];
        switch (n) {
            default: {
                args[0] = "project";
                break;
            }
            case 1: {
                args[0] = "model";
                break;
            }
            case 2: {
                args[0] = "view";
                break;
            }
        }
        args[1] = "rw/ai/messages/re/ReMessageBadKey";
        args[2] = "<init>";
        throw new IllegalArgumentException(String.format(format, args));
    }
    
    public static class View extends ReMessage.View
    {
        JButton aiPreferences;
        
        public View(@NotNull final Project project) {
            super(project);
            if (project == null) {
                $$$reportNull$$$0(0);
            }
            (this.aiPreferences = new JButton("AI Preferences")).setOpaque(false);
        }
        
        void addButtonListener(final ActionListener actionListener) {
            this.aiPreferences.addActionListener(actionListener);
        }
        
        public void addButton() {
            final JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, 0));
            panel.add(this.aiPreferences);
            panel.add(Box.createHorizontalGlue());
            panel.setOpaque(false);
            this.msgContainer.add(panel);
        }
        
        private static /* synthetic */ void $$$reportNull$$$0(final int n) {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "project", "rw/ai/messages/re/ReMessageBadKey$View", "<init>"));
        }
    }
    
    public static class Model extends ReMessage.Model
    {
        public Model(@NotNull final Context context) {
            super(context);
            if (context == null) {
                $$$reportNull$$$0(0);
            }
        }
        
        public Model() {
        }
        
        @Override
        public MessageType getType() {
            return MessageType.RE_BAD_KEY;
        }
        
        private static /* synthetic */ void $$$reportNull$$$0(final int n) {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "context", "rw/ai/messages/re/ReMessageBadKey$Model", "<init>"));
        }
    }
}
