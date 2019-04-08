package com.cw.client.tcp;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class TcpSocketServer {
    public static void main(String[] avgs){
        try {
            ServerSocket server = new ServerSocket(8800);
            // 阻塞服务 ，检测到有一个请求，创建 Socket;
            Socket socket = server.accept();

            /*
            2. receive Object
             */
            InputStream is = socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            User user = (User)ois.readObject();
            System.out.println(" This msg is from  User "+user.name);



            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os);

            pw.write("copy!");
            pw.flush();
            InetAddress inetAddress = socket.getInetAddress();
            System.out.println(inetAddress.getHostAddress());
            System.out.println(inetAddress.getHostName());
            ois.close();
            is.close();
            pw.close();
            os.close();
            socket.close();
            server.close();

/*
 This msg is from  User Tom
192.168.43.162
cwizs-air

   */

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
