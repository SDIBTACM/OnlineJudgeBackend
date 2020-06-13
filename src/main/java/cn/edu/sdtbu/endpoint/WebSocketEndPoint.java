package cn.edu.sdtbu.endpoint;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author bestsort
 * @version 1.0
 * @date 2020-06-07 19:25
 */
@Component
//TODO 屏蔽外部请求
@ServerEndpoint("/alerts/{userId}")
public class WebSocketEndPoint {

    /**
     * 存活的session集合（使用线程安全的map保存）
     */
    private static final Map<String, Session> LIVING_SESSIONS = new ConcurrentHashMap<>();

    /**
     * 建立连接的回调方法
     *
     * @param session  与客户端的WebSocket连接会话
     * @param userId 用户名，WebSocket支持路径参数
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Long userId) {
        LIVING_SESSIONS.put(session.getId(), session);
        sendMessageToAll("来自服务端的消息： " + userId + " 加入聊天室");
    }

    /**
     * 收到客户端消息的回调方法
     *
     * @param message 客户端传过来的消息
     * @param session 对应的session
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("userId") Long userId) {
        sendMessageToAll("来自服务端的消息: 发送随机数用以测试" + new Random().nextInt() + "");
    }


    /**
     * 发生错误的回调方法
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        sendMessage(session, "你的请求无法被处理，此链接将会被关闭。请使用 userId。");
    }

    /**
     * 关闭连接的回调方法
     */
    @OnClose
    public void onClose(Session session, @PathParam("userId") Long userId) {
        LIVING_SESSIONS.remove(session.getId());
        sendMessageToAll("来自服务端的消息： " + userId + " 关闭连接");
    }


    /**
     * 单独发送消息
     *
     * @param session
     * @param message
     */
    public void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 群发消息
     *
     * @param message
     */
    public void sendMessageToAll(String message) {
        LIVING_SESSIONS.forEach((sessionId, session) -> {
            sendMessage(session, message);
        });
    }

}

