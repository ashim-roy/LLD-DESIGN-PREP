package com.tekion.dealer_rate_limiter.service;

import com.tekion.dealer_rate_limiter.model.RateLimitPolicy;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

class RateLimiterTest {

    @Test
    void allowsRequestsUntilBucketIsEmpty() {
        RateLimiter limiter =
                new RateLimiter(new RateLimitPolicy(2, 0.001));

        assertTrue(limiter.allowRequest("dealer-1"));
        assertTrue(limiter.allowRequest("dealer-1"));
        assertFalse(limiter.allowRequest("dealer-1"));
    }

    @Test
    void dealersHaveIndependentBuckets() {
        RateLimiter limiter =
                new RateLimiter(new RateLimitPolicy(1, 0.001));

        assertTrue(limiter.allowRequest("dealer-A"));
        assertFalse(limiter.allowRequest("dealer-A"));

        assertTrue(limiter.allowRequest("dealer-B"));
    }

    @Test
    void concurrentRequestsCannotExceedCapacity() throws Exception {
        int capacity = 10;
        int totalRequests = 50;
        int threadCount = 10;

        RateLimiter limiter = new RateLimiter(
                new RateLimitPolicy(capacity, 0.000001)
        );

        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        CountDownLatch ready = new CountDownLatch(threadCount);
        CountDownLatch start = new CountDownLatch(1);

        List<Future<Boolean>> results = new ArrayList<>();

        try {
            for (int i = 0; i < totalRequests; i++) {
                results.add(pool.submit(() -> {
                    ready.countDown();
                    start.await();
                    return limiter.allowRequest("dealer-A");
                }));
            }

            // Wait until all worker threads are ready.
            ready.await();

            // Release the workers together.
            start.countDown();

            int allowed = 0;

            for (Future<Boolean> result : results) {
                if (result.get()) {
                    allowed++;
                }
            }

            assertEquals(10, allowed);
        } finally {
            pool.shutdownNow();
        }
    }
}
