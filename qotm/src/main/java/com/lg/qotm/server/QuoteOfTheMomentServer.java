package com.lg.qotm.server;

import com.lg.qotm.client.QuoteOfTheMomentClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * A UDP server that responds to the QOTM (quote of the moment) request to a {@link QuoteOfTheMomentClient}.
 *
 * Inspired by <a href="http://docs.oracle.com/javase/tutorial/networking/datagrams/clientServer.html">the official
 * Java tutorial</a>.
 */
public final class QuoteOfTheMomentServer {

    private static final int PORT = Integer.parseInt(System.getProperty("port","7686"));

    public static void main(String[] args) throws Exception {
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST,true)
                    .handler(new QuoteOfTheMomentServerHandler());

            b.bind(PORT).sync().channel().closeFuture().await();

        }finally {

        }
    }
}
