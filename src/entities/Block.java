package entities;

import gui.Sprite;
import tools.SplitString;

import java.lang.Integer;

public class Block implements Comparable<Block> {
    private Sprite sprite;

    public Block(Double x, Double y) {
        sprite = new Sprite("resources/block.png");
        sprite.setPosition(x, y);
    }

    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public int compareTo(Block block) {
        if((sprite.getPositionX() == block.getSprite().getPositionX()) && (sprite.getPositionY() == block.getSprite().getPositionY())){
            return 0;
        } else{
            return 1;
        }
    }

    @Override
    public String toString() {
        return SplitString.split(this.getClass().toString(), ".").getElement(0).getDataT() + "," + sprite;
    }

}
