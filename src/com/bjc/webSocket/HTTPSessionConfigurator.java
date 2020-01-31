package com.bjc.webSocket;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class HTTPSessionConfigurator extends ServerEndpointConfig.Configurator {

    /**
      在客户端与webSocket在进行第一次握手的过程，获取HttpSession
     */
    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
        // 1. 拿到HTTPSession
        HttpSession httpSession = (HttpSession)request.getHttpSession();
        // 2. 将HTTPSession存储在ServerEndpointConfig对象中
        config.getUserProperties().put(HttpSession.class.getName(),httpSession);
    }
}
