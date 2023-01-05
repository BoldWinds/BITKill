package cn.edu.bit.BITKill.model;

public class CreateRoomParam {

    // 该类只有一个creator字段标识是谁创建房间
    private String creator;

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
