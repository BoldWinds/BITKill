package cn.edu.bit.BITKill.model.params;

import cn.edu.bit.BITKill.model.Character;

public class ProphetResult {

    private String target;

    private Character character;

    public ProphetResult() {
    }

    public ProphetResult(String target, Character character) {
        this.target = target;
        this.character = character;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }
}
