package com.example.ultramemorycore.pool;

import java.util.concurrent.atomic.AtomicLong;

public class ArrayPools {
    public static final AtomicLong ALLOCATED_COUNT = new AtomicLong(0);
    public static final AtomicLong REUSED_COUNT = new AtomicLong(0);
    public static final AtomicLong DISCARDED_COUNT = new AtomicLong(0);

    private static final ThreadLocal<byte[]> BYTE_POOL = ThreadLocal.withInitial(() -> new byte[0]);
    private static final ThreadLocal<int[]> INT_POOL = ThreadLocal.withInitial(() -> new int[0]);

    public static byte[] getByteArray(int minSize) {
        byte[] current = BYTE_POOL.get();
        if (current.length >= minSize) {
            REUSED_COUNT.incrementAndGet();
            return current;
        }
        ALLOCATED_COUNT.incrementAndGet();
        if (current.length > 0) DISCARDED_COUNT.incrementAndGet();
        byte[] newBuf = new byte[minSize];
        BYTE_POOL.set(newBuf);
        return newBuf;
    }

    public static int[] getIntArray(int minSize) {
        int[] current = INT_POOL.get();
        if (current.length >= minSize) {
            REUSED_COUNT.incrementAndGet();
            return current;
        }
        ALLOCATED_COUNT.incrementAndGet();
        if (current.length > 0) DISCARDED_COUNT.incrementAndGet();
        int[] newBuf = new int[minSize];
        INT_POOL.set(newBuf);
        return newBuf;
    }

    public static void clearAll() {
        BYTE_POOL.remove();
        INT_POOL.remove();
    }
}
