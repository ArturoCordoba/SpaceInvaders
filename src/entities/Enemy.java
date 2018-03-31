package entities;

import java.lang.Integer;
import gui.Sprite;

public abstract class Enemy implements Comparable<Enemy> {
    protected Sprite sprite;
    protected Integer score;

    public Enemy() {

    }

    public Sprite getSprite() {
        return sprite;
    }

    public Integer getScore() {
        return score;
    }

    public void setVelocity(Integer xVelocity, Integer yVelocity){
        sprite.setVelocity(xVelocity, yVelocity);
    }

    @Override
    public int compareTo(Enemy enemy) {
        if((sprite.getPositionX() == enemy.getSprite().getPositionX()) && (sprite.getPositionY() == enemy.getSprite().getPositionY())){
            return 0;
        } else{
            return 1;
        }
    }
}
