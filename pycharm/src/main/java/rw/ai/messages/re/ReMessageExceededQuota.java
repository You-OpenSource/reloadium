package rw.ai.messages.re;

import rw.ai.messages.MessageType;
import rw.ai.context.Context;
import rw.ai.lang.AiBundle;
import rw.ai.messages.MessageView;
import rw.ai.messages.MessageModel;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.project.Project;

public class ReMessageExceededQuota extends ReMessage
{
    public ReMessageExceededQuota(@NotNull final Project project, @NotNull final Model model, @NotNull final View view) {
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
        final String content = AiBundle.message("ai.remessage.quota.exceeded", new Object[0]);
        this.setRawContent(content);
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
        args[1] = "rw/ai/messages/re/ReMessageExceededQuota";
        args[2] = "<init>";
        throw new IllegalArgumentException(String.format(format, args));
    }
    
    public static class View extends ReMessage.View
    {
        public View(@NotNull final Project project) {
            super(project);
            if (project == null) {
                $$$reportNull$$$0(0);
            }
        }
        
        private static /* synthetic */ void $$$reportNull$$$0(final int n) {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "project", "rw/ai/messages/re/ReMessageExceededQuota$View", "<init>"));
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
            return MessageType.RE_EXCEEDED_QUOTA;
        }
        
        private static /* synthetic */ void $$$reportNull$$$0(final int n) {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "context", "rw/ai/messages/re/ReMessageExceededQuota$Model", "<init>"));
        }
    }
}
