package entities;

import gui.AnimateSprite;

public class Crab extends Enemy {
    public Crab(Double x, Double y) {
        sprite = new AnimateSprite("resources/crab", 2, ".png", 0.5);
        sprite.setPosition(x, y);
        score = 20;
    }
}
