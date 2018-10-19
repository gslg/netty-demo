package com.lg.echo.usejdk;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class PlainNioEchoServer {

    public void serve(int port) throws IOException {

        System.out.println("Listening for connections on port " + port);
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        ServerSocket serverSocket = serverSocketChannel.socket();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(port);
        serverSocket.bind(inetSocketAddress);

        serverSocketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true){
            try {
                selector.select();
            }catch (IOException e){
                e.printStackTrace();
                break;
            }

            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> it = readyKeys.iterator();
            while (it.hasNext()){
                SelectionKey key = it.next();
                it.remove();

                try {
                    if(key.isAcceptable()){
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel client = server.accept();

                        System.out.println("Accepted connection from " + client);
                        client.configureBlocking(false);
                        client.register(selector,SelectionKey.OP_WRITE | SelectionKey.OP_READ,
                                ByteBuffer.allocate(100));
                    }

                    if(key.isReadable()){
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer out = (ByteBuffer) key.attachment();
                        client.read(out);
                    }

                    if(key.isWritable()){
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer out = (ByteBuffer) key.attachment();

                        out.flip();
                        client.write(out);
                        out.compact();
                    }
                }catch (IOException e){
                    e.printStackTrace();
                    key.cancel();
                    key.channel().close();
                }
            }
        }

    }
}
