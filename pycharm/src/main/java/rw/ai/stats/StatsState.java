package rw.ai.stats;

public class StatsState
{
    public Integer messagesN;
    
    public StatsState() {
        this.messagesN = 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final StatsState that = (StatsState)o;
        final boolean ret = this.messagesN.equals(that.messagesN);
        return ret;
    }
}
