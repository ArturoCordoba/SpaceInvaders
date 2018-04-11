package entities;

import gui.Sprite;
import javafx.scene.image.Image;

import java.lang.Integer;
import java.lang.Double;

public class Player {
    private Sprite sprite;
    private Integer lives;
    private Integer score;
    private Double lastShootTime;

    public Player() {
        this.sprite = new Sprite("resources/canon.png");
        this.sprite.setPosition(720, 850);
        this.lives = 3;
        this.score = 0;
        this.lastShootTime = 0.0;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Integer getLives() {
        return lives;
    }

    public void addLive() {
        this.lives += 1;
    }

    /**
     * Metodo para restar una vida
     */
    public void decreaseLive(){
        this.lives -= 1;

        if(this.lives <= 0){
            sprite.setImage(new Image("resources/deadcanon.png"));
        }
    }

    public Integer getScore() {
        return score;
    }

    public void addScore(Integer score) {
        this.score += score;
    }

    public void addShootTime(Double time){
        lastShootTime += time;
    }

    public void setLastShootTime(Double lastShootTime) {
        this.lastShootTime = lastShootTime;
    }

    public Double getLastShootTime() {
        return lastShootTime;
    }
}
