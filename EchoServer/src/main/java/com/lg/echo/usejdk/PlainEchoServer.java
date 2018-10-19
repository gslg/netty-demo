package com.lg.echo.usejdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Description:
 * @Author: liuguo@gridsum.com
 * @Date: 2018/10/18
 */
public class PlainEchoServer {
    public void serve(int port) throws IOException {
        final ServerSocket socket = new ServerSocket(port); //绑定socket服务到端口

        try {
            while (true){
                final Socket clientSocket = socket.accept(); //这里会阻塞知道一个新的客户端连接
                System.out.println("Accepted connection from " + clientSocket);

                new Thread(new Runnable() {  //创建一个线程来处理客户端连接
                    @Override
                    public void run() {
                        try {
                            //读取客户端数据并原样返回给客户端
                            BufferedReader reader = new BufferedReader(
                                    new InputStreamReader(clientSocket.getInputStream()));

                            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(),true);

                            while (true){
                                writer.println(reader.readLine());
                                writer.flush();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                            try {
                                clientSocket.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }).start(); //开始线程
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
