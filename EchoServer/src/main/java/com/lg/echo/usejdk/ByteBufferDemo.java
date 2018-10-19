package com.lg.echo.usejdk;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Description:
 * @Author: liuguo@gridsum.com
 * @Date: 2018/10/18
 */
public class ByteBufferDemo {
    public static void main(String[] args) throws Exception {
        FileChannel channel = new FileInputStream("D:\\workspace\\data-process\\README.md").getChannel();
        ByteBuffer buf = ByteBuffer.allocate(1024);

        int byteRead = -1;
        do {
            byteRead = channel.read(buf); //从channel中读数据写入到buf中
            if(byteRead != -1){
                buf.flip(); //切换buf到读模式
                while (buf.hasRemaining()){
                    System.out.println((char)buf.get()); //从buf中读取，每一个get都会更新指针的位置
                }
                buf.clear();//清理buf空间，使buf可以重新写
            }
        }while (byteRead != -1);

        channel.close();
    }
}
