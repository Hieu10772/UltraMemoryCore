package com.example.ultramemorycore.mixin;

import com.example.ultramemorycore.pool.NettyDirectBufferPool;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.handler.DecoderHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(DecoderHandler.class)
public class DecoderHandlerMixin {

    @Inject(
        method = "decode",
        at = @At("TAIL")
    )
    private void onDecodeTail(ChannelHandlerContext ctx, ByteBuf in, List<Object> out, CallbackInfo ci) {
        if (in != null && in.isDirect() && !in.isReadable()) {
            NettyDirectBufferPool.releaseDirect(in);
        }
    }
}
