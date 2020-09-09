package com.cw.client.websocket;




import com.cw.websocket.client.MyWebsocketClient;
import lombok.extern.slf4j.Slf4j;




import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author joseph
 */
@Slf4j
//@SpringBootApplication
//@EnableWebMvc
public class WebsocketClientApplication {
    static AtomicInteger openFailed = new AtomicInteger(0);
    static final Object obj = new Object();
    static final Map<MyWebsocketClient, Object> clientObjectMap = new ConcurrentHashMap<>();

    public static void main(String[] args) throws InterruptedException, URISyntaxException {
        try {
//            SpringApplication.run(WebsocketClientApplication.class, args);

            sendMsg();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendMsg() throws URISyntaxException {
        final String pre = "gbid_";
        String gbid = pre + "1";
//        URI uri = new URI("ws://20.1.120.192:8012/facecenter/phoneWebsocket/" + gbid);
        URI uri = new URI("ws://localhost:8014/websocket/" + gbid);
//        URI uri = new URI("ws://192.168.3.83:8000/facecenter/phoneWebsocket/" + gbid);

        MyWebsocketClient client = new MyWebsocketClient(uri, gbid);
        try {
            client.connectBlocking();
            client.send("{'type':'location', 'lat':0, 'lng':0}");
            clientObjectMap.put(client, obj);
        } catch (Exception e) {
            e.printStackTrace();
            openFailed.incrementAndGet();
        }

//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                int i = 0;
//                try {
//                    for (; i < 1 ; i++ ) {
//                        final String gbid = pre + i;
//                        URI uri = new URI("ws://20.1.120.192:8000/facecenter/phoneWebSocket/" + gbid);
//                        MyWebsocketClient client = new MyWebsocketClient(uri, gbid);
//                        try {
//                            client.connect();
//
////                            while(!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)){
////                                System.out.println("还没有打开");
////                            }
//                            System.out.println("==== 打开===");
//                            client.send("");
//                            clientObjectMap.put(client, obj);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            openFailed.incrementAndGet();
//                        }
//                    }
//                } catch (Exception e){
//                   e.printStackTrace();
//                }
//            }
//        });
//        thread.setDaemon(true);
//        thread.start();
//        thread.join();
        int read = 0;
        try {
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextInt()) {
                if (scanner.nextInt() == 1) {
                    break;
                }
            }
//            while (read != 1) {
//                read = System.in.read();
//            }
            log.error("createCount "     );
            for (MyWebsocketClient client1 : clientObjectMap.keySet()) {
                try {
                    client1.close();
                }catch (Exception e) {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
