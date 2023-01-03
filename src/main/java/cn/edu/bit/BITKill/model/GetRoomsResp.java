package cn.edu.bit.BITKill.model;

import cn.edu.bit.BITKill.GlobalData;
import cn.edu.bit.BITKill.Room;

import java.util.List;

public class GetRoomsResp {

    private final String type = "get rooms";

    private boolean success;

    private List<Room> rooms;

    public GetRoomsResp(boolean success) {
        this.success = success;
        this.rooms = GlobalData.getRooms();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
