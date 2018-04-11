package entities;

import java.lang.Integer;
import gui.Sprite;
import tools.Random;

public abstract class Enemy implements Comparable<Enemy> {
    protected Sprite sprite;
    protected Integer score;
    protected Double lastShootTime = 0.0;

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

    public Double getLastShootTime() {
        return lastShootTime;
    }

    public void updateLastShootTime(Double lastShootTime){
        this.lastShootTime += lastShootTime;
    }

    /**
     * Metodo para hacer que un enemigo dispare
     * @return Una instancia de la clase Projectile
     */
    public Projectile shoot(){
        this.lastShootTime = 0.0;
        Integer x = (int)(sprite.getPositionX() + (sprite.getWidth() / 2)); //Se obtiene la posicion en x de la bala
        Integer y = (int)(sprite.getPositionY() - sprite.getHeight()) + 40; //Se obtiene la posicion en y de la bala
        return new Projectile("resources/laser_red.png", x, y, 800);
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
