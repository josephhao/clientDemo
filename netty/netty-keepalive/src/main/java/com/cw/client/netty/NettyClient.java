package com.cw.client.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

public class NettyClient {

    private Bootstrap bootstrap;

    private int test = 0;


    public NettyClient() {
        bootstrap = new Bootstrap();
        NioEventLoopGroup workers = new NioEventLoopGroup();
//        try {
        bootstrap = bootstrap.group(workers)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new StringDecoder())
                                .addLast(new StringEncoder())
                                .addLast(new ClientChannelHandler(NettyClient.this));
                    }
                });
    }

    public void connect() throws InterruptedException {
        ChannelFuture future  = bootstrap.connect(new InetSocketAddress(9000));
         future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture f) throws Exception {
                if (f.isSuccess()) {
                    System.out.println("connect server successfully!");
                    Channel channel = f.channel();
                    while (channel.isActive()) {
                        channel.writeAndFlush("keepalive package");
                    }
                } else {
                    Thread.sleep(3000);
                    System.out.println("start to reconnect.");
                    connect();
                }
            }
        });

    }

    public static void main(String[] args) {
        NettyClient client = new NettyClient();
        try {
            client.connect();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
