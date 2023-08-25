// 
// Decompiled by Procyon v0.5.36
// 

package rw.ai.preferences;

public class AiPreferencesState
{
    public String accountName;
    
    public AiPreferencesState() {
        this.accountName = "";
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final AiPreferencesState that = (AiPreferencesState)o;
        final boolean ret = this.accountName.equals(that.accountName);
        return ret;
    }
}
