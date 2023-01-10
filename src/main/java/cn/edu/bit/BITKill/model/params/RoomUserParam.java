package cn.edu.bit.BITKill.model.params;

public class RoomUserParam {

    private long roomID;

    private String username;

    public RoomUserParam() {
    }

    public RoomUserParam(long roomID, String username) {
        this.roomID = roomID;
        this.username = username;
    }

    public long getRoomID() {
        return roomID;
    }

    public void setRoomID(long roomID) {
        this.roomID = roomID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
