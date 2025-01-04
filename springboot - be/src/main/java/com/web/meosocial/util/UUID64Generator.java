package com.web.meosocial.util;

import java.util.concurrent.ThreadLocalRandom;

public class UUID64Generator {
    public long generateUUID64() {
        long timestamp = System.currentTimeMillis() & ((1L << 42) - 1);
        long randomBits = ThreadLocalRandom.current().nextLong(1L << 22);
        return (timestamp << 22) | randomBits;
    }
}
