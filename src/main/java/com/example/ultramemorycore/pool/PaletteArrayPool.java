package com.example.ultramemorycore.pool;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PaletteArrayPool {
    private static final Map<Integer, Deque<int[]>> POOL = new ConcurrentHashMap<>();

    public static int[] acquire(int length) {
        Deque<int[]> stack = POOL.get(length);
        if (stack != null) {
            synchronized (stack) {
                if (!stack.isEmpty()) {
                    int[] arr = stack.pop();
                    java.util.Arrays.fill(arr, 0);
                    return arr;
                }
            }
        }
        return new int[length];
    }

    public static void release(int[] array) {
        if (array == null || array.length > 4096) return;
        POOL.computeIfAbsent(array.length, k -> new ArrayDeque<>()).add(array);
    }

    public static void clear() {
        POOL.clear();
    }
}
