package com.bjc.webSocket;

import com.alibaba.fastjson.JSON;
import com.bjc.util.MessageUtil;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ServerEndpoint(value="/chatWeSocket",configurator = HTTPSessionConfigurator.class)
public class ChatSocket {

    // 记录websocket的会话对象session
    private Session session;

    // 记录当前用户的Servlet的HttpSession
    private HttpSession httpSession;

    /**
     * map中存放的是key为HTTPSession，值为Endpoint的实例
     * 因为Endpoint的实例是在第一次握手的时候创建的，且每一个客户端都拥有一个Endpoint的实例对象
     * 而我们的ChatSocket实际上是实现了ChatSocket的类，所以，ChatSocket就是一个Endpoint
     * 该map可以用于记录当前登录用户的HttpSession信息，及对应的Endpoint实例信息。
     */
    private static Map<HttpSession,ChatSocket> onLineUsers = new HashMap<>();

    // 记录当前登录用户数
    private static int onLineCount = 0;

    /**
     * wesocket关闭连接的处理函数
     * @param session
     * @param closeReason
     */
    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        // 关闭之后，在线用户数减1
        decrCount();
        System.out.println("客户端关闭了一个链接，当前在线人数：" + getOnLineCount());
    }

    /**
     * websocket出现异常的处理函数
     * @param session
     * @param throwable
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
        System.out.println("服务异常");
    }

    /**
     * @param message  客户端传递过来的消息内容
     * @param session  当前会话对象
     */
    @OnMessage
    public void onMessage(String message,Session session){
        System.out.println("onMessage: name = " + httpSession.getAttribute("username") + " message内容：" + message );
        // 1. 获取客户端消息内容并解析
        Map<String,String> messageMap = JSON.parseObject(message, Map.class);
        String fromName = messageMap.get("fromName");   // 发送人
        String toName = messageMap.get("toName");       // 接收人
        String content = messageMap.get("content");     // 发送内容

        // 2. 判定是否存在接收人
        if(null == toName || toName.isEmpty()){
            return;
        }
        // 组装消息内容
        String msgContent = MessageUtil.getContent(MessageUtil.TYPE_MESSAGE, fromName, toName, message);
        System.out.println("服务端给客户端发送消息，消息内容：" + msgContent);
        if("All".equals(toName)){  // 3. 如果接收人是广播(All)，如果是，则说明发送广播消息
            broadcastAllUsers(msgContent);
        } else {  // 4. 如果不是广播，则给指定的用户推送消息
            singlePushMsg(msgContent,fromName,toName);
        }
    }

    // 给指定用户发送消息
    private void singlePushMsg(String msgContent,String fromName,String toName) {
        // 定义标记字段，标记toName是否在线
        boolean isOnline = false;
        // 定义toName对应的EndPoint实例对象
        ChatSocket chatSocket2 = null;
        // 定义fromName对应的EndPoint实例对象
        ChatSocket chatSocket4 = null;

        // 1. 判断当前接收人是否在线
        for(HttpSession h : onLineUsers.keySet()){
            if(toName.equals(h.getAttribute("username"))){
                isOnline = true;
                chatSocket2 = onLineUsers.get(h);
            }
            if(fromName.equals(h.getAttribute("username"))){
                chatSocket4 = onLineUsers.get(h);
            }
        }

        // 2. 如果存在，发送消息
        if(isOnline){
            try {
                // 分别给发送人和接收人发送消息
                chatSocket2.session.getBasicRemote().sendText(msgContent);
                chatSocket4.session.getBasicRemote().sendText(msgContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 表示只要请求的是 chatWeSocket ,且是第一次请求就会触发该方法
     * 这里的config就是HTTPSessionConfigurator类中modifyHandshake方法的形参ServerEndpointConfig
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config){
        // 1. 记录webSocket的会话信息对象session
        this.session = session;

        // 2. 获取当前登录用户的HttpSession信息
        HttpSession httpSession = (HttpSession)config.getUserProperties().get(HttpSession.class.getName());
        this.httpSession = httpSession;

        System.out.println("当前登录用户：" + httpSession.getAttribute("username") + ", Endpoint : " +hashCode() + " , 在线用户数：" + onLineUsers.size());

        // 3. 记录当前登录用户信息，及对应的Endpoint实例
        if(httpSession.getAttribute("username") != null){
            onLineUsers.put(httpSession,this);
        }

        // 4. 获取当前所有登录用户
        String names = getNames();

        // 5.组装消息
        String messages = MessageUtil.getContent(MessageUtil.TYPE_USER, "", "", names);

        // 6. 通过广播的形式发送消息
        /*
            发送单挑消息
            发送消息则由RemoteEndpoint完成，其实例由session维护，根据使用情况，
            我们可以通过Session.getBasicRemote获取同步消息发送的实例，
            然后调用RemoteEndpoint的sendXxx()方法就可以发送消息
        * */
        // session.getBasicRemote().sendText(message);
        // 广播就是给所有用户发送信息，我们在onLineUsers中有存储所有在线用户的endpoint的实例信息
        broadcastAllUsers(messages);

        // 7. 记录当前用户登录数
        incrCount();

    }

    // 广播的形式发送消息
    private void broadcastAllUsers(String messages) {
        for(HttpSession s : onLineUsers.keySet()){
            ChatSocket chatSocket = onLineUsers.get(s);
            try {
                chatSocket.session.getBasicRemote().sendText(messages);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 获取在线用户
    private String getNames() {
        StringBuilder names = new StringBuilder();
        if(onLineUsers.size() > 0){
            for(HttpSession h : onLineUsers.keySet()){
                String username = (String)h.getAttribute("username");
                if(names.length() == 0){
                    names.append(username);
                } else {
                    names.append("," + username);
                }
            }
        }
        return names.toString();
    }

    public int getOnLineCount(){
        return onLineCount;
    }

    public synchronized void incrCount(){
        onLineCount ++;
    }

    public synchronized void decrCount(){
        onLineCount --;
    }
}
