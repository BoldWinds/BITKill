package cn.edu.bit.BITKill.model.params;

import cn.edu.bit.BITKill.model.Character;

public class WinnerParam {

    private long roomID;

    private Character winner;

    public WinnerParam() {
    }

    public WinnerParam(long roomID, Character winner) {
        this.roomID = roomID;
        this.winner = winner;
    }

    public long getRoomID() {
        return roomID;
    }

    public void setRoomID(long roomID) {
        this.roomID = roomID;
    }

    public Character getWinner() {
        return winner;
    }

    public void setWinner(Character winner) {
        this.winner = winner;
    }
}
