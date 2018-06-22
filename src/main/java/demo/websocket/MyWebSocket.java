package demo.websocket;

import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 * @author wuwei
 * @date 2018/5/9 17:10
 */
@ServerEndpoint("/websocket")
@Component
public class MyWebSocket {

    private static Logger logger = Logger.getLogger(MyWebSocket.class.getName());
    private static final List<Session> list = Collections.synchronizedList(new LinkedList<>());

    //连接
    @OnOpen
    public void onOpen(Session session) {
        list.add(session);
        //连接上后给客户端一个消息
        sendMsg(session, "连接服务器成功！");
    }

    //接收客户端文本消息
    @OnMessage
    public void onMessage(String message, Session session) {
        //转发消息给其它所有客户端
        for (Session s : list) {
            if (s != session) {
                sendMsg(s, message);
            }
        }
        logger.log(Level.INFO, "客户端发送消息：" + message);
    }
    
    //接收客户端图片消息
    @OnMessage
    public void onMessage(byte[] message, Session session) {
        if (message == null || message.length == 0) return;
        //给所有其它websocket客户端下发消息
        for (Session s : list) {
            if (s != session) {
                try {
                    s.getBasicRemote().sendBinary(ByteBuffer.wrap(message));
                } catch (Exception e) {
                    logger.log(Level.SEVERE, e.getMessage());
                }
            }
        }
    }
    
    //关闭
    @OnClose
    public void onClose(Session session) {
        list.remove(session);
        logger.log(Level.INFO, "连接已关闭！");
    }

    //异常
    @OnError
    public void onError(Session session, Throwable throwable) {
        sendMsg(session, throwable.getMessage());
    }

    //发送消息给客户端
    public void sendMsg(Session session, String msg) {
        try {
            session.getBasicRemote().sendText(msg);
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
        }
    }
}
