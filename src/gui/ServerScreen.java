package gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.Integer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sample.Main;

public class ServerScreen {
    private final Integer width = 1440;
    private final Integer height = 900;
    private AnchorPane anchorPane;
    private Scene scene;
    private Stage stage;

    private TextField serverAddressTF;
    private TextField serverPortTF;

    /**
     * Constructor
     * @param mainStage Stage principal del programa
     */
    public ServerScreen(Stage mainStage) {
        anchorPane = new AnchorPane();
        scene = new Scene(anchorPane, width, height);
        stage = mainStage;
        stage.setScene(scene);
        createLabels();
        createTextFields();
        createButtons();
        createBackground();
    }

    private void createLabels(){
        Integer labels_start_x = 380; //Posicion en x donde inician los labels
        Integer labels_start_y = 230; //Posicion en y donde inician los labels
        Integer space_between_labels_y = 110; //Espacio entre los labels
        Integer counter = 0;

        Label serverAddressLabel = new Label("SERVER IP:");
        serverAddressLabel.setTextFill(Color.WHITE);
        serverAddressLabel.setLayoutX(labels_start_x + 55);
        serverAddressLabel.setLayoutY(labels_start_y + (counter * space_between_labels_y));
        counter++;

        Label serverPortLabel = new Label("SERVER PORT:");
        serverPortLabel.setTextFill(Color.WHITE);
        serverPortLabel.setLayoutX(labels_start_x);
        serverPortLabel.setLayoutY(labels_start_y + (counter * space_between_labels_y));
        counter++;

        //Se pone el Font a cada label
        try {
            serverAddressLabel.setFont(Font.loadFont(new FileInputStream("src/resources/kenvector_future.ttf"), 23));
            serverPortLabel.setFont(Font.loadFont(new FileInputStream("src/resources/kenvector_future.ttf"), 23));
        } catch (FileNotFoundException e) {
            serverAddressLabel.setFont(Font.font("Verdana", 50));
            serverPortLabel.setFont(Font.font("Verdana", 50));
        }

        //Se agregan los labels al anchorPane
        anchorPane.getChildren().add(serverAddressLabel);
        anchorPane.getChildren().add(serverPortLabel);
    }
    
    private void createTextFields(){
        Integer textFields_start_x = 630; //Posicion en x donde inician los TextFields
        Integer textFields_start_y = 230; //Posicion en y donde inician los TextFields
        Integer space_between_textFields_y = 110; //Espacio entre los TextFields
        Integer counter = 0;

        serverAddressTF = new TextField();
        serverAddressTF.setLayoutX(textFields_start_x);
        serverAddressTF.setLayoutY(textFields_start_y + (counter * space_between_textFields_y));
        counter++;

        serverPortTF = new TextField();
        serverPortTF.setLayoutX(textFields_start_x);
        serverPortTF.setLayoutY(textFields_start_y + (counter * space_between_textFields_y));
        counter++;

        anchorPane.getChildren().add(serverAddressTF);
        anchorPane.getChildren().add(serverPortTF);
    }

    /**
     * Metodo para crear los botones en la ventana
     */
    @SuppressWarnings("Duplicates")
    private void createButtons(){
        Integer buttons_start_x = 620; //Posicion x donde se crean los botones
        Integer buttons_start_y = 420; //Posicion y donde se crea el primer boton
        Integer space_between_buttons_y = 120; //Espacio entre los botones
        Integer counter = 0; //Variable que lleva la cuenta de botones creados

        //Se crea el boton para volver a jugar
        BlueButton nextButton = new BlueButton("NEXT");
        nextButton.setLayoutX(buttons_start_x);
        nextButton.setLayoutY(buttons_start_y + (counter * space_between_buttons_y));
        anchorPane.getChildren().add(nextButton);
        counter++;

        //Se le asigna la accion
        nextButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String serverIP = serverAddressTF.getCharacters().toString();
                String serverPort = serverPortTF.getCharacters().toString();

                Main.connect(serverIP, Integer.valueOf(serverPort));

                stage.setScene(Main.getMenuScreen().getScene());
            }
        });

        BlueButton watchButton = new BlueButton("WATCH");
        watchButton.setLayoutX(buttons_start_x);
        watchButton.setLayoutY(buttons_start_y + (counter * space_between_buttons_y));
        anchorPane.getChildren().add(watchButton);
        counter++;

        //Se establece la accion a realizar al ser presionado
        watchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String serverIP = serverAddressTF.getCharacters().toString();
                String serverPort = serverPortTF.getCharacters().toString();
                Main.connect(serverIP, Integer.valueOf(serverPort));
                new ObserverScreen(stage);
            }
        });

        //Se crea el boton para regresar al menu
        BlueButton offlineButton = new BlueButton("OFFLINE");
        offlineButton.setLayoutX(buttons_start_x);
        offlineButton.setLayoutY(buttons_start_y + (counter * space_between_buttons_y));
        anchorPane.getChildren().add(offlineButton);
        counter++;

        //Se le asigna la accion a realizar al ser clickeado
        offlineButton.setOnAction(new EventHandler<ActionEvent>() {
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
