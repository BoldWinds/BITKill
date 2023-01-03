package cn.edu.bit.BITKill.model;

import java.util.List;

public class GetRoomsResp {

    private List<Room> rooms;

    public GetRoomsResp() {
        this.rooms = GlobalData.getRooms();
    }
    
}
