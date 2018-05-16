package demo.websocket;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author wuwei
 * @date 2018/5/9 17:10
 */
@ServerEndpoint("/websocket")
@Component
public class MyWebSocket {

    private static Logger logger = Logger.getLogger(MyWebSocket.class.getName());

    //连接
    @OnOpen
    public void onOpen(Session session) {
        //连接上后给客户端一个消息
        sendMsg(session, "连接服务器成功！");
    }

    //关闭
    @OnClose
    public void onClose() {
        logger.log(Level.INFO, "连接已关闭！");
    }

    //接收客户端消息
    @OnMessage
    public void onMessage(String message) {
        logger.log(Level.INFO, "客户端发送消息：" + message);
    }

    //异常
    @OnError
    public void onError(Session session, Throwable throwable) {
        sendMsg(session, throwable.getMessage());
    }

    //发送消息给客户端
    public synchronized void sendMsg(Session session, String msg) {
        try {
            session.getBasicRemote().sendText(msg);
        } catch (Exception e) {
            logger.log(Level.SEVERE, null, e);
        }
    }
}
