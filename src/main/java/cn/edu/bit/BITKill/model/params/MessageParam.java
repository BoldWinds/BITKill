package cn.edu.bit.BITKill.model.params;

import cn.edu.bit.BITKill.model.ChatChannel;

public class MessageParam {

    private long roomID;

    private String username;

    // 要发给谁
    private ChatChannel channel;

    private String message;

    public MessageParam() {
    }

    public MessageParam(long roomID, String username, ChatChannel channel, String message) {
        this.roomID = roomID;
        this.username = username;
        this.channel = channel;
        this.message = message;
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

    public ChatChannel getChannel() {
        return channel;
    }

    public void setChannel(ChatChannel channel) {
        this.channel = channel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
