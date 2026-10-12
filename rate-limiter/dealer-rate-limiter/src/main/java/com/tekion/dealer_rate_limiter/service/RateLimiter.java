package com.tekion.dealer_rate_limiter.service;

import com.tekion.dealer_rate_limiter.model.RateLimitPolicy;
import com.tekion.dealer_rate_limiter.model.TokenBucket;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;



public class RateLimiter {

    private RateLimitPolicy policy;
    private final ConcurrentMap<String, TokenBucket> buckets = new ConcurrentHashMap<>();

    public RateLimiter(RateLimitPolicy policy) {
        this.policy = policy;
    }

    public synchronized boolean allowRequest(String dealerId) {

        // 1. Find the dealer's bucket
        TokenBucket bucket = buckets.get(dealerId);

        // 2. If the dealer is new, create a bucket
        if (bucket == null) {
            bucket = new TokenBucket(policy);
            buckets.put(dealerId, bucket);
        }

        // 3. Ask the bucket whether the request is allowed
        return bucket.allowRequest();
    }
}

//public class RateLimiter {
//    private final RateLimitPolicy policy;
//
//    private final ConcurrentMap<String, TokenBucket> buckets = new ConcurrentHashMap<>();
//
//    public RateLimiter(RateLimitPolicy policy) {
//        this.policy = policy;
//    }
//
//    public boolean allowRequest(String dealerId) {
//        if (dealerId == null || dealerId.isBlank()) {
//            throw new IllegalArgumentException("Dealer ID is required");
//        }
//
//        TokenBucket bucket = buckets.computeIfAbsent(dealerId, id -> new TokenBucket(policy));
//
//        return bucket.allowRequest();
//    }
//}
