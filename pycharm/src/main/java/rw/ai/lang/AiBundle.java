// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.lang;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.PropertyKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.NonNls;
import com.intellij.DynamicBundle;

public final class AiBundle extends DynamicBundle
{
    @NonNls
    public static final String BUNDLE = "messages.AiBundle";
    private static final AiBundle INSTANCE;
    
    private AiBundle() {
        super("messages.AiBundle");
    }
    
    @NotNull
    @Nls
    public static String message(@NotNull @PropertyKey(resourceBundle = "messages.AiBundle") final String key, final Object... params) {
        if (key == null) {
            $$$reportNull$$$0(0);
        }
        if (params == null) {
            $$$reportNull$$$0(1);
        }
        if (AiBundle.INSTANCE.containsKey(key)) {
            final String message = AiBundle.INSTANCE.getMessage(key, params);
            if (message == null) {
                $$$reportNull$$$0(2);
            }
            return message;
        }
        final AiBundle instance = AiBundle.INSTANCE;
        return message(key, params);
    }
    
    static {
        INSTANCE = new AiBundle();
    }
    
    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        String format = null;
        switch (n) {
            default: {
                format = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                break;
            }
            case 2: {
                format = "@NotNull method %s.%s must not return null";
                break;
            }
        }
        int n2 = 0;
        switch (n) {
            default: {
                n2 = 3;
                break;
            }
            case 2: {
                n2 = 2;
                break;
            }
        }
        final Object[] args = new Object[n2];
        switch (n) {
            default: {
                args[0] = "key";
                break;
            }
            case 1: {
                args[0] = "params";
                break;
            }
            case 2: {
                args[0] = "rw/ai/lang/AiBundle";
                break;
            }
        }
        switch (n) {
            default: {
                args[1] = "rw/ai/lang/AiBundle";
                break;
            }
            case 2: {
                args[1] = "message";
                break;
            }
        }
        switch (n) {
            default: {
                args[2] = "message";
                break;
            }
            case 2: {
                break;
            }
        }
        final String format2 = String.format(format, args);
        RuntimeException ex = null;
        switch (n) {
            default: {
                ex = new IllegalArgumentException(format2);
                break;
            }
            case 2: {
                ex = new IllegalStateException(format2);
                break;
            }
        }
        throw ex;
    }
}
