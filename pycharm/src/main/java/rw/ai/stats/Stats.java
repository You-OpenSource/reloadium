// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.stats;

import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.PersistentStateComponent;

@State(name = "AiStats", storages = { @Storage("aiReloadium.xml") })
public class Stats implements PersistentStateComponent<StatsState>
{
    StatsState state;
    
    public Stats() {
        this.state = new StatsState();
    }
    
    public static Stats get() {
        return (Stats)ApplicationManager.getApplication().getService((Class)Stats.class);
    }
    
    @NotNull
    public StatsState getState() {
        final StatsState state = this.state;
        if (state == null) {
            $$$reportNull$$$0(0);
        }
        return state;
    }
    
    public void loadState(@NotNull final StatsState state) {
        if (state == null) {
            $$$reportNull$$$0(1);
        }
        XmlSerializerUtil.copyBean((Object)state, (Object)this.state);
    }
    
    public void onNewMessage() {
        final StatsState state = this.state;
        ++state.messagesN;
        this.loadState(this.state);
    }
    
    private static /* synthetic */ void $$$reportNull$$$0(final int n) {
        String format = null;
        switch (n) {
            default: {
                format = "@NotNull method %s.%s must not return null";
                break;
            }
            case 1: {
                format = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
                break;
            }
        }
        int n2 = 0;
        switch (n) {
            default: {
                n2 = 2;
                break;
            }
            case 1: {
                n2 = 3;
                break;
            }
        }
        final Object[] args = new Object[n2];
        switch (n) {
            default: {
                args[0] = "rw/ai/stats/Stats";
                break;
            }
            case 1: {
                args[0] = "state";
                break;
            }
        }
        switch (n) {
            default: {
                args[1] = "getState";
                break;
            }
            case 1: {
                args[1] = "rw/ai/stats/Stats";
                break;
            }
        }
        switch (n) {
            case 1: {
                args[2] = "loadState";
                break;
            }
        }
        final String format2 = String.format(format, args);
        RuntimeException ex = null;
        switch (n) {
            default: {
                ex = new IllegalStateException(format2);
                break;
            }
            case 1: {
                ex = new IllegalArgumentException(format2);
                break;
            }
        }
        throw ex;
    }
}
