package entities;

import gui.AnimateSprite;

public class Octopus extends Enemy {
    public Octopus(Integer x, Integer y){
        sprite = new AnimateSprite("resources/octopus", 2, ".png", 0.5);
        sprite.setPosition(x, y);
        score = 40;
    }
}
