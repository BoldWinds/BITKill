package cn.edu.bit.BITKill.model;

import cn.edu.bit.BITKill.util.PrintHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

// 这个类保存游戏中关于身份、玩家和游戏状态的信息
public class Game {

    public long roomID;

    private List<String> players;

    // 玩家对身份的映射
    private HashMap<String, Character> playerCharacterMap;

    // 存储担任警长的username
    private String captain;

    // 存储玩家是否存活的map
    private HashMap<String,Boolean> playerStateMap;

    private GameState gameState;

    // 为true代表需要竞选警长
    boolean electCaptain;

    // 存储女巫剩余的药
    private Drug drugs;

    // 随机分配身份
    // 必须在players正确包含所有玩家, 并且playerCharacterMap包含所有玩家到UNDEF的键值对时才正确
    public void assignCharacters(){
        int size = players.size();
        // 创建一个随机数生成器
        Random random = new Random();

        // 定义每种角色的数量
        int numProphet = 1;
        int numWitch = 1;
        int numWolf = (size - 2)/2;
        int numVillage = size - 2 - numWolf;

        // 遍历HashMap
        for (String key : playerCharacterMap.keySet()) {
            // 生成0到size-1的随机数
            int randomInt = random.nextInt(size);

            // 根据随机数分配角色
            if(numVillage > 0 && randomInt < numVillage){
                playerCharacterMap.put(key, Character.VILLAGE);
                numVillage--;
            }else if (numWolf > 0 && randomInt < numWolf + numVillage){
                playerCharacterMap.put(key, cn.edu.bit.BITKill.model.Character.WOLF);
                numWolf--;
            }else if (numProphet > 0 && randomInt < numProphet + numWolf + numVillage){
                playerCharacterMap.put(key, Character.PROPHET);
                numProphet--;
            }else{
                playerCharacterMap.put(key, Character.WITCH);
                numWitch--;
            }
        }
    }

    // 直接向playerStateMap添加键值对
    public void addPlayerStateMap(String player, Boolean alive) {
        this.playerStateMap.put(player,alive);
    }

    // 直接向playerCharacterMap添加键值对
    public void addPlayerCharacterMap(String player, Character character) {
        this.playerCharacterMap.put(player,character);
    }

    // 获取某个身份的所有玩家
    public List<String> getPlayersByCharacter(Character character){
        List<String> players = new ArrayList<>();
        for (String player : playerCharacterMap.keySet()){
            if(character == playerCharacterMap.get(player)){
                players.add(player);
            }
        }
        return players;
    }

    // 获取某个身份的所有存活玩家
    public List<String> getAlivePlayersByCharacter(Character character){
        List<String> players = new ArrayList<>();
        for (String player : playerCharacterMap.keySet()){
            if(character == playerCharacterMap.get(player) && playerStateMap.get(player)){
                players.add(player);
            }
        }
        return players;
    }

    // 构造方法:
    public Game() {
        this.roomID = 1;
        this.players = new ArrayList<>();
        this.playerCharacterMap = new HashMap<>();
        this.captain = "";
        this.playerStateMap = new HashMap<>();
        this.gameState = GameState.START;
        this.electCaptain = true;
        this.drugs = Drug.ALL;
    }

    public Game(long roomID) {
        this.roomID = roomID;
        this.players = new ArrayList<>();
        this.playerCharacterMap = new HashMap<>();
        this.captain = "";
        this.playerStateMap = new HashMap<>();
        this.gameState = GameState.START;
        this.electCaptain = true;
        this.drugs = Drug.ALL;
    }

    public Game(long roomID, List<String> players, HashMap<String, Character> playerCharacterMap, String captain, HashMap<String, Boolean> playerStateMap, GameState gameState, boolean electCaptain,Drug drugs) {
        this.roomID = roomID;
        this.players = players;
        this.playerCharacterMap = playerCharacterMap;
        this.captain = captain;
        this.playerStateMap = playerStateMap;
        this.gameState = gameState;
        this.electCaptain = electCaptain;
        this.drugs = drugs;
    }

    // Get and Set Methods:
    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    public HashMap<String, Character> getPlayerCharacterMap() {
        return playerCharacterMap;
    }

    public void setPlayerCharacterMap(HashMap<String, Character> playerCharacterMap) {
        this.playerCharacterMap = playerCharacterMap;
    }

    public String getCaptain() {
        return captain;
    }

    public void setCaptain(String captain) {
        this.captain = captain;
    }

    public HashMap<String, Boolean> getPlayerStateMap() {
        return playerStateMap;
    }

    public void setPlayerStateMap(HashMap<String, Boolean> playerStateMap) {
        this.playerStateMap = playerStateMap;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public long getRoomID() {
        return roomID;
    }

    public void setRoomID(long roomID) {
        this.roomID = roomID;
    }

    public boolean isElectCaptain() {
        return electCaptain;
    }

    public void setElectCaptain(boolean electCaptain) {
        this.electCaptain = electCaptain;
    }

    public Drug getDrugs() {
        return drugs;
    }

    public void setDrugs(Drug drugs) {
        this.drugs = drugs;
    }

    @Override
    public String toString() {
        return "Game{" +
                "roomID=" + roomID +
                ", players=" +PrintHelper.list2String(players)  +
                ", playerCharacterMap=" + PrintHelper.map2String(playerCharacterMap) +
                ", captain='" + captain + '\'' +
                ", playerStateMap=" + PrintHelper.map2String(playerStateMap) +
                ", gameState=" + gameState +
                '}';
    }
}