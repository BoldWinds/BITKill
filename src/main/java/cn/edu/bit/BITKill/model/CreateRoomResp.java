package cn.edu.bit.BITKill.model;

public class CreateRoomResp {

    private long roomID;


    public long getRoomID() {
        return roomID;
    }

    public void setRoomID(long roomID) {
        this.roomID = roomID;
    }

    public CreateRoomResp(long roomID) {
        this.roomID = roomID;
    }
}
