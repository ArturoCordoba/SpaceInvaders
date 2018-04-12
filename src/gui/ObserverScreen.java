package gui;

import datastructures.LinkedList;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sample.Main;
import tools.SplitString;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SuppressWarnings("Duplicates")
public class ObserverScreen extends GameScreen{
    private Image ovniImage;
    private Image crabImage;
    private Image octopusImage;
    private Image squidImage;

    private Image[] ovniFrames;
    private Image[] crabFrames;
    private Image[] octopusFrames;
    private Image[] squidFrames;

    private Double ovniElapsedTime = 0.0;
    private Double crabElapsedTime = 0.0;
    private Double octopusElapsedTime = 0.0;
    private Double squidElapsedTime = 0.0;

    private Integer ovniIndex = 0;
    private Integer crabIndex = 0;
    private Integer octopusIndex = 0;
    private Integer squidIndex = 0;

    /**
     * Constructor
     * @param mainStage Stage principal de la aplicacion
     */
    public ObserverScreen(Stage mainStage) {
        super(mainStage, "");
        loadFrames();
        render();
    }

    /**
     * Metodo para cargar todos los frames de las animaciones
     */
    private void loadFrames(){
        //Se cargan los frames del ovni
        ovniFrames = new Image[19];
        for (Integer i = 1; i <= 19; i++) {
            ovniFrames[i - 1] = new Image("resources/ovni" + i + ".png");
        }

        //Se cargan los frames del crab
        crabFrames = new Image[2];
        for (Integer i = 1; i <= 2; i++) {
            crabFrames[i - 1] = new Image("resources/crab" + i + ".png");
        }

        //Se cargan los frames del octopus
        octopusFrames = new Image[2];
        for (Integer i = 1; i <= 2; i++) {
            octopusFrames[i - 1] = new Image("resources/octopus" + i + ".png");
        }

        //Se cargan los frames del squid
        squidFrames = new Image[2];
        for (Integer i = 1; i <= 2; i++) {
            squidFrames[i - 1] = new Image("resources/squid" + i + ".png");
        }
    }

    /**
     * Metodo para actualizar el Image de cada animacion
     * @param time Tiempo transcurrido desde el ultimo frame
     */
    private void updateFrames(Double time){
        //Animacion del ovni
        ovniElapsedTime += time;
        if(ovniElapsedTime >= 0.02) {
            ovniElapsedTime = 0.0;
            ovniIndex += 1;
            if(ovniIndex >= ovniFrames.length)
                ovniIndex = 0;
        }
        ovniImage = ovniFrames[ovniIndex];

        //Animacion del crab
        crabElapsedTime += time;
        if(crabElapsedTime >= 0.5) {
            crabElapsedTime = 0.0;
            crabIndex += 1;
            if(crabIndex >= crabFrames.length)
                crabIndex = 0;
        }
        crabImage = crabFrames[crabIndex];

        //Animacion del octopus
        octopusElapsedTime += time;
        if(octopusElapsedTime >= 0.5) {
            octopusElapsedTime = 0.0;
            octopusIndex += 1;
            if(octopusIndex >= octopusFrames.length)
                octopusIndex = 0;
        }
        octopusImage = octopusFrames[octopusIndex];

        //Animacion del squid
        squidElapsedTime += time;
        if(squidElapsedTime >= 0.5) {
            squidElapsedTime = 0.0;
            squidIndex += 1;
            if(squidIndex >= squidFrames.length)
                squidIndex = 0;
        }
        squidImage = squidFrames[squidIndex];
    }

    /**
     * Metodo para renderizar el contenido enviado por el servidor
     */
    @Override
    protected void render(){
        Canvas canvas = new Canvas(width, height);
        anchorPane.getChildren().add(canvas);
        GraphicsContext gcontext = canvas.getGraphicsContext2D();
        Font font;

        //Se carga el tipo de letra para los textos
        try {
            font = Font.loadFont(new FileInputStream("src/resources/kenvector_future.ttf"), 23);
        } catch (FileNotFoundException e) {
            font = Font.font("Verdana",23);
        }

        gcontext.setFont(font);
        gcontext.setFill(Color.WHITE);

        //Se cargan las imagenes de los corazones, el jugador, las balas, y los bloques
        Image heartImage = new Image("resources/heart.png");
        Image playerImage = new Image("resources/canon.png");
        Image playerBulletImage = new Image("resources/laser_blue.png");
        Image enemyBulletImage = new Image("resources/laser_red.png");
        Image blockImage = new Image("resources/block.png");

        lastNanoTime = new Long(System.nanoTime());

        new AnimationTimer(){

            @Override
            public void handle(long currentNanoTime) {
                Double elapsedTime = (currentNanoTime - lastNanoTime) / 1000000000.0;
                lastNanoTime = currentNanoTime;

                updateFrames(elapsedTime);

                //Se limpia el graphic context
                gcontext.clearRect(0, 0, width, height);

                //Se recibe el string proveniente del servidor
                String info = Main.getSocket().receiveString();

                //Se dividen los distintos elementos recibidos
                LinkedList<String> infoList = SplitString.split(info, "/");

                //Se obtiene el nivel
                String levelS = (String) infoList.getElement(5).getDataT();
                gcontext.fillText(levelS, 110, 75); //level

                //Se obtienen las diferentes listas de entidades
                LinkedList<String> enemyList = SplitString.split((String)infoList.getElement(4).getDataT(), "_");
                LinkedList<String> player = SplitString.split((String)infoList.getElement(3).getDataT(), "_");
                LinkedList<String> enemyProjectileList = SplitString.split((String)infoList.getElement(2).getDataT(), "_");
                LinkedList<String> playerProjectileList = SplitString.split((String)infoList.getElement(1).getDataT(), "_");
                LinkedList<String> blockList = SplitString.split((String)infoList.getElement(0).getDataT(), "_");

                //Se renderizan las entidades obtenidas
                extractEnemies(enemyList, gcontext);
                extractPlayer(player, gcontext, playerImage, heartImage);
                extractProjectiles(enemyProjectileList, playerProjectileList, enemyBulletImage, playerBulletImage, gcontext);
                extractBlocks(blockList, blockImage, gcontext);

            }
        }.start();
    }

    /**
     * Metodo para extraer los enemigos del string recibido
     * @param enemyList Lista con los string de los enemigos
     * @param gcontext GraphicsContext en el que se dibuja
     */
    private void extractEnemies(LinkedList<String> enemyList, GraphicsContext gcontext){
        for (Integer i = 0; i < enemyList.getSize(); i++) {
            LinkedList<String> enemy = SplitString.split((String) enemyList.getElement(i).getDataT(), ",");

            Image image;
            Double x = Double.valueOf((String) enemy.getElement(3).getDataT());
            Double y = Double.valueOf((String) enemy.getElement(2).getDataT());

            if(((String)enemy.getElement(5).getDataT()).compareTo("Ovni") == 0){
                image = ovniImage;
            } else if(((String)enemy.getElement(5).getDataT()).compareTo("Crab") == 0){
                image = crabImage;
            } else if(((String)enemy.getElement(5).getDataT()).compareTo("Octopus") == 0){
                image = octopusImage;
            } else {
                image = squidImage;
            }

            gcontext.drawImage(image, x, y);
        }
    }

    /**
     * Metodo para extraer la informacion del jugador
     * @param playerList Informacion de jugador
     * @param gcontext GraphicsContext en el que se dibuja
     * @param playerImage Imagen de la nave del jugador
     * @param heartImage Imagen de los corazones de vida
     */
    private void extractPlayer(LinkedList<String> playerList, GraphicsContext gcontext, Image playerImage, Image heartImage){
        LinkedList<String> player = SplitString.split((String) playerList.getElement(0).getDataT(), ",");

        Double x = Double.valueOf((String) player.getElement(3).getDataT());
        Double y = Double.valueOf((String) player.getElement(2).getDataT());
        gcontext.drawImage(playerImage, x, y);

        Integer lives = Integer.valueOf((String) player.getElement(5).getDataT());

        Double heart_position_x = 100.0; //vidas
        for (Integer i = 0; i < lives; i++) {
            Sprite heart = new Sprite();
            heart.setImage(heartImage);
            heart.setPosition(heart_position_x, 4.0);
            heart_position_x += 30;
            heart.render(gcontext);
        }

        String score = (String) player.getElement(4).getDataT();
        gcontext.fillText(score, 110, 50); //score
    }

    /**
     * Metodo para extraer y renderizar los proyectiles
     * @param enemy Balas de los enemigos
     * @param player Balas de los jugadores
     * @param enemyBullet Image de las balas de los enemigos
     * @param playerBullet Image de las balas del jugador
     * @param gcontext GraphicsContext en el que se dibuja
     */
    private void extractProjectiles(LinkedList<String> enemy, LinkedList<String> player, Image enemyBullet, Image playerBullet, GraphicsContext gcontext){
        if (((String) enemy.getElement(0).getDataT()).compareTo("") != 0) {
            for (Integer i = 0; i < enemy.getSize(); i++) {

                LinkedList<String> dataEnemy = SplitString.split((String) enemy.getElement(i).getDataT(), ",");

                Double x = Double.valueOf((String) dataEnemy.getElement(3).getDataT());
                Double y = Double.valueOf((String) dataEnemy.getElement(2).getDataT());

                gcontext.drawImage(enemyBullet, x, y);
            }
        }

        if (((String) player.getElement(0).getDataT()).compareTo("") != 0) {
            for (Integer i = 0; i < player.getSize(); i++) {
                LinkedList<String> dataPlayer = SplitString.split((String) player.getElement(i).getDataT(), ",");

                Double x = Double.valueOf((String) dataPlayer.getElement(3).getDataT());
                Double y = Double.valueOf((String) dataPlayer.getElement(2).getDataT());

                gcontext.drawImage(playerBullet, x, y);
            }
        }
    }

    /**
     * Metodo para extraer y renderizar los bloques
     * @param blocks Informacion de los bloques
     * @param block Image de los bloques
     * @param gcontext GraphicsContext en el que se dibuja
     */
    private void extractBlocks(LinkedList<String> blocks, Image block, GraphicsContext gcontext){
        if (((String) blocks.getElement(0).getDataT()).compareTo("") != 0) {
            for (Integer i = 0; i < blocks.getSize(); i++) {
                LinkedList<String> data = SplitString.split((String) blocks.getElement(i).getDataT(), ",");

                Double x = Double.valueOf((String) data.getElement(3).getDataT());
                Double y = Double.valueOf((String) data.getElement(2).getDataT());

                gcontext.drawImage(block, x, y);
            }
        }
    }

}
