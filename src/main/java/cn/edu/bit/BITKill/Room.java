package cn.edu.bit.BITKill;

import java.util.ArrayList;
import java.util.List;

public class Room {

    public long roomID;

    private String creator;

    private int playerCount;

    // 保存房间中玩家的username
    private List<String> players;

    private boolean gaming;

    private boolean full;


    // 玩家进入该房间
    public void addPlayer(String username){
        players.add(username);
    }

    // 玩家退出房间
    public void removePlayer(String username){
        players.remove(username);
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
}
