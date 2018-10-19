package com.lg.file.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.stream.ChunkedWriteHandler;


/**
 * @Description:
 * @Author: liuguo@gridsum.com
 * @Date: 2018/10/18
 */
public class HttpStaticFileServerInitializer extends ChannelInitializer<SocketChannel> {

    private SslContext ssCtx;

    public HttpStaticFileServerInitializer(SslContext ssCtx){
        this.ssCtx = ssCtx;
    }


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        if(ssCtx != null){
            pipeline.addLast(ssCtx.newHandler(ch.alloc()));
        }

        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new HttpStaticFileServerHandler());
    }
}
