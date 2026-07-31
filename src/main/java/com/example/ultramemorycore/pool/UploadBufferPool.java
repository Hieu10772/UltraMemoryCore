package com.example.ultramemorycore.pool;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UploadBufferPool {
    private static final Map<Integer, Deque<ByteBuffer>> BUCKETS = new ConcurrentHashMap<>();

    public static ByteBuffer acquire(int capacity) {
        int bucketSize = nextPowerOfTwo(capacity);
        Deque<ByteBuffer> stack = BUCKETS.get(bucketSize);
        if (stack != null) {
            synchronized (stack) {
                if (!stack.isEmpty()) {
                    ByteBuffer buf = stack.pop();
                    buf.clear();
                    return buf;
                }
            }
        }
        return ByteBuffer.allocateDirect(bucketSize).order(ByteOrder.nativeOrder());
    }

    public static void release(ByteBuffer buffer) {
        if (buffer == null || !buffer.isDirect()) return;
        int capacity = buffer.capacity();
        BUCKETS.computeIfAbsent(capacity, k -> new ArrayDeque<>()).add(buffer);
    }

    public static void clear() {
        BUCKETS.clear();
    }

    private static int nextPowerOfTwo(int value) {
        int highestOne = Integer.highestOneBit(value);
        return value == highestOne ? value : highestOne << 1;
    }
}
