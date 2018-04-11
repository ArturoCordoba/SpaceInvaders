package gui;

import java.lang.Integer;
import java.lang.Double;
import java.lang.String;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Sprite {
    protected Image image;
    protected Double positionX;
    protected Double positionY;
    protected Integer velocityX;
    protected Integer velocityY;
    protected Double width;
    protected Double height;

    /**
     * Constructor
     * @param filename Ruta del archivo donde se encuentra la imagen del sprite
     */
    public Sprite(String filename) {
        Image image = new Image(filename);
        this.image = image;
        width = image.getWidth();
        height = image.getHeight();
        positionX = 0.0;
        positionY = 0.0;
        velocityX = 0;
        velocityY = 0;
    }

    public Sprite(){
        positionX = 0.0;
        positionY = 0.0;
        velocityX = 0;
        velocityY = 0;
    }

    public void setImage(Image image) {
        this.image = image;
        width = image.getWidth();
        height = image.getHeight();
    }

    public Double getPositionX() {
        return positionX;
    }

    public Double getPositionY() {
        return positionY;
    }

    public void setPositionX(Double positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(Double positionY) {
        this.positionY = positionY;
    }

    public void setPosition(Integer x, Integer y) {
        positionX = Double.valueOf(x);
        positionY = Double.valueOf(y);
    }

    public Integer getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(Integer velocityX) {
        this.velocityX = velocityX;
    }

    public Integer getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(Integer velocityY) {
        this.velocityY = velocityY;
    }

    public void setVelocity(Integer x, Integer y) {
        velocityX = x;
        velocityY = y;
    }

    public void addVelocity(Integer x, Integer y) {
        velocityX += x;
        velocityY += y;
    }

    public Double getWidth() {
        return width;
    }

    public Double getHeight() {
        return height;
    }

    public void update(Double time) {
        positionX += velocityX * time;
        positionY += velocityY * time;
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(image, positionX, positionY);
    }

    public Rectangle2D getBoundary() {
        return new Rectangle2D(positionX,positionY,width,height);
    }

    public boolean intersects(Sprite sprite) {
        return sprite.getBoundary().intersects( this.getBoundary() );
    }


    @Override
    public String toString() {
        return positionX + "," + positionY + "," + velocityX + "," + velocityY;
    }
}