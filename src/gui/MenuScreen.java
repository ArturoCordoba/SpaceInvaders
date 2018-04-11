package gui;

import java.lang.Integer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MenuScreen {
    private final Integer width = 1440;
    private final Integer height = 900;
    private AnchorPane anchorPane;
    private Scene scene;
    private Stage stage;

    /**
     * Constructor
     * @param mainStage Stage principal del programa
     */
    public MenuScreen(Stage mainStage) {
        anchorPane = new AnchorPane();
        scene = new Scene(anchorPane, width, height);
        stage = mainStage;
        stage.setScene(scene);
        createButtons();
        createBackground();
        createLogo();
    }

    public Scene getScene() {
        return scene;
    }

    /**
     * Metodo para crear los botones de la ventana
     */
    private void createButtons(){
        Integer menu_buttons_start_x = 610; //Posicion en x donde se empiezan a dibujar los botones
        Integer menu_buttons_start_y = 300; //Posicion en y donde se empiezan a dibujar los botones
        Integer space_between_buttons_y = 120; //Espacio en y entre los botones
        Integer counter = 1;

        BlueButton startButton = new BlueButton("START");
        startButton.setLayoutX(menu_buttons_start_x);
        startButton.setLayoutY(menu_buttons_start_y + (counter * space_between_buttons_y));
        anchorPane.getChildren().add(startButton);
        counter++;

        //Se establece la accion a realizar al ser presionado
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                new GameScreen(stage);
            }
        });

        BlueButton watchButton = new BlueButton("WATCH");
        watchButton.setLayoutX(menu_buttons_start_x);
        watchButton.setLayoutY(menu_buttons_start_y + (counter * space_between_buttons_y));
        anchorPane.getChildren().add(watchButton);
        counter++;

        //Se establece la accion a realizar al ser presionado
        watchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

            }
        });

        BlueButton exitButton = new BlueButton("EXIT");
        exitButton.setLayoutX(menu_buttons_start_x);
        exitButton.setLayoutY(menu_buttons_start_y + (counter * space_between_buttons_y));
        anchorPane.getChildren().add(exitButton);
        counter++;

        //Se establece la accion a realizar al ser presionado
        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.close();
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

    /**
     * Metodo para crear el logo en la ventana
     */
    private void createLogo(){
        ImageView logo = new ImageView("resources/logo.png");
        logo.setLayoutX(440);
        logo.setLayoutY(80);
        anchorPane.getChildren().add(logo);
    }
}
