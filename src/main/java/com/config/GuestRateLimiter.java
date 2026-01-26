package com.config;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class GuestRateLimiter {

    private final StringRedisTemplate redisTemplate;

    private final int MAX_REQUESTS = 5;
    private final Duration WINDOW = Duration.ofMinutes(1);

    private static final String PREFIX = "guest:ip:";

    public GuestRateLimiter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Checks if the IP is allowed to make a request.
     * @param ip Client IP
     * @return true if allowed, false if rate-limited
     */
    public boolean allow(String ip) {
        String key = PREFIX + ip;
        Long count = redisTemplate.opsForValue().increment(key);
        if (count == 1) {
            redisTemplate.expire(key, WINDOW);
        }

        return count <= MAX_REQUESTS;
    }
}
