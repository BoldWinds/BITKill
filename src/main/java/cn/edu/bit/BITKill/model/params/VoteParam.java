package cn.edu.bit.BITKill.model.params;

public class VoteParam {

    private long roomID;

    private String voter;

    private String target;

    public VoteParam() {
    }

    public VoteParam(long roomID, String voter, String target) {
        this.roomID = roomID;
        this.voter = voter;
        this.target = target;
    }

    public long getRoomID() {
        return roomID;
    }

    public void setRoomID(long roomID) {
        this.roomID = roomID;
    }

    public String getVoter() {
        return voter;
    }

    public void setVoter(String voter) {
        this.voter = voter;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
