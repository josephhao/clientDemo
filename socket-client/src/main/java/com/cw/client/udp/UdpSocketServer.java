package com.cw.client.udp;

import java.io.*;
import java.net.*;


public class UdpSocketServer {
    public static void main(String[] avgs){
        try {
            byte[] receive = new byte[1024];
            // 当 DatagramPacket 构造函数 只 bytes 和 length 两个参数时，表示用来接收数据；
            DatagramPacket dp =  new DatagramPacket(receive, receive.length);
            DatagramSocket ds = new DatagramSocket(8800);
            ds.receive(dp);
            String str = new String(receive, 0, dp.getLength());
            System.out.println(str);

            // 目标网址；
            SocketAddress sa = dp.getSocketAddress();
            String reply = "ppppppppppp";
            dp = new DatagramPacket(reply.getBytes(), reply.length(),sa);
            ds.send(dp);
            System.out.println("Server send over");

            if(!ds.isClosed()){
                ds.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
