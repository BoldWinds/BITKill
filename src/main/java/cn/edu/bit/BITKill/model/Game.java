package cn.edu.bit.BITKill.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// 保存游戏相关状态的枚举类
enum GameState{
    START,      // 游戏开始
    KILL,       // 狼人刀人
    PROPHET,    // 预言家查验
    WITCH,      // 女巫操作
    ELECT,      // 选举警长
    VOTE,       // 投票环节
    WORDS,      // 遗言
    END,        // 游戏结束

}

enum Character{
    VILLAGE,
    WOLF,
    PROPHET,
    WITCH
}

// 这个类保存游戏中关于身份、玩家和游戏状态的信息
public class Game {

    public long roomID;

    private List<String> players;

    // 玩家对身份的映射
    private HashMap<String,String> playerCharacterMap;

    // 存储担任警长的username
    private String captain;

    // 存储玩家是否存活的map
    private HashMap<String,String> playerStateMap;

    private GameState gameState;

    public Game() {
        this.roomID = 1;
        this.players = new ArrayList<>();
        this.playerCharacterMap = new HashMap<>();
        this.captain = "";
        this.playerStateMap = new HashMap<>();
        this.gameState = GameState.START;
    }

    public Game(long roomID, List<String> players, HashMap<String, String> playerCharacterMap, String captain, HashMap<String, String> playerStateMap, GameState gameState) {
        this.roomID = roomID;
        this.players = players;
        this.playerCharacterMap = playerCharacterMap;
        this.captain = captain;
        this.playerStateMap = playerStateMap;
        this.gameState = gameState;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    public HashMap<String, String> getPlayerCharacterMap() {
        return playerCharacterMap;
    }

    public void setPlayerCharacterMap(HashMap<String, String> playerCharacterMap) {
        this.playerCharacterMap = playerCharacterMap;
    }

    public String getCaptain() {
        return captain;
    }

    public void setCaptain(String captain) {
        this.captain = captain;
    }

    public HashMap<String, String> getPlayerStateMap() {
        return playerStateMap;
    }

    public void setPlayerStateMap(HashMap<String, String> playerStateMap) {
        this.playerStateMap = playerStateMap;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}