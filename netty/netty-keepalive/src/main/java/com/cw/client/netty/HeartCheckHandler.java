package com.cw.client.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

public class HeartCheckHandler extends ChannelInboundHandlerAdapter {

    private int readFailCount = 0;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            switch (event.state()) {
                case READER_IDLE:
                    readFailCount++;
                    break;
                case WRITER_IDLE:
                    break;
                case ALL_IDLE:
                    break;
            }

            if (readFailCount >=3 ) {
                readFailCount=0;
                ctx.channel().close();
            }
        }



    }
}
