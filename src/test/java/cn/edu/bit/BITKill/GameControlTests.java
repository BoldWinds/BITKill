package cn.edu.bit.BITKill;

import cn.edu.bit.BITKill.model.GameControl;
import cn.edu.bit.BITKill.util.PrintHelper;
import org.junit.jupiter.api.Test;

public class GameControlTests {

    @Test
    void testVote(){
        GameControl gameControl = new GameControl(1);

        gameControl.vote("a","b",1);
        gameControl.vote("b","a",1);
        gameControl.vote("c","a",1);
        gameControl.vote("d","b",1);
        gameControl.vote("e","c",1);
        gameControl.vote("f","d",1);
        gameControl.vote("g","c",1);

        System.out.println(PrintHelper.map2String(gameControl.getVoteMap()));
        System.out.println(gameControl.getVoteResult());
        System.out.println(gameControl.isTie());
    }


}
