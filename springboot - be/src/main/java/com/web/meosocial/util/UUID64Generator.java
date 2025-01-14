package com.web.meosocial.util;

import java.util.concurrent.ThreadLocalRandom;

public class UUID64Generator {
    /**
     * Generates a 64-bit unique identifier (UUID) using the current timestamp and random bits.
     * The upper 42 bits come from the current time in milliseconds, and the lower 22 bits are random.
     *
     * @return A 64-bit long representing the generated UUID.
     */
    public long generateUUID64() {
        long timestamp = System.currentTimeMillis() & ((1L << 42) - 1);
        long randomBits = ThreadLocalRandom.current().nextLong(1L << 22);
        return (timestamp << 22) | randomBits;
    }
}
