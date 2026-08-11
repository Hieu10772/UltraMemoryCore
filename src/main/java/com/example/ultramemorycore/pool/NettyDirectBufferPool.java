package com.example.ultramemorycore.pool;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NettyDirectBufferPool {
    private static final int MAX_POOL_SIZE = 64;
    private static final int INITIAL_CAPACITY = 4096; 
    private static final Queue<ByteBuf> DIRECT_POOL = new ConcurrentLinkedQueue<>();

    public static ByteBuf acquireDirect() {
        ByteBuf buf = DIRECT_POOL.poll();
        if (buf != null && buf.refCnt() > 0) {
            buf.clear();
            return buf;
        }
        return Unpooled.directBuffer(INITIAL_CAPACITY);
    }

    public static void releaseDirect(ByteBuf buf) {
        if (buf == null) return;

        if (buf.isDirect() && buf.refCnt() > 0 && DIRECT_POOL.size() < MAX_POOL_SIZE) {
            if (buf.capacity() <= 65536) { 
                buf.clear();
                DIRECT_POOL.offer(buf);
                return;
            }
        }
        
        if (buf.refCnt() > 0) {
            buf.release();
        }
    }

    public static void clear() {
        while (!DIRECT_POOL.isEmpty()) {
            ByteBuf buf = DIRECT_POOL.poll();
            if (buf != null && buf.refCnt() > 0) {
                buf.release();
            }
        }
    }
}
