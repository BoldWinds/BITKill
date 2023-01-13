package cn.edu.bit.BITKill;

import cn.edu.bit.BITKill.model.Game;
import cn.edu.bit.BITKill.model.GameControl;
import cn.edu.bit.BITKill.model.GlobalData;
import cn.edu.bit.BITKill.model.Room;
import cn.edu.bit.BITKill.util.PrintHelper;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

public class GlobalDataTests {

    @Test
    void testRooms(){
        Room room = new Room();
        for (int i = 0;i<10;i++){
            room = new Room(GlobalData.getNextRoomId());
            GlobalData.addRoom(room);
        }
        System.out.println(GlobalData.getNextRoomId());

        List<Room> rooms = GlobalData.getRooms();
        System.out.println(rooms.size()+ " , " + PrintHelper.list2String(rooms));

        GlobalData.removeRoom(room.roomID);
        rooms = GlobalData.getRooms();
        System.out.println(rooms.size()+ " , " + PrintHelper.list2String(rooms));

        room = GlobalData.getARandomRoom();
        System.out.println(room.toString());
    }

    @Test
    void testGames(){
        Game game = new Game(1);
        GameControl gameControl = new GameControl(1);

        HashMap<Long,Game> games;
        HashMap<Long,GameControl> gameControls;

        GlobalData.addGame(game,gameControl);
        games = GlobalData.getGames();
        gameControls = GlobalData.getGameControls();
        System.out.println(games.size() + " , " + PrintHelper.map2String(games));
        System.out.println(gameControls.size() + " , " + PrintHelper.map2String(gameControls));

        GlobalData.removeGame(1);
        games = GlobalData.getGames();
        gameControls = GlobalData.getGameControls();
        System.out.println(games.size());
        System.out.println(gameControls.size());

    }

}
