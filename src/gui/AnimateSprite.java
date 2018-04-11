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

    /**
     * Constructor
     * @param filename Ruta sin el numero ni la extension de las imagenes que componen la animacion
     * @param frames Cantidad de imagenes que componen la animacion
     * @param extension Extension de las imagenes
     * @param duration Duracion de cada frame
     */
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

    /**
     * Metodo para calcular el frame que corresponde segun el tiempo transcurrido
     * @param time Tiempo desde el ultimo frame
     * @return Image correspondiente
     */
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
