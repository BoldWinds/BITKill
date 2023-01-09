package cn.edu.bit.BITKill.model.params;

public class JoinOrLeaveParam {

    private String user;

    private long roomID;

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public long getRoomID() {
        return roomID;
    }

    public void setRoomID(long roomID) {
        this.roomID = roomID;
    }

    public JoinOrLeaveParam() {
    }

    public JoinOrLeaveParam(String user, long roomID) {
        this.user = user;
        this.roomID = roomID;
    }
}
