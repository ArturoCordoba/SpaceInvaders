package gui;

import java.lang.Integer;
import java.lang.String;
import java.lang.Double;

import javafx.scene.image.Image;

public class AnimateSprite extends Sprite{
    private Image[] framesList;
    private Double duration;
    private Double elapsedTime;
    private Integer index;

    public AnimateSprite(String filename, Integer frames, String extension, Double duration) {
        super(filename + "1" + extension);
        framesList = new Image[frames];
        for (Integer i = 1; i <= frames; i++) {
            framesList[i - 1] = new Image(filename + i + extension);
        }
        this.duration = duration;
        elapsedTime = 0.0;
        index = 0;
    }

    public Image getFrame(double time) {
        elapsedTime += time;
        if(elapsedTime >= duration) {
            elapsedTime = 0.0;
            index += 1;
            if(index >= framesList.length)
                index = 0;
        }
        return framesList[index];
    }

    @Override
    public void update(Double time){
        positionX += velocityX * time;
        positionY += velocityY * time;
        image = getFrame(time);
    }
}
