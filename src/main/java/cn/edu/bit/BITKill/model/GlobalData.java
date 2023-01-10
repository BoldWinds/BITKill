package cn.edu.bit.BITKill.model;

import cn.edu.bit.BITKill.util.PrintHelper;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalData {

    static private long nextRoomId = 1;

    // 程序维护的所有Room列表
    static private List<Room> rooms = new ArrayList<>();

    // 程序维护的所有Game列表
    static private List<Game> games = new ArrayList<>();

    // 程序维护的所有GameControl列表
    static private List<GameControl> gameControls = new ArrayList<>();

    // 用户名到session的map
    static private HashMap<String, WebSocketSession> userSessionMap = new HashMap<>();

//--------------------------------------------------------------------------------------------
// 以下是该类的方法:

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

    // 添加game和gameControl
    static public void addGame(Game game,GameControl gameControl){
        games.add(game);
        gameControls.add(gameControl);
    }

    // 删除game和gameControl
    static public void removeGame(long roomId){
        for (int i = 0; i<games.size(); i++){
            if(games.get(i).roomID == roomId){
                games.remove(i);
                break;
            }
        }
        for (int i = 0; i<gameControls.size(); i++){
            if(gameControls.get(i).roomID == roomId){
                gameControls.remove(i);
                return;
            }
        }
    }

    // 获取房间列表
    public static List<Room> getRooms() {
        return rooms;
    }

    //获取一个未满、无密码的房间（用于匹配）
    public static Room getARandomRoom(){
        for (int i = 0;i<rooms.size(); i++){
            if(!rooms.get(i).isFull() && rooms.get(i).getPassword().equals("")){
                return rooms.get(i);
            }
        }
        return null;
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
    public static boolean setGameByID(long roomID, Game game){
        for (int i = 0;i<games.size(); i++){
            if(games.get(i).roomID == roomID){
                games.set(i,game);
                return true;
            }
        }
        return false;
    }

    // 根据ID号获取GameControl
    public static GameControl getGameControlByID(long roomID){
        for (int i = 0;i<gameControls.size(); i++){
            if(gameControls.get(i).roomID == roomID){
                return gameControls.get(i);
            }
        }
        return null;
    }

    // 根据ID号设置GameControl
    public static boolean setGameControlByID(long roomID, GameControl gameControl){
        for (int i = 0;i<gameControls.size(); i++){
            if(gameControls.get(i).roomID == roomID){
                gameControls.set(i,gameControl);
                return true;
            }
        }
        return false;
    }

    public static void writeBack(long roomID,Game game,GameControl gameControl){
        setGameByID(roomID,game);
        setGameControlByID(roomID,gameControl);
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

    public static long getNextRoomId() {
        return nextRoomId;
    }

    @Override
    public String toString() {
        return "Game{" +
                "roomID=" + getNextRoomId() +
                ", rooms=" + PrintHelper.list2String(rooms)  +
                ", games=" + PrintHelper.list2String(gameControls)+
                ", userSessionMap=" + PrintHelper.map2String(userSessionMap)  +
                '}';
    }
}
