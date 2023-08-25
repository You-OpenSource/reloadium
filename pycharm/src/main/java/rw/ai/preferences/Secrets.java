// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.preferences;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.credentialStore.CredentialAttributes;

public class Secrets
{
    public static CredentialAttributes CHAT_GPT_CRED_ATTR;
    private static Secrets singleton;
    SecretsState state;
    
    private Secrets() {
        this.state = new SecretsState();
        final PasswordSafe passwordSafe = (PasswordSafe)ApplicationManager.getApplication().getService((Class)PasswordSafe.class);
        this.state.openAiApiKey = passwordSafe.getPassword(Secrets.CHAT_GPT_CRED_ATTR);
    }
    
    public static Secrets get() {
        if (Secrets.singleton == null) {
            Secrets.singleton = new Secrets();
        }
        return Secrets.singleton;
    }
    
    public SecretsState getState() {
        return this.state;
    }
    
    public void loadState(final SecretsState state) {
        this.state = state;
        final PasswordSafe passwordSafe = (PasswordSafe)ApplicationManager.getApplication().getService((Class)PasswordSafe.class);
        passwordSafe.setPassword(Secrets.CHAT_GPT_CRED_ATTR, this.state.openAiApiKey);
    }
    
    static {
        Secrets.CHAT_GPT_CRED_ATTR = new CredentialAttributes("reloadium", "chatGptApiKey");
    }
}
