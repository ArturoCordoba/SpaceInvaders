package gui;

import datastructures.LinkedList;
import entities.*;
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
    /**
     * Constructor
     * @param mainStage Stage principal de la aplicacion
     */
    public ObserverScreen(Stage mainStage) {
        super(mainStage, "");
        enemyList = new LinkedList<Enemy>();
        playerProjectileList = new LinkedList<Projectile>();
        enemyProjectileList = new LinkedList<Projectile>();
        blockList = new LinkedList<Block>();
        render();
    }

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

        Image heartImage = new Image("resources/heart.png");
        Image ovniImage = new Image("resources/ovni7.png");
        Image crabImage = new Image("resources/crab1.png");
        Image octopusImage = new Image("resources/octopus1.png");
        Image squidImage = new Image("resources/squid1.png");
        Image playerImage = new Image("resources/canon.png");
        Image playerBulletImage = new Image("resources/laser_blue.png");
        Image enemyBulletImage = new Image("resources/laser_red.png");
        Image blockImage = new Image("resources/block.png");

        new AnimationTimer(){

            @Override
            public void handle(long l) {
                gcontext.clearRect(0, 0, width, height);

                String info = Main.getSocket().receiveString();

                LinkedList<String> infoList = SplitString.split(info, "/");

                String levelS = (String) infoList.getElement(5).getDataT();
                gcontext.fillText(levelS, 110, 75); //level

                LinkedList<String> enemyList = SplitString.split((String)infoList.getElement(4).getDataT(), "_");
                LinkedList<String> player = SplitString.split((String)infoList.getElement(3).getDataT(), "_");
                LinkedList<String> enemyProjectileList = SplitString.split((String)infoList.getElement(2).getDataT(), "_");
                LinkedList<String> playerProjectileList = SplitString.split((String)infoList.getElement(1).getDataT(), "_");
                LinkedList<String> blockList = SplitString.split((String)infoList.getElement(0).getDataT(), "_");

                extractEnemies(enemyList, crabImage, squidImage, octopusImage, ovniImage, gcontext);

                extractPlayer(player, gcontext, playerImage, heartImage);

                extractProjectiles(enemyProjectileList, playerProjectileList, enemyBulletImage, playerBulletImage, gcontext);

                extractBlocks(blockList, blockImage, gcontext);

            }
        }.start();
    }

    private void extractEnemies(LinkedList<String> enemyList, Image crab, Image squid, Image octopus, Image ovni, GraphicsContext gcontext){
        for (Integer i = 0; i < enemyList.getSize(); i++) {
            LinkedList<String> enemy = SplitString.split((String) enemyList.getElement(i).getDataT(), ",");

            Image image;
            Double x = Double.valueOf((String) enemy.getElement(3).getDataT());
            Double y = Double.valueOf((String) enemy.getElement(2).getDataT());

            if(((String)enemy.getElement(5).getDataT()).compareTo("Ovni") == 0){
                image = ovni;
            } else if(((String)enemy.getElement(5).getDataT()).compareTo("Crab") == 0){
                image = crab;
            } else if(((String)enemy.getElement(5).getDataT()).compareTo("Octopus") == 0){
                image = octopus;
            } else {
                image = squid;
            }

            gcontext.drawImage(image, x, y);
        }
    }

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
