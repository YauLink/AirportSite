package com.myapp.Airports.service;

import com.myapp.Airports.exceptions.TooManyLoginAttemptsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.Locale;

@Component
public class LoginRateLimiter {

    private final ConcurrentMap<String, AttemptWindow> attempts = new ConcurrentHashMap<>();

    @Value("${security.login.max-failures:5}")
    private int maxFailures;

    @Value("${security.login.window-ms:900000}")
    private long windowMs;

    @Value("${security.login.max-tracked-users:10000}")
    private int maxTrackedUsers;

    public void checkAllowed(String username) {
        String key = key(username);
        AttemptWindow window = attempts.get(key);
        if (window == null) {
            return;
        }

        synchronized (window) {
            if (expired(window)) {
                attempts.remove(key, window);
                return;
            }
            if (window.failures >= maxFailures) {
                throw new TooManyLoginAttemptsException();
            }
        }
    }

    public void recordFailure(String username) {
        String key = key(username);
        if (attempts.size() >= maxTrackedUsers) {
            attempts.keySet().stream().findAny().ifPresent(attempts::remove);
        }
        attempts.compute(key, (ignored, current) -> {
            AttemptWindow window = current;
            if (window == null || expired(window)) {
                window = new AttemptWindow(System.currentTimeMillis());
            }
            synchronized (window) {
                window.failures++;
            }
            return window;
        });
    }

    public void reset(String username) {
        attempts.remove(key(username));
    }

    private boolean expired(AttemptWindow window) {
        return System.currentTimeMillis() - window.startedAt >= windowMs;
    }

    private String key(String username) {
        return username == null ? "<blank>" : username.trim().toLowerCase(Locale.ROOT);
    }

    private static final class AttemptWindow {
        private final long startedAt;
        private int failures;

        private AttemptWindow(long startedAt) {
            this.startedAt = startedAt;
        }
    }
}
