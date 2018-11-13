package com.lg.transport;

import io.netty.channel.Channel;
import io.netty.channel.local.LocalServerChannel;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * java平台的阻塞IO服务
 */
public class PlainOioServer {

    public void serve(int port) throws IOException {
       final ServerSocket serverSocket = new ServerSocket(port);
       try{
           while (true){

               Socket client = serverSocket.accept();
               System.out.println("Accepted connection from " + client);

               new Thread(new Runnable() {
                   @Override
                   public void run() {
                       try (OutputStream out = client.getOutputStream()){

                           out.write("Hi!\n".getBytes(Charset.forName("UTF-8")));
                           out.flush();

                       } catch (IOException e) {
                           e.printStackTrace();
                       }finally {
                           try {
                               client.close();
                           } catch (IOException e) {

                           }
                       }
                   }
               }).start();

           }

       }catch (IOException e){
           e.printStackTrace();
       }

    }


    public static void main(String[] args) throws IOException {
        new PlainOioServer().serve(8080);
    }
}
