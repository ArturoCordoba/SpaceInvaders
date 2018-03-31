package gui;

import java.lang.Integer;
import java.lang.String;

import datastructures.LinkedList;
import entities.*;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GameScreen {

    private final Integer width = 1440;
    private final Integer height = 900;
    private AnchorPane anchorPane;
    private Scene scene;
    private Stage stage;

    private LinkedList<Enemy> enemyList;
    private LinkedList<Projectile> playerProjectileList;
    private LinkedList<Projectile> enemyProjectileList;

    private Player player;

    java.lang.Long lastNanoTime;

    public GameScreen(Stage mainStage) {
        player = new Player();
        enemyList = new LinkedList<Enemy>();
        playerProjectileList = new LinkedList<Projectile>();
        enemyProjectileList = new LinkedList<Projectile>();
        anchorPane = new AnchorPane();
        scene = new Scene(anchorPane, width, height);
        stage = mainStage;
        stage.setScene(scene);
        createBackground();
        createLabels();

        render();
    }

    private void createLabels(){
        Integer labels_start_x = 4;
        Integer labels_start_y = 4;
        Integer counter = 0;

        Label livesLabel = new Label("LIVES: ");
        livesLabel.setTextFill(Color.WHITE);
        livesLabel.setLayoutX(labels_start_x);
        livesLabel.setLayoutY(labels_start_y + (counter * 25));
        counter++;

        Label scoreLabel = new Label("SCORE: ");
        scoreLabel.setTextFill(Color.WHITE);
        scoreLabel.setLayoutX(labels_start_x);
        scoreLabel.setLayoutY(labels_start_y + (counter * 25));
        counter++;

        try {
            scoreLabel.setFont(Font.loadFont(new FileInputStream("src/resources/kenvector_future.ttf"), 23));
            livesLabel.setFont(Font.loadFont(new FileInputStream("src/resources/kenvector_future.ttf"), 23));

        } catch (FileNotFoundException e) {
            scoreLabel.setFont(Font.font("Verdana",23));
            livesLabel.setFont(Font.font("Verdana",23));

        }

        anchorPane.getChildren().add(scoreLabel);
        anchorPane.getChildren().add(livesLabel);
    }

    private void createBackground(){
        Image backgroundImage = new Image("resources/game_background.jpg", width, height, false, true);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        anchorPane.setBackground(new Background(background));

    }

    private void render(){
        Canvas canvas = new Canvas(width, height);
        anchorPane.getChildren().add(canvas);

        LinkedList<String> input = new LinkedList<>();

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                String code = keyEvent.getCode().toString();
                if(!input.contains(code)){
                    input.insertAtEnd(code);
                }
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                String code = keyEvent.getCode().toString();
                input.deleteElement(code);
            }
        });

        GraphicsContext gcontext = canvas.getGraphicsContext2D();
        Font font;
        try {
             font = Font.loadFont(new FileInputStream("src/resources/kenvector_future.ttf"), 23);
        } catch (FileNotFoundException e) {
            font = Font.font("Verdana",23);
        }
        gcontext.setFont(font);
        gcontext.setFill(Color.WHITE);

        lastNanoTime = new java.lang.Long(System.nanoTime());

        Enemy squid = new Octopus(200, 200);
        //squid.setVelocity(100, 0);

        enemyList.insertAtFirst(squid);

        Image heartImage = new Image("resources/heart.png");
        Sprite playerShip = player.getSprite();

        new AnimationTimer(){
            @Override
            public void handle(long currentNanoTime) {
                Double elapsedTime = (currentNanoTime - lastNanoTime) / 1000000000.0;
                lastNanoTime = currentNanoTime;
                player.addShootTime(elapsedTime); //Se añade el tiempo transcurrido al jugador

                //Se limpia el GraphicContext
                gcontext.clearRect(0, 0, width, height);

                //Control de la nave del jugador
                player.getSprite().setVelocity(0, 0);
                if(input.contains("A") && playerShip.getPositionX() > 0)
                    playerShip.addVelocity(-300, 0);
                if (input.contains("D")&& playerShip.getPositionX() < (width - playerShip.getWidth()))
                    playerShip.addVelocity(300,0);
                if (input.contains("SPACE") && (player.getLastShootTime() > 0.5)) {
                    player.setLastShootTime(0.0);
                    Integer x = (int)(playerShip.getPositionX() + (playerShip.getWidth() / 2));
                    Integer y = (int)(playerShip.getPositionY() - playerShip.getHeight()) + 40;
                    Projectile projectile = new Projectile("resources/laser_blue.png", x, y, -800);
                    playerProjectileList.insertAtFirst(projectile);
                }

                //Se dibuja la informacion de la partida
                gcontext.fillText(player.getScore().toString(), 110, 50); //score
                Integer heart_position_x = 100; //vidas
                for (Integer i = 0; i < player.getLives(); i++) {
                    Sprite heart = new Sprite();
                    heart.setImage(heartImage);
                    heart.setPosition(heart_position_x, 4);
                    heart_position_x += 30;
                    heart.render(gcontext);

                }

                //Colisiones
                LinkedList<Enemy> deadEnemies = new LinkedList<>(); //Lista que contiene los enemigos que deben ser borrados
                LinkedList<Projectile> destroyedPlayerProjectiles = new LinkedList<>(); //Lista que contiene a los proyectiles que deben ser eliminados

                //Colision de una bala del jugador contra la de un enemigo
                for (Integer i = 0; i < playerProjectileList.getSize(); i++) {
                    Projectile projectile = (Projectile) playerProjectileList.getElement(i).getDataT();

                    for (Integer j = 0; j < enemyList.getSize(); j++) {
                        Enemy enemy = (Enemy) enemyList.getElement(j).getDataT();

                        if(projectile.getSprite().intersects(enemy.getSprite())){
                            deadEnemies.insertAtFirst(enemy); //Se inserta al enemigo en la lista de enemigos muertos
                            destroyedPlayerProjectiles.insertAtFirst(projectile); //Se inserta el proyectil en la lista de proyectiles destruidos
                            player.addScore(enemy.getScore()); //Se actualiza el score del jugador
                        }
                    }
                }

                //Eliminacion de las entidades que colisionaron
                for (Integer i = 0; i < deadEnemies.getSize(); i++) {
                    enemyList.deleteElement((Enemy) deadEnemies.getElement(i).getDataT());
                }

                for (Integer i = 0; i < destroyedPlayerProjectiles.getSize(); i++) {
                    playerProjectileList.deleteElement((Projectile) destroyedPlayerProjectiles.getElement(i).getDataT());
                }


                //renderizado de las entidades
                for (Integer i = 0; i < playerProjectileList.getSize(); i++) {
                    Projectile projectile = (Projectile) playerProjectileList.getElement(i).getDataT();
                    projectile.getSprite().update(elapsedTime);
                    projectile.getSprite().render(gcontext);
                }

                for (Integer i = 0; i < enemyList.getSize(); i++) {
                    Enemy enemy = (Enemy) enemyList.getElement(i).getDataT();
                    enemy.getSprite().update(elapsedTime);
                    enemy.getSprite().render(gcontext);
                }


                playerShip.update(elapsedTime);
                playerShip.render(gcontext);
            }
        }.start();

    }
}
