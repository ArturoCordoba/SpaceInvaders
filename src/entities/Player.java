package entities;

import gui.Sprite;

import java.lang.Integer;
import java.lang.Double;

public class Player {
    private Sprite sprite;
    private Integer lives;
    private Integer score;
    private Double lastShootTime;

    public Player() {
        this.sprite = new Sprite("resources/playership_blue.png");
        this.sprite.setPosition(720, 800);
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

    public void setLives(Integer lives) {
        this.lives = lives;
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
