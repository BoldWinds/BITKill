package cn.edu.bit.BITKill.model;

public class CreateRoomParam {

    private final String type = "create room";

    private String creator;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public CreateRoomParam(String creator) {
        this.creator = creator;
    }
}
