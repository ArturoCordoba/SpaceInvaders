package entities;

import gui.Sprite;

import java.lang.Integer;

public class Block implements Comparable<Block> {
    private Sprite sprite;

    public Block(Integer x, Integer y) {
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
}