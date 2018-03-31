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

    public MenuScreen(Stage mainStage) {
        anchorPane = new AnchorPane();
        scene = new Scene(anchorPane, width, height);
        stage = mainStage;
        stage.setScene(scene);
        createButtons();
        createBackground();
        createLogo();

        //SubScene gameScene = new SubScene();
        //MenuScreen view = new MenuScreen();
    }

    public void showMenu() {
        stage.setScene(scene);
    }

    private void createButtons(){
        Integer menu_buttons_start_x = 610;
        Integer menu_buttons_start_y = 400;
        Integer counter = 1;

        MenuButton startButton = new MenuButton("START");
        startButton.setLayoutX(menu_buttons_start_x);
        startButton.setLayoutY(menu_buttons_start_y + (counter * 100));
        anchorPane.getChildren().add(startButton);
        counter++;

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                GameScreen gameScreen = new GameScreen(stage);
            }
        });

        MenuButton watchButton = new MenuButton("WATCH");
        watchButton.setLayoutX(menu_buttons_start_x);
        watchButton.setLayoutY(menu_buttons_start_y + (counter * 100));
        anchorPane.getChildren().add(watchButton);
        counter++;

        MenuButton exitButton = new MenuButton("EXIT");
        exitButton.setLayoutX(menu_buttons_start_x);
        exitButton.setLayoutY(menu_buttons_start_y + (counter * 100));
        anchorPane.getChildren().add(exitButton);
        counter++;

        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.close();
            }
        });
    }

    private void createBackground(){
        Image backgroundImage = new Image("resources/menu_background.jpg", width, height, false, true);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        anchorPane.setBackground(new Background(background));
    }

    private void createLogo(){
        ImageView logo = new ImageView("resources/logo.png");
        logo.setLayoutX(260);
        logo.setLayoutY(20);
        anchorPane.getChildren().add(logo);
    }
}
