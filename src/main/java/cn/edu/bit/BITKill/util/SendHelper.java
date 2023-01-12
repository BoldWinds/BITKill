package cn.edu.bit.BITKill.util;

import cn.edu.bit.BITKill.model.params.CommonResp;
import cn.edu.bit.BITKill.model.GlobalData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

@Slf4j
public class SendHelper {

    // 将response发给users中的所有用户
    // 若所有用户都发送成功，则返回true；否则返回false
    public static boolean sendMessageByList(List<String> users,CommonResp response){
        // 先确定所有用户都在可以发送的状态
        for (String user : users){
            if (GlobalData.getSessionByUsername(user) == null){
                log.warn("user: "+ user + "is offline, cannot send message!");
                return false;
            }
        }
        // 运行到这里说明所有要发送的用户都在线
        for (String user : users){
            WebSocketSession session = GlobalData.getSessionByUsername(user);
            if (session != null){
                sendMessageBySession(session,response);
            }
        }
        return true;
    }

    public static boolean sendMessageByUsername(String username,CommonResp response){
        WebSocketSession session = GlobalData.getSessionByUsername(username);
        if(session == null){
            log.warn("user: "+ username + "is offline, cannot send message!");
            return false;
        }else{
            return sendMessageBySession(session,response);
        }
    }

    // 通过一个session发送response
    public static boolean sendMessageBySession(WebSocketSession session,CommonResp response){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            byte[] responseJson = objectMapper.writeValueAsBytes(response);
            session.sendMessage(new TextMessage(responseJson));
            log.info("send message to session: "+session.getId() + new String(responseJson));
            return true;
        }catch (IOException e){
            e.printStackTrace();
            log.warn("IOException happens when sending message to session: "+session.getId());
            return false;
        }
    }

    // 向List中除了except对应用户发送response
    public static boolean sendMessageByListExcept(List<String> users, CommonResp response, String except)
    {
        // 先确定所有用户都在可以发送的状态
        for (String user : users) {
            if (user.equals(except)) {
                continue;
            }
            if (GlobalData.getSessionByUsername(user) == null) {
                return false;
            }
        }
        // 运行到这里说明所有要发送的用户都在线
        for (String user : users) {
            if (user.equals(except)) {
                continue;
            }
            sendMessageBySession(GlobalData.getSessionByUsername(user), response);
        }
        return true;
    }

    public static void sendErrorMessage(WebSocketSession session) throws IOException {
        session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsBytes(new CommonResp<>())));
    }
}
