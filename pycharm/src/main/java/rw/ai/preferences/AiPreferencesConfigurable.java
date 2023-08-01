// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.preferences;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import javax.swing.JComponent;
import com.intellij.openapi.util.NlsContexts;
import org.apache.commons.lang.StringUtils;
import java.util.Objects;
import rw.consts.Const;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.options.Configurable;

public class AiPreferencesConfigurable implements Configurable, SearchableConfigurable, Configurable.NoScroll
{
    PreferencesForm form;
    
    AiPreferencesConfigurable() {
        this.form = new PreferencesForm();
    }
    
    @NlsContexts.ConfigurableName
    public String getDisplayName() {
        Objects.requireNonNull(Const.get());
        return "reloadium-you";
    }
    
    @Nullable
    public JComponent createComponent() {
        return this.form.getMainPanel();
    }
    
    public boolean isModified() {
        return !this.form.getState().equals(AiPreferences.get().getState()) || !this.form.getSecretsState().equals(Secrets.get().getState());
    }
    
    public void apply() {
        this.form.onApply();
        Secrets.get().loadState(this.form.getSecretsState());
    }
    
    public void reset() {
        this.form.setState(AiPreferences.get().getState());
        this.form.setSecretsState(Secrets.get().getState());
    }
    
    @NotNull
    @NonNls
    public String getId() {
        Objects.requireNonNull(Const.get());
        return "reloadium";
    }
}
