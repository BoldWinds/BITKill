package cn.edu.bit.BITKill;

import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GlobalData {

    static private List<Room> rooms = new ArrayList<>();

    static private HashMap<String, WebSocketSession> userSessionMap = new HashMap<>();

    static public void addRoom(Room room){
        rooms.add(room);
    }

    //static public void removeRoom()

    // 用户登录，给Map添加<username,ID>对
    static public void userLogin(String username,WebSocketSession session){
        userSessionMap.put(username,session);
    }

    // 用户下线，给Map删除<username,ID>对
    static public void userLogout(String username,WebSocketSession session){
        userSessionMap.remove(username,session);
    }

    // 通过用户名获取对应sessionID
    static public WebSocketSession getSessionID(String username){
        return userSessionMap.get(username);
    }




}
