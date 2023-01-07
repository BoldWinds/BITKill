package cn.edu.bit.BITKill.service;

import cn.edu.bit.BITKill.model.*;
import cn.edu.bit.BITKill.util.SendHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {

    //TODO: 完成房间相关API，各个函数的参数还没写

    public void createRoom(WebSocketSession session, String paramJson) throws Exception
    {
        //Json解析
        ObjectMapper objectMapper = new ObjectMapper();
        CommonParam<CreateRoomParam> param = objectMapper.readValue(paramJson, new TypeReference<CommonParam<CreateRoomParam>>(){});
        CreateRoomParam createRoomParam = param.getContent();

        //创建Room对象
        Room newRoom = new Room(GlobalData.getNextRoomId(), createRoomParam.getCreator(), createRoomParam.getRoomName(), createRoomParam.getPassword(), 0, new ArrayList<String>(), false, false);
        newRoom.addPlayer(createRoomParam.getCreator());

        GlobalData.addRoom(newRoom);

        //构建返回参数
        CommonResp<Room> createRoomResp = new CommonResp<>("create room", true, "The room is created successfully", newRoom);

        //发送
        if(!SendHelper.sendMessageBySession(session, createRoomResp)){
            System.out.println("Error at getRooms: Respond error.\n");
        };
        return;
    }

    public void getRooms(WebSocketSession session, String paramJson)
    {
        //content = null, 无需Json解析

        //构建返回参数
        CommonResp<List<Room>> getRoomsResp = new CommonResp<>("get rooms", true, "Get rooms successfully", GlobalData.getRooms());

        //发送
        if(!SendHelper.sendMessageBySession(session, getRoomsResp)){
            System.out.println("Error at getRooms: Respond error.\n");
        };
        return;
    }

    public void getARoom(WebSocketSession session, String paramJson) throws JsonProcessingException {
        //Json解析
        ObjectMapper objectMapper = new ObjectMapper();
        CommonParam<roomIDParam> param = objectMapper.readValue(paramJson, new TypeReference<CommonParam<roomIDParam>>(){});
        long roomID = param.getContent().getRoomID();

        //获取Room
        Room room = GlobalData.getRoomByID(roomID);

        //构建返回参数
        CommonResp<Room> getARoomResp = new CommonResp<>("get a room", true, "Get a room successfully", room);

        //发送
        if(!SendHelper.sendMessageBySession(session, getARoomResp)){
            System.out.println("Error at getARoom: Respond error.\n");
        };
        return;
    }

    public void joinRoom(WebSocketSession session, String paramJson) throws JsonProcessingException
    {
        //Json解析
        ObjectMapper objectMapper = new ObjectMapper();
        CommonParam<JoinOrLeaveParam> param = objectMapper.readValue(paramJson, new TypeReference<CommonParam<JoinOrLeaveParam>>(){});
        JoinOrLeaveParam joinParam = param.getContent();

        //尝试获取房间，若获取不到则发送错误信息
        Room room = GlobalData.getRoomByID(joinParam.getRoomID());
        if(room == null)
        {
            CommonResp<Room> errorResp = new CommonResp<>("join room", false, "Wrong room id", null);
            SendHelper.sendMessageBySession(session, errorResp);
            return;
        }

        //比对密码，判断是否可加入
        if(!(room.getPassword().equals("") || joinParam.getPassword().equals(room.getPassword())))
        {
            CommonResp<Room> errorResp = new CommonResp<>("join room", false, "Wrong password", null);
            SendHelper.sendMessageBySession(session, errorResp);
            return;
        }

        //判断用户是否已加入
        if(room.getPlayers().contains(joinParam.getUser()))
        {
            CommonResp<Room> errorResp = new CommonResp<>("join room", false, "Already join", null);
            SendHelper.sendMessageBySession(session, errorResp);
            return;
        }

        //判断房间是否已满
        if(room.isFull())
        {
            CommonResp<Room> errorResp = new CommonResp<>("join room", false, "Join a full room", null);
            SendHelper.sendMessageBySession(session, errorResp);
            return;
        }

        //将该username加入房间（我修改了Room.addPlayer和Room.removePlayer的代码，现在它们会自动维护playerCount和full这两个变量）
        room.addPlayer(joinParam.getUser());
        GlobalData.setRoomByID(joinParam.getRoomID(), room);

        //广播加入消息给房间其他人
        CommonResp<Room> joinRoomBroadcast = new CommonResp<>("join room", true, "Someone joins", room);
        SendHelper.sendMessageByListExcept(room.getPlayers(), joinRoomBroadcast, joinParam.getUser());

        //加入成功信息发回自己
        CommonResp<Room> joinRoomResp = new CommonResp<>("join room", true, "Join the room successfully", room);
        SendHelper.sendMessageBySession(session, joinRoomResp);
        return;
    }

    public void leaveRoom(WebSocketSession session, String paramJson) throws JsonProcessingException {
        //Json解析
        ObjectMapper objectMapper = new ObjectMapper();
        CommonParam<JoinOrLeaveParam> param = objectMapper.readValue(paramJson, new TypeReference<CommonParam<JoinOrLeaveParam>>(){});
        JoinOrLeaveParam leaveParam = param.getContent();

        //尝试获取房间，若获取不到则发送错误信息
        Room room = GlobalData.getRoomByID(leaveParam.getRoomID());
        if(room == null)
        {
            CommonResp<Room> errorResp = new CommonResp<>("leave room", false, "Wrong room id", null);
            SendHelper.sendMessageBySession(session, errorResp);
            return;
        }

        //判断请求人是否在该房间中
        if(!(room.getPlayers().contains(leaveParam.getUser())))
        {
            CommonResp<Room> errorResp = new CommonResp<>("leave room", false, "Not in this room", null);
            SendHelper.sendMessageBySession(session, errorResp);
            return;
        }

        //退出房间，若之后房间人数为0则删除该房间
        room.removePlayer(leaveParam.getUser());
        if(room.getPlayerCount() == 0)
        {
            GlobalData.removeRoom(leaveParam.getRoomID());
            CommonResp<Room> leaveRoomResp = new CommonResp<>("leave room", true, "Leave the room successfully", null);
            SendHelper.sendMessageBySession(session, leaveRoomResp);
        }
        else
        {
            GlobalData.setRoomByID(leaveParam.getRoomID(), room);
            CommonResp<Room> leaveRoomBroadcast = new CommonResp<>("leave room", true, "Someone leaves", room);
            CommonResp<Room> leaveRoomResp = new CommonResp<>("leave room", true, "Leave the room successfully", room);
            SendHelper.sendMessageBySession(session, leaveRoomResp);
            SendHelper.sendMessageByList(room.getPlayers(), leaveRoomBroadcast);
        }
        return;
    }

}