package com.example.ultramemorycore.mixin;

import com.example.ultramemorycore.pool.NettyDirectBufferPool;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.PacketEncoder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PacketEncoder.class)
public class EncoderHandlerMixin {

    @Inject(
        method = "encode(Lio/netty/channel/ChannelHandlerContext;Ljava/lang/Object;Lio/netty/buffer/ByteBuf;)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onEncodeHead(ChannelHandlerContext ctx, Object msg, ByteBuf out, CallbackInfo ci) {
        if (out != null && !out.isDirect()) {
            ByteBuf directBuf = NettyDirectBufferPool.acquireDirect();
            out.writeBytes(directBuf);
            NettyDirectBufferPool.releaseDirect(directBuf);
        }
    }
}
