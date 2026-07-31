package com.example.ultramemorycore.pool;

import com.example.ultramemorycore.memory.PlatformDetector;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class UploadBufferPool {

    private static final int IOS_MAX_BUCKET = 256 * 1024;
    private static final int DESKTOP_MAX_BUCKET = 2 * 1024 * 1024;

    private static final Map<Integer, Deque<ByteBuffer>> BUCKETS =
            new ConcurrentHashMap<>();

    private UploadBufferPool() {}

    public static ByteBuffer acquire(int capacity) {

        // iOS + Pojav: không reuse direct buffer để tránh native memory buildup
        if (PlatformDetector.disableUploadBufferPooling()) {
            return allocate(capacity);
        }

        int bucketSize = nextPowerOfTwo(capacity);

int maxBucket = PlatformDetector.disableUploadBufferPooling()
        ? IOS_MAX_BUCKET
        : DESKTOP_MAX_BUCKET;

if (bucketSize > maxBucket) {
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

    // iOS / Pojav: không giữ DirectBuffer
    if (PlatformDetector.disableUploadBufferPooling()) {
        return;
    }

    int capacity = buffer.capacity();

    if (capacity > DESKTOP_MAX_BUCKET) {
        return;
    }

    Deque<ByteBuffer> deque =
            BUCKETS.computeIfAbsent(capacity, k -> new ArrayDeque<>());

    synchronized (deque) {

        // Giới hạn tối đa 4 buffer cho mỗi bucket
        if (deque.size() >= 4) {
            return;
        }

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

        return value == highest ? value : highest << 1;
    }
}
