package com.myapp.Airports.service;

import com.myapp.Airports.exceptions.TooManyLoginAttemptsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LoginRateLimiterTest {

    private LoginRateLimiter limiter;

    @BeforeEach
    void setUp() {
        limiter = new LoginRateLimiter();
        ReflectionTestUtils.setField(limiter, "maxFailures", 2);
        ReflectionTestUtils.setField(limiter, "windowMs", 60_000L);
        ReflectionTestUtils.setField(limiter, "maxTrackedUsers", 10);
    }

    @Test
    void failuresAreNormalizedAndLockAfterConfiguredThreshold() {
        limiter.recordFailure(" John ");
        limiter.recordFailure("john");

        assertThrows(TooManyLoginAttemptsException.class,
                () -> limiter.checkAllowed("JOHN"));
    }

    @Test
    void resetAllowsLoginAgain() {
        limiter.recordFailure("john");
        limiter.recordFailure("john");
        limiter.reset(" john ");

        assertDoesNotThrow(() -> limiter.checkAllowed("JOHN"));
    }

    @Test
    void nullUsernamesShareTheBlankBucket() {
        limiter.recordFailure(null);
        limiter.recordFailure("  ");

        assertThrows(TooManyLoginAttemptsException.class,
                () -> limiter.checkAllowed(null));
    }
}
