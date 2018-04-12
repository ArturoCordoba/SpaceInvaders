package entities;

import gui.AnimateSprite;
import tools.Random;

public class Ovni extends Enemy {
    private boolean showedUp;
    private Double lastChangeDirection;

    public Ovni(Double x, Double y){
        sprite = new AnimateSprite("resources/ovni", 19, ".png", 0.02);
        sprite.setPosition(x, y);
        score = Random.getRamdomNumber(100, 300);
        showedUp = false;
        lastChangeDirection = 0.0;
    }

    public boolean isShowedUp() {
        return showedUp;
    }

    public void setShowedUp(boolean showedUp) {
        this.showedUp = showedUp;
    }

    public Double getLastChangeDirection() {
        return lastChangeDirection;
    }

    public void setLastChangeDirection(Double lastChangeDirection) {
        this.lastChangeDirection = lastChangeDirection;
    }

    public void addLastChangeDirection(Double elapsedTime) {
        this.lastChangeDirection += elapsedTime;
    }
}
