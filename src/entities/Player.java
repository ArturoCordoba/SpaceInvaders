package entities;

import java.lang.Integer;

public class Player {
    private Integer lives;
    private Integer score;

    public Player() {
        this.lives = 3;
        this.score = 0;
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

    public void setScore(Integer score) {
        this.score = score;
    }
}
