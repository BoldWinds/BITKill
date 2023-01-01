package cn.edu.bit.BITKill;

import java.util.ArrayList;
import java.util.List;

public class Room {

    private String creatorUsername;

    // 保存房间中玩家的username
    private List<String> players;

    // 玩家进入该房间
    public void addPlayer(String username){
        players.add(username);
    }

    // 玩家退出房间
    public void removePlayer(String username){
        players.remove(username);
    }

    public Room(String creatorUsername) {
        this.creatorUsername = creatorUsername;
        this.players = new ArrayList<>();
        players.add(creatorUsername);
    }
}
