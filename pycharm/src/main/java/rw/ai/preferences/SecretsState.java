package rw.ai.preferences;

public class SecretsState
{
    public String openAiApiKey;
    
    public SecretsState() {
        this.openAiApiKey = "";
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final SecretsState that = (SecretsState)o;
        final boolean ret = this.openAiApiKey.equals(that.openAiApiKey);
        return ret;
    }
}
