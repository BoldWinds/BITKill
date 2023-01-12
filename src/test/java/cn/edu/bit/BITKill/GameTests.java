package cn.edu.bit.BITKill;

import cn.edu.bit.BITKill.model.Character;
import cn.edu.bit.BITKill.model.Game;
import cn.edu.bit.BITKill.util.PrintHelper;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class GameTests {

    // 测试身份分配
    @Test
    void assignTest(){
        Game game = getGame();
        game.assignCharacters();
        System.out.println(PrintHelper.map2String(game.getPlayerCharacterMap()));
    }

    @Test
    void getCharacterTest(){
        Game game = getGame();

        printPlayerCharacter(game);

        game.playerDie("a");

        printAlivePlayerCharacter(game);
        printPlayerCharacter(game);
    }

    @Test
    void endTest(){
        Game game = getGame();

        printPlayerCharacter(game);
        Character c;

        game.playerDie("a");
        if ((c = game.judgeEnd()) != Character.UNDEF){
            System.out.println("Winner: "+c);
            printAlivePlayerCharacter(game);
            return;
        }

        game.playerDie("b");
        if ((c = game.judgeEnd()) != Character.UNDEF){
            System.out.println("Winner: "+c);
            printAlivePlayerCharacter(game);
            return;
        }

        game.playerDie("c");
        if ((c = game.judgeEnd()) != Character.UNDEF){
            System.out.println("Winner: "+c);
            printAlivePlayerCharacter(game);
            return;
        }

        game.playerDie("d");
        if ((c = game.judgeEnd()) != Character.UNDEF){
            System.out.println("Winner: "+c);
            printAlivePlayerCharacter(game);
            return;
        }


    }

    @Test
    void killTest(){
        Date date = new Date();
        System.out.println(date.getTime());
    }

    private Game getGame(){
        List<String> players = new ArrayList<>();
        HashMap<String, Boolean> playerStateMap = new HashMap<>();
        players.add("a");
        playerStateMap.put("a",true);
        players.add("b");
        playerStateMap.put("b",true);
        players.add("c");
        playerStateMap.put("c",true);
        players.add("d");
        playerStateMap.put("d",true);
        players.add("e");
        playerStateMap.put("e",true);
        players.add("f");
        playerStateMap.put("f",true);
        players.add("g");
        playerStateMap.put("g",true);

        Game game = new Game(1);
        game.setPlayers(players);
        game.setPlayerStateMap(playerStateMap);
        game.assignCharacters();

        return game;
    }

    void printPlayerCharacter(Game game){
        System.out.println("witch: " + PrintHelper.list2String(game.getPlayersByCharacter(Character.WITCH)));
        System.out.println("wolf: " + PrintHelper.list2String(game.getPlayersByCharacter(Character.WOLF)));
        System.out.println("village: " + PrintHelper.list2String(game.getPlayersByCharacter(Character.VILLAGE)));
        System.out.println("prophet: " + PrintHelper.list2String(game.getPlayersByCharacter(Character.PROPHET)));
    }

    void printAlivePlayerCharacter(Game game){
        System.out.println("witch: " + PrintHelper.list2String(game.getAlivePlayersByCharacter(Character.WITCH)));
        System.out.println("wolf: " + PrintHelper.list2String(game.getAlivePlayersByCharacter(Character.WOLF)));
        System.out.println("village: " + PrintHelper.list2String(game.getAlivePlayersByCharacter(Character.VILLAGE)));
        System.out.println("prophet: " + PrintHelper.list2String(game.getAlivePlayersByCharacter(Character.PROPHET)));
    }
}
