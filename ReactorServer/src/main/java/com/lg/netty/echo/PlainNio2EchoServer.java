package com.lg.netty.echo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

public class PlainNio2EchoServer {
    public void serve(int port) throws IOException {
        System.out.println("Listening for connections on port " + port);

        final AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(port);
        serverChannel.bind(inetSocketAddress);

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        serverChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
            @Override
            public void completed(AsynchronousSocketChannel channel, Object attachment) {
                serverChannel.accept(null,this);
                ByteBuffer buffer = ByteBuffer.allocate(100);
                channel.read(buffer,buffer,new EchoCompletionHandler(channel));
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                try {
                    serverChannel.close();

                } catch (IOException e) {
                 // ingnore on close
                } finally {
                    countDownLatch.countDown();
                }
            }
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }


    private final class EchoCompletionHandler implements CompletionHandler<Integer, ByteBuffer>{

        private final AsynchronousSocketChannel socketChannel;

        public EchoCompletionHandler(AsynchronousSocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        @Override
        public void completed(Integer result, ByteBuffer buffer) {
            buffer.flip(); //切换至可读

            socketChannel.write(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    if(buffer.hasRemaining()){
                        socketChannel.write(buffer,buffer,this);
                    }else {
                        buffer.compact();
                        socketChannel.read(buffer, buffer,
                                EchoCompletionHandler.this);
                    }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    try {
                        socketChannel.close();
                    } catch (IOException e) {
                        // ingnore on close
                    }
                }
            });
        }

        @Override
        public void failed(Throwable exc, ByteBuffer buffer) {
            try {
                socketChannel.close();
            } catch (IOException e) {
                // ingnore on close
            }
        }
    }
}
