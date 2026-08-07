package com.example.ultramemorycore.pool;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class UploadBufferPool {

    private static final int MAX_BUCKET = 2 * 1024 * 1024;

    private static final Map<Integer, Deque<ByteBuffer>> BUCKETS =
            new ConcurrentHashMap<>();

    private static volatile long temporaryLimit = 16L * 1024L * 1024L;

    private UploadBufferPool() {
    }

    public static void configure(long limitBytes) {
        temporaryLimit = Math.max(1024L * 1024L, limitBytes);
    }

    public static ByteBuffer acquire(int capacity) {

        int bucketSize = nextPowerOfTwo(capacity);

        if (bucketSize > MAX_BUCKET) {
            return allocate(bucketSize);
        }

        Deque<ByteBuffer> stack = BUCKETS.get(bucketSize);

        if (stack != null) {
            synchronized (stack) {
                ByteBuffer buf = stack.pollFirst();

                if (buf != null) {
                    buf.clear();
                    return buf;
                }
            }
        }

        return allocate(bucketSize);
    }

    public static void release(ByteBuffer buffer) {

        if (buffer == null || !buffer.isDirect()) {
            return;
        }

        int capacity = buffer.capacity();

        if (capacity > MAX_BUCKET || capacity > temporaryLimit) {
            return;
        }

        Deque<ByteBuffer> deque =
                BUCKETS.computeIfAbsent(capacity, k -> new ArrayDeque<>());

        synchronized (deque) {

            // Giới hạn tối đa 4 buffer cho mỗi bucket
            if (deque.size() >= 4) {
                return;
            }

            buffer.clear();
            deque.offerFirst(buffer);
        }
    }

    public static void clear() {
        BUCKETS.clear();
    }

    private static ByteBuffer allocate(int capacity) {
        return ByteBuffer
                .allocateDirect(capacity)
                .order(ByteOrder.nativeOrder());
    }

    private static int nextPowerOfTwo(int value) {

        if (value <= 0) {
            return 1;
        }

        int highest = Integer.highestOneBit(value);

        return value == highest ? highest : highest << 1;
    }
}
