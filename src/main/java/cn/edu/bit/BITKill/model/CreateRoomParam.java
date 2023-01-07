package cn.edu.bit.BITKill.model;

public class CreateRoomParam {

    // 该类只有一个creator字段标识是谁创建房间
    private String creator;

    private String password;

    private String roomName;

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public CreateRoomParam() {
    }

    public CreateRoomParam(String creator) {
        this.creator = creator;
    }
}
