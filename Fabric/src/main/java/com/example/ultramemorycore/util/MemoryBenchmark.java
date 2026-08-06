package com.example.ultramemorycore.util;

import com.example.ultramemorycore.cache.NbtStringPool;
import com.example.ultramemorycore.pool.ArrayPools;

public class MemoryBenchmark {
    public static void main(String[] args) {
        System.out.println("=== UltraMemoryCore Execution Benchmark ===");
        long startTime = System.nanoTime();

        for (int i = 0; i < 100_000; i++) {
            String key = NbtStringPool.intern("Enchantments");
            byte[] buf = ArrayPools.getByteArray(256);
        }

        long elapsedMs = (System.nanoTime() - startTime) / 1_000_000;
        System.out.println("Time Elapsed: " + elapsedMs + " ms");
        System.out.println("Buffers Reused: " + ArrayPools.REUSED_COUNT.get());
        System.out.println("Buffers Allocated: " + ArrayPools.ALLOCATED_COUNT.get());
    }
}
