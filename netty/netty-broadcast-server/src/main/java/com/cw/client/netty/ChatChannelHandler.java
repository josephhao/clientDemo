package com.cw.client.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Date;

@ChannelHandler.Sharable
public class ChatChannelHandler extends SimpleChannelInboundHandler<String> {
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        String s = "[ client ] " + channel.remoteAddress().toString() + " online at " + new Date();
        channelGroup.writeAndFlush(s);
        channelGroup.add(channel);
        System.out.println(s);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.forEach(channel1 -> {
            if (channel == channel1) {
                channel1.writeAndFlush("self: " + msg);
            } else {
                channel1.writeAndFlush(channel1.remoteAddress().toString() + ": " + msg);
            }
        });
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.remove(channel);
        String s = "[ client ] " + channel.remoteAddress().toString() + " offline at " + new Date();
        channelGroup.writeAndFlush(s);
        System.out.println(s);
    }
}
