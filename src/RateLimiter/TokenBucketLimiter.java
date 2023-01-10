package RateLimiter;

public class TokenBucketLimiter implements RateLimiter {

    private final long capacity;
    private final long windowTimeInSeconds;
    private final long refillCountPerSecond;

    private long lastRefillTimeStamp;
    private long availableTokens;

    public TokenBucketLimiter(long capacity, long windowTimeInSeconds){
        this.capacity = capacity;
        this.windowTimeInSeconds = windowTimeInSeconds;
        this.refillCountPerSecond = capacity / windowTimeInSeconds;

        this.lastRefillTimeStamp = System.currentTimeMillis();
        this.availableTokens = 0;
    }

    @Override
    public boolean canProceed() {
        tryRefill();

        if (availableTokens > 0) {
            availableTokens--;
            return true;
        }
        return false;
    }

    private void tryRefill(){
        long now = System.currentTimeMillis();
        long elapsedTimeMs = now - lastRefillTimeStamp;

        // refill tokens for this duration
        long tokensToBeAdded = (elapsedTimeMs / 1000) * refillCountPerSecond;
        if (tokensToBeAdded > 0) {
            availableTokens = Math.min(capacity, availableTokens + tokensToBeAdded);
            this.lastRefillTimeStamp = now;
        }
    }

    public long getAvailableTokens(){
        return this.availableTokens;
    }

}
