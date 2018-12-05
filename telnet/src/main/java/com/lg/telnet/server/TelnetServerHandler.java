package com.lg.telnet.server;

import io.netty.channel.*;

import java.net.InetAddress;
import java.time.LocalDateTime;

/**
 * 处理服务端的消息
 */
@ChannelHandler.Sharable
public class TelnetServerHandler extends SimpleChannelInboundHandler<String> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //对每个新连接打招呼
        ctx.write("Welcome to "+ InetAddress.getLocalHost().getHostName()+"!\r\n");
        ctx.write("It is "+ LocalDateTime.now()+" now.\r\n");
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        String response;
        boolean close = false;

        if(msg == null || msg.isEmpty()) {
            response = "Please type something!\r\n";
        }else if("bye".equals(msg)) {
            response = "Have a good day!\r\n";
            close = true;
        } else {
          response = "Did you say '" + msg +"' ?\r\n";
        }

        // We do not need to write a ChannelBuffer here.
        // We know the encoder inserted at TelnetPipelineFactory will do the conversion.
        ChannelFuture future = ctx.write(response);

        // Close the connection after sending 'Have a good day!'
        // if the client has sent 'bye'.
        if(close){
            future.addListener(ChannelFutureListener.CLOSE);
        }


    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


}
