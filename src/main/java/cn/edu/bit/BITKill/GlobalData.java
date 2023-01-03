package cn.edu.bit.BITKill;

import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GlobalData {

    static private long nextRoomId = 1;

    static private List<Room> rooms = new ArrayList<>();

    static private HashMap<String, WebSocketSession> userSessionMap = new HashMap<>();

    public static long getNextRoomId() {
        return nextRoomId;
    }

    static public void addRoom(Room room){
        rooms.add(room);
        nextRoomId++;
    }

    static public void removeRoom(long roomId){
        for(int i=0;i<rooms.size();i++){
            if(rooms.get(i).roomID == roomId){
                rooms.remove(i);
                return;
            }
        }
    }

    // 获取房间列表
    public static List<Room> getRooms() {
        return rooms;
    }


    // 用户登录，给Map添加<username,ID>对1
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
