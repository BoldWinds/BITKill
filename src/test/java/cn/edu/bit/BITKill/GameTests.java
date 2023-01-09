package cn.edu.bit.BITKill;

import cn.edu.bit.BITKill.model.Game;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class GameTests {

    @Test
    void assignTest(){
        List<String> players = new ArrayList<>();
        players.add("a");
        players.add("b");
        players.add("c");
        players.add("d");
        players.add("e");
        players.add("f");
        players.add("g");

        Game game = new Game(1);
        game.setPlayers(players);
        game.assignCharacters();

        for (String s : game.getPlayerCharacterMap().keySet()){
            System.out.println(s + ": " + game.getPlayerCharacterMap().get(s));
        }

    }

    @Test
    void killTest(){

    }
}
