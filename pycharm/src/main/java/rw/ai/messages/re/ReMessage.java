// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.messages.re;

import rw.icons.Icons;
import javax.swing.Icon;
import org.jetbrains.annotations.Nullable;
import rw.ai.messages.MessageType;
import rw.ai.context.Context;
import rw.ai.messages.MessageView;
import rw.ai.messages.MessageModel;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.project.Project;
import rw.ai.messages.Message;

public class ReMessage extends Message
{
    public ReMessage(@NotNull final Project project, @NotNull final MessageModel model, @NotNull final MessageView view) {
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
    }
    
    @Override
    public View getView() {
        return (View)super.getView();
    }
    
    @Override
    public MessageModel getModel() {
        return super.getModel();
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
        args[1] = "rw/ai/messages/re/ReMessage";
        args[2] = "<init>";
        throw new IllegalArgumentException(String.format(format, args));
    }
    
    public static class Model extends MessageModel
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
        public String getAiContent() {
            return "";
        }
        
        @Override
        public MessageType getType() {
            return MessageType.RE;
        }
        
        @Nullable
        @Override
        public String getRole() {
            return null;
        }
        
        private static /* synthetic */ void $$$reportNull$$$0(final int n) {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "context", "rw/ai/messages/re/ReMessage$Model", "<init>"));
        }
    }
    
    public static class View extends MessageView
    {
        public View(@NotNull final Project project) {
            super(project);
            if (project == null) {
                $$$reportNull$$$0(0);
            }
        }
        
        @Override
        protected Icon getIcon() {
            return Icons.ProductBig;
        }
        
        @Override
        protected int getColorOffset() {
            return 20;
        }
        
        private static /* synthetic */ void $$$reportNull$$$0(final int n) {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", "project", "rw/ai/messages/re/ReMessage$View", "<init>"));
        }
    }
}
