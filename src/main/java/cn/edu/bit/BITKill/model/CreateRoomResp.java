package cn.edu.bit.BITKill.model;

public class CreateRoomResp {
    private final String type = "create room";

    private boolean success;

    private long roomID;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getRoomID() {
        return roomID;
    }

    public void setRoomID(long roomID) {
        this.roomID = roomID;
    }

    public CreateRoomResp(boolean success, long roomID) {
        this.success = success;
        this.roomID = roomID;
    }
}
