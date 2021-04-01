package com.cw.client.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.UUID;

public class ClientChannelHandler extends ChannelInboundHandlerAdapter {

    private NettyClient client;

    int count = 0;

    public ClientChannelHandler(NettyClient client) {
        this.client = client;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("client" + UUID.randomUUID());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String buf = (String) msg;
        System.out.println("from server: " + buf);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        client.connect();
    }
}
