package com.cw.client.tcp;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpSocketClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("abcdef", 8800)) {
            OutputStream os = socket.getOutputStream();

            /*
            2. send object
             */
            ObjectOutputStream oos = new ObjectOutputStream(os);
            User user = new User("Tom");
            oos.writeObject(user);
            oos.flush();
            socket.shutdownOutput();

            /*


             */
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String input = null;
            while( (input= br.readLine())!=null){
                System.out.println(input);
            }
            InetAddress inetAddress = socket.getInetAddress();
            System.out.println(inetAddress.getHostAddress());
            System.out.println(inetAddress.getHostName());
            oos.close();
            os.close();
            br.close();
            is.close();
            socket.close();
/*
copy!
192.168.43.162
abcdef

 */


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
