package com.lg.echo.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelHandler.Sharable //表示可以在多个channel上共享
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Server received: " + msg);
        ctx.write(msg); //把收到的信息写回去.请注意，这不会将消息刷像远程对方.
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
       // 将所有先前写入的消息（待机）刷新到远程对方，并在操作完成后关闭该通道。
        ctx.channel().writeAndFlush(Unpooled.EMPTY_BUFFER);
                //.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //记录异常日志
        cause.printStackTrace();
        ctx.close();
    }
}
