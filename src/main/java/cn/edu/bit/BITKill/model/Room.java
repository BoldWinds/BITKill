package cn.edu.bit.BITKill.model;

import cn.edu.bit.BITKill.util.PrintHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Room {

    public long roomID;

    private String creator;

    private String roomName;

    // 为""则代表没有密码
    private String password;

    private int playerCount;

    // 保存房间中玩家的username
    private List<String> players;

    private boolean gaming;

    private boolean full;

    // 玩家进入该房间
    public void addPlayer(String username){
        GlobalData.addUserRoomMap(username, roomID);
        players.add(username);
        playerCount++;
        if(playerCount >= 7)
        {
            full = true;
        }
    }

    // 玩家退出房间
    public void removePlayer(String username){
        GlobalData.removeUserRoomMap(username);
        players.remove(username);
        playerCount--;
        if(playerCount < 7)
        {
            full = false;
        }
    }


    public Room() {
    }

    public Room(long roomID){
        this.roomID = roomID;
        this.creator = "";
        this.roomName = "undef";
        this.password = "";
        this.playerCount = 0;
        this.players = new ArrayList<>();
        this.gaming = false;
        this.full = false;
    }

    public Room(long roomID, String creator, String roomName, String password, int playerCount, List<String> players, boolean gaming, boolean full) {
        this.roomID = roomID;
        this.creator = creator;
        this.roomName = roomName;
        this.password = password;
        this.playerCount = playerCount;
        this.players = players;
        this.gaming = gaming;
        this.full = full;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    public boolean isGaming() {
        return gaming;
    }

    public void setGaming(boolean gaming) {
        this.gaming = gaming;
    }

    public boolean isFull() {
        return full;
    }

    public void setFull(boolean full) {
        this.full = full;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomID=" + roomID +
                ", creator='" + creator + '\'' +
                ", roomName='" + roomName + '\'' +
                ", password='" + password + '\'' +
                ", playerCount=" + playerCount +
                ", players=" + PrintHelper.list2String(players) +
                ", gaming=" + gaming +
                ", full=" + full +
                '}';
    }
}
