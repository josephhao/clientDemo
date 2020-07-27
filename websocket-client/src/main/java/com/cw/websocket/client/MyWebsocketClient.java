package com.cw.websocket.client;


import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;


@Slf4j
public class MyWebsocketClient extends WebSocketClient {

    private String id;

    public MyWebsocketClient(URI serverUri, String id) {
        super(serverUri);
        this.id = id;
    }

    @Override
   public void onOpen(ServerHandshake arg0) {
      // TODO Auto-generated method stub
      log.info("------ MyWebSocket onOpen ------");
   }

   @Override
   public void onClose(int arg0, String arg1, boolean arg2) {
       // TODO Auto-generated method stub
       log.info("------ MyWebSocket onClose ------");
   }

   @Override
   public void onError(Exception arg0) {
       // TODO Auto-generated method stub
       log.info("------ MyWebSocket onError ------");
   }

   @Override
   public void onMessage(String arg0) {
       // TODO Auto-generated method stub
       log.info("-------- 接收到服务端数据： " + arg0 + "--------");
   }
}
