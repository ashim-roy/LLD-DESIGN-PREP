package com.tekion.dealer_rate_limiter.model;

public record RateLimitPolicy(
        int capacity,
        double refillRate
) {
    public RateLimitPolicy(int capacity, double refillRate) {
        if (capacity <= 0 || refillRate <= 0) {
            throw new IllegalArgumentException("Invalid policy");
        }

        this.capacity = capacity;
        this.refillRate = refillRate;
    }
}

/*
With a record, Java generates the constructor, accessors, equals(), hashCode() and toString() for you.
What the record gives you
Constructor
new RateLimitPolicy(10, 2.0)


Accessors
policy.capacity() and policy.refillRate()


Value-based equality
Two policies with the same capacity and refill rate compare equal.


Immutability
The record components cannot be reassigned after construction.

Here's why each check matters:
Condition	Reason
capacity <= 0	A bucket needs positive capacity.
!Double.isFinite(refillRate)	Rejects NaN, positive infinity and negative infinity.
refillRate <= 0	A zero or negative refill rate is not valid for this implementation.
Why not just check refillRate <= 0? Because NaN is unusual: comparisons such as NaN <= 0 are false. The isFinite() check catches it.

compact constructor:
public record RateLimitPolicy(
        int capacity,
        double refillRate
) {
    public RateLimitPolicy {
        if (capacity <= 0 || !Double.isFinite(refillRate)
                || refillRate <= 0) {
            throw new IllegalArgumentException("Invalid policy");
        }
    }
}

Why does the model contain validation code?
This is an important design concept: the model protects its own validity.
Suppose someone writes:
RateLimitPolicy policy = new RateLimitPolicy(-5, -2.0);


Without validation, the application could create a policy with an impossible bucket capacity and a negative refill rate.
With your constructor, that invalid object is never successfully created.
This is sometimes called fail-fast validation: reject invalid data at the point where the object is constructed, rather than waiting for the rate limiter to malfunction later.
We put this validation in RateLimitPolicy because the rules describe what makes a valid policy, not how the entire application should handle HTTP requests.
One distinction worth remembering: a record is only shallowly immutable. Its component references cannot be reassigned, but a mutable object stored in a record component could still be modified internally. Here, both components are primitive values, so that concern doesn't apply.
5. What should you remember for the interview?
You only need three ideas:
1. Record: a concise way to declare an immutable data carrier with generated accessors and value-based methods.
2. Compact constructor: lets you validate incoming record values without writing the full constructor boilerplate.
3. Validation in the model: prevents invalid domain objects from being created.

 */