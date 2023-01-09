package cn.edu.bit.BITKill.model.params;

import cn.edu.bit.BITKill.model.Drug;

public class WitchParam {
    private long roomID;

    private String target;

    private Drug drug;

    public WitchParam() {
    }

    public WitchParam(long roomID, String target, Drug drug) {
        this.roomID = roomID;
        this.target = target;
        this.drug = drug;
    }

    public long getRoomID() {
        return roomID;
    }

    public void setRoomID(long roomID) {
        this.roomID = roomID;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Drug getDrug() {
        return drug;
    }

    public void setDrug(Drug drug) {
        this.drug = drug;
    }
}
