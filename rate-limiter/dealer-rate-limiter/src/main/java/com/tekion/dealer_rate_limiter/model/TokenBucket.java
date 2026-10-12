package com.tekion.dealer_rate_limiter.model;


public class TokenBucket {

    private final int capacity;
    private final double refillRate;

    private double tokens;
    private long lastRefillTime;

    public TokenBucket(RateLimitPolicy policy) {
        this.capacity = policy.capacity();
        this.refillRate = policy.refillRate();
        this.tokens = capacity;
        this.lastRefillTime = System.currentTimeMillis();
    }

    public synchronized boolean allowRequest() {

        // Step 1: Add tokens based on the time elapsed since the last refill
        refill();

        // Step 2: If we have less than 1 token, reject the request
        if (tokens < 1) {
            return false;
        }

        // Step 3: Consume 1 token because we are allowing this request
        tokens--;

        // Step 4: Request is allowed
        return true;
    }

    private void refill() {
        long now = System.currentTimeMillis();

        // Convert milliseconds to seconds
        double elapsed = (now - lastRefillTime) / 1000.0;

        // Add tokens based on elapsed time
        tokens = Math.min(capacity, tokens + elapsed * refillRate);

        lastRefillTime = now;
    }
}

/*
Your refill() method does not run automatically every second. It runs whenever allowRequest() calls it, which happens when a request arrives.
Let's understand with an example
Suppose your policy is:
capacity = 5;
refillRate = 2; // 2 tokens per second


Initially, the bucket has 5 tokens.
Time
Tokens remaining


At 10:00:00 — Initial state5

At 10:00:01 — Request arrives4

At 10:00:04 — Next request arrives4


Why are there 4 tokens at 10:00:04?
1. The previous request consumed one token, leaving 4.
2. Three seconds passed, so refill() calculates \(3 \times 2 = 6\) new tokens.
3. The bucket cannot exceed its capacity of 5, so it stays at 5.
4. The new request consumes one token, leaving 4.
The important line
tokens = Math.min(capacity, tokens + elapsed * refillRate);


This line does two things:
- Adds tokens based on how much time has elapsed since the previous refill.
- Ensures the token count never exceeds capacity.
One key detail: The bucket doesn't need a background thread or timer. It calculates the refill lazily, only when a request arrives. Even if no requests arrive for a long time, the next request will calculate the elapsed time and refill up to capacity.
git
 */