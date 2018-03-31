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
import javafx.scene.image.ImageView;
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

    private Player player;

    java.lang.Long lastNanoTime;

    public GameScreen(Stage mainStage) {
        player = new Player();
        enemyList = new LinkedList<>();
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
        //gcontext.setLineWidth(1);

        lastNanoTime = new java.lang.Long(System.nanoTime());

        Enemy squid = new Octopus(200, 200);
        squid.setVelocity(100, 0);

        enemyList.insertAtFirst(squid);

        Image heartImage = new Image("resources/heart.png");

        new AnimationTimer(){
            @Override
            public void handle(long currentNanoTime) {
                Double elapsedTime = (currentNanoTime - lastNanoTime) / 1000000000.0;
                lastNanoTime = currentNanoTime;

                //Se limpia el GraphicContext
                gcontext.clearRect(0, 0, width, height);

                //Se dibuja la informacion de la partida
                gcontext.fillText(player.getScore().toString(), 110, 50); //score
                Integer heart_position_x = 100;
                for (Integer i = 0; i < player.getLives(); i++) {
                    Sprite heart = new Sprite();
                    heart.setImage(heartImage);
                    heart.setPosition(heart_position_x, 4);
                    heart_position_x += 30;
                    heart.render(gcontext);

                }


                //renderizado de los enemigos
                for (Integer i = 0; i < enemyList.getSize(); i++) {
                    Enemy enemy = (Enemy) enemyList.getElement(i).getDataT();
                    enemy.getSprite().update(elapsedTime);
                    enemy.getSprite().render(gcontext);
                }

            }
        }.start();

    }
}
