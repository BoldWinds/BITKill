package cn.edu.bit.BITKill.service;

import cn.edu.bit.BITKill.model.CommonResp;
import cn.edu.bit.BITKill.model.Game;
import cn.edu.bit.BITKill.model.GlobalData;
import cn.edu.bit.BITKill.model.Room;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    // TODO: 完成游戏流程相关服务
    private CommonResp startGame(long roomID){
        Game game = new Game();
        Room room = GlobalData.getRoomByID(roomID);

        return  new CommonResp<Game>("game start",true,"game start!",game);
    }
}
