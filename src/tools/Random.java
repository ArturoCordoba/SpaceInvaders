package tools;

/**
 * Clase que se encarga de obtener un numero random en un intervalo dado.
 */
public class Random {
    public static float getRandomNumber(float from, float to){
        return from + (int)(Math.random() * ((to - from) + 1));
    }

    public static int getRamdomNumber(int from, int to){
        return from + (int)(Math.random() * ((to - from) + 1));
    }
}