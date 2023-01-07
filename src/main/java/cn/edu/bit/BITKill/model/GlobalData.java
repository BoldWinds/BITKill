package cn.edu.bit.BITKill.model;

import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalData {

    static private long nextRoomId = 1;

    static private List<Room> rooms = new ArrayList<>();

    // 用户名到session的map
    static private HashMap<String, WebSocketSession> userSessionMap = new HashMap<>();

    static private List<Game> games = new ArrayList<>();

    public static long getNextRoomId() {
        return nextRoomId;
    }

    // 添加房间
    static public void addRoom(Room room){
        rooms.add(room);
        nextRoomId++;
    }

    // 删除房间
    static public void removeRoom(long roomId){
        for(int i=0;i<rooms.size();i++){
            if(rooms.get(i).roomID == roomId){
                rooms.remove(i);
                return;
            }
        }
    }

    // 添加游戏
    static public void addGame(Game game){
        games.add(game);
    }

    // 删除游戏
    static public void removeGame(long roomId){
        for (int i = 0; i<games.size(); i++){
            if(games.get(i).roomID == roomId){
                games.remove(i);
                return;
            }
        }
    }

    // 获取房间列表
    public static List<Room> getRooms() {
        return rooms;
    }

    // 根据ID号获取Room
    public static Room getRoomByID(long roomID){
        for (int i = 0;i<rooms.size(); i++){
            if(rooms.get(i).roomID == roomID){
                return rooms.get(i);
            }
        }
        return null;
    }

    // 根据ID号设置Room
    public static boolean setRoomByID(long roomID,Room room){
        for (int i = 0;i<rooms.size(); i++){
            if(rooms.get(i).roomID == roomID){
                rooms.set(i,room);
                return true;
            }
        }
        return false;
    }

    // 根据ID号获取Game
    public static Game getGameByID(long roomID){
        for (int i = 0;i<games.size(); i++){
            if(games.get(i).roomID == roomID){
                return games.get(i);
            }
        }
        return null;
    }

    // 根据ID号设置Game
    public static boolean setGameByID(long roomID,Game game){
        for (int i = 0;i<games.size(); i++){
            if(games.get(i).roomID == roomID){
                games.set(i,game);
                return true;
            }
        }
        return false;
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
    static public WebSocketSession getSessionByUsername(String username){
        return userSessionMap.get(username);
    }

    static public String getUsernameBySession(WebSocketSession session)
    {
        for (Map.Entry<String, WebSocketSession> entry : userSessionMap.entrySet())
        {
            if(entry.getValue().equals(session))
            {
                return entry.getKey();
            }
        }
        return null;
    }
}
