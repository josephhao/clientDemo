package com.cw.client.udp;

import java.io.*;
import java.net.*;

public class UdpSocketClient {
    public static void main(String[] args) {

        try {
            InetAddress inetAddress = InetAddress.getByName("localhost");
            DatagramSocket ds = new DatagramSocket( 8809);
            byte[] bytes = "tttttttttt".getBytes();
            // 当 DatagramPacket 构造函数中 inetAddress 是目标IP ， port 为 目标端口；
            DatagramPacket datagramPacket = new DatagramPacket(bytes , bytes.length, inetAddress,8800);
            ds.send(datagramPacket);
            System.out.println("Client send over");

            bytes = new byte[1024];
            datagramPacket = new DatagramPacket(bytes, bytes.length);
            ds.receive(datagramPacket);
            String rep = new String(bytes , 0, datagramPacket.getLength());
            System.out.println(rep);

            if(!ds.isClosed()) {
                ds.close();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
