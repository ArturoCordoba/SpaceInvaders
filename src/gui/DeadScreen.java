package gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.Integer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sample.Main;

public class DeadScreen {
    private final Integer width = 1440;
    private final Integer height = 900;
    private AnchorPane anchorPane;
    private Scene scene;
    private Stage stage;

    /**
     * Constructor
     * @param mainStage Stage principal de la aplicacion
     * @param score Score obtenido por el usuario antes de morir
     */
    public DeadScreen(Stage mainStage, Integer score) {
        anchorPane = new AnchorPane();
        scene = new Scene(anchorPane, width, height);
        stage = mainStage;
        mainStage.setScene(scene);
        createBackground();
        createLabels(score);
        createButtons();
    }

    /**
     * Metodo para crear los labels de la ventana
     * @param score Score obtenido por el usuario
     */
    private void createLabels(Integer score){
        Integer labels_start_x = 380; //Posicion en x donde inician los labels
        Integer labels_start_y = 250; //Posicion en y donde inician los labels
        Integer space_between_labels_y = 110; //Espacio entre los labels
        Integer counter = 0;

        Label gameoverLabel = new Label("GAME OVER");
        gameoverLabel.setTextFill(Color.WHITE);
        gameoverLabel.setLayoutX(labels_start_x);
        gameoverLabel.setLayoutY(labels_start_y + (counter * space_between_labels_y));
        counter++;

        Label scoreLabel = new Label("SCORE: " + score.toString());
        scoreLabel.setTextFill(Color.WHITE);
        scoreLabel.setLayoutX(labels_start_x + 210);
        scoreLabel.setLayoutY(labels_start_y + (counter * space_between_labels_y));
        counter++;

        //Se pone el Font a cada label
        try {
            gameoverLabel.setFont(Font.loadFont(new FileInputStream("src/resources/kenvector_future.ttf"), 100));
            scoreLabel.setFont(Font.loadFont(new FileInputStream("src/resources/kenvector_future.ttf"), 30));
        } catch (FileNotFoundException e) {
            gameoverLabel.setFont(Font.font("Verdana", 50));
            scoreLabel.setFont(Font.font("Verdana", 50));
        }

        //Se agregan los labels al anchorPane
        anchorPane.getChildren().add(gameoverLabel);
        anchorPane.getChildren().add(scoreLabel);
    }

    /**
     * Metodo para crear los botones en la ventana
     */
    private void createButtons(){
        Integer buttons_start_x = 620; //Posicion x donde se crean los botones
        Integer buttons_start_y = 430; //Posicion y donde se crea el primer boton
        Integer space_between_buttons_y = 120; //Espacio entre los botones
        Integer counter = 0; //Variable que lleva la cuenta de botones creados

        //Se crea el boton para volver a jugar
        BlueButton retryButton = new BlueButton("RETRY");
        retryButton.setLayoutX(buttons_start_x);
        retryButton.setLayoutY(buttons_start_y + (counter * space_between_buttons_y));
        anchorPane.getChildren().add(retryButton);
        counter++;

        //Se le asigna la accion
        retryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                new GameScreen(stage);
            }
        });

        //Se crea el boton para regresar al menu
        BlueButton menuButton = new BlueButton("MENU");
        menuButton.setLayoutX(buttons_start_x);
        menuButton.setLayoutY(buttons_start_y + (counter * space_between_buttons_y));
        anchorPane.getChildren().add(menuButton);
        counter++;

        //Se le asigna la accion a realizar al ser clickeado
        menuButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.setScene(Main.getMenuScreen().getScene());
            }
        });
    }

    /**
     * Metodo para crear el fondo de la ventana
     */
    private void createBackground(){
        Image backgroundImage = new Image("resources/menu_background.jpg", width, height, false, true);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        anchorPane.setBackground(new Background(background));
    }

}
