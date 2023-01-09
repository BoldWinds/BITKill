package cn.edu.bit.BITKill.model.params;

import java.util.HashMap;

public class VoteResult {

    // 标志是否平票，true为平票，false为有结果
    private boolean tie;

    private String result;

    private HashMap<String,String> voterTargetMap;

    public VoteResult() {
    }

    public VoteResult(boolean tie, String result, HashMap<String, String> voterTargetMap) {
        this.tie = tie;
        this.result = result;
        this.voterTargetMap = voterTargetMap;
    }

    public boolean isTie() {
        return tie;
    }

    public void setTie(boolean tie) {
        this.tie = tie;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public HashMap<String, String> getVoterTargetMap() {
        return voterTargetMap;
    }

    public void setVoterTargetMap(HashMap<String, String> voterTargetMap) {
        this.voterTargetMap = voterTargetMap;
    }
}
