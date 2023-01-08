package cn.edu.bit.BITKill.model;

import java.util.HashMap;

// 用于保存一些不需要返回给client，仅供server使用的game信息
public class GameControl {

    public long roomID;

    // 存储每个用户被投了几票
    private HashMap<String, Integer> voteMap;

    // 存储哪个用户投了哪个
    private HashMap<String,String> voterTargetMap;

    // 狼人目标
    private String killTarget;

    // 毒药/解药目标
    private String witchTarget;

    // 使用毒药/解药/不使用
    private Drug drugType;

    public GameControl(long roomID) {
        this.roomID = roomID;
        voteMap = new HashMap<>();
        voterTargetMap = new HashMap<>();
        this.killTarget = "";
        this.witchTarget = "";
        this.drugType = Drug.NONE;
    }

    // 将两个map都置空
    public void setEmptyMap(){
        voteMap = new HashMap<>();
        voterTargetMap = new HashMap<>();
    }

    // voter投票给target
    public void vote(String voter,String target) {
        this.voterTargetMap.put(voter,target);
        if(voteMap.containsKey(target)){
            voteMap.replace(target,(voteMap.get(target)+1));
        }else{
            voteMap.put(target,1);
        }
    }

    // 获取投票结果
    public String getVoteResult(){
        int maxVote = 0;
        String result = null;
        for(String voter : voteMap.keySet()){
            if(voteMap.get(voter) > maxVote){
                result = voter;
            }
        }
        return result;
    }

    public HashMap<String, Integer> getVoteMap() {
        return voteMap;
    }

    public void setVoteMap(HashMap<String, Integer> voteMap) {
        this.voteMap = voteMap;
    }

    public HashMap<String, String> getVoterTargetMap() {
        return voterTargetMap;
    }

    public void setVoterTargetMap(HashMap<String, String> voterTargetMap) {
        this.voterTargetMap = voterTargetMap;
    }

    public long getRoomID() {
        return roomID;
    }

    public void setRoomID(long roomID) {
        this.roomID = roomID;
    }

    public String getKillTarget() {
        return killTarget;
    }

    public void setKillTarget(String killTarget) {
        this.killTarget = killTarget;
    }

    public String getDrugTarget() {
        return witchTarget;
    }

    public void setDrugTarget(String witchTarget) {
        this.witchTarget = witchTarget;
    }

    public String getWitchTarget() {
        return witchTarget;
    }

    public void setWitchTarget(String witchTarget) {
        this.witchTarget = witchTarget;
    }

    public Drug getDrugType() {
        return drugType;
    }

    public void setDrugType(Drug drugType) {
        this.drugType = drugType;
    }
}
