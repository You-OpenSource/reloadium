package rw.ai.preferences;

import java.util.Objects;

public class AiPreferencesState
{
    public String accountName;
    public Boolean useYoucom;

    public AiPreferencesState() {
        this.accountName = "";
        this.useYoucom = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AiPreferencesState that = (AiPreferencesState) o;
        return Objects.equals(accountName, that.accountName) && Objects.equals(useYoucom, that.useYoucom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountName, useYoucom);
    }
}
