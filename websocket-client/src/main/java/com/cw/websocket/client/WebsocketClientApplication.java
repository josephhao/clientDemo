package com.cw.websocket.client;


//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

//@SpringBootApplication
public class WebsocketClientApplication {
    public static void main(String[] args) throws URISyntaxException, InterruptedException {
//        SpringApplication.run(WebsocketClientApplication.class);
        URI uri = new URI("ws://localhost:8014/websocket/gbid");
        MyWebsocketClient client = new MyWebsocketClient(uri, "test");
        client.connectBlocking();

        client.send("mytest");

        int i =0;
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()) {
            client.send("gbid " + i++);
            if (scanner.nextInt() == 1) {
                break;
            }
        }
        client.close();
    }
}
