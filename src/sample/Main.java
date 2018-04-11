package sample;

import gui.MenuScreen;
import gui.ServerScreen;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage mainStage = new Stage();
    private static MenuScreen menuScreen = new MenuScreen(mainStage);
    private static boolean online = false;

    public static MenuScreen getMenuScreen() {
        return menuScreen;
    }

    public static boolean isOnline() {
        return online;
    }

    public static void setOnline(boolean online) {
        Main.online = online;
    }

    @Override
    public void start(Stage primaryStage){
        try {
            new ServerScreen(mainStage);
            mainStage.setTitle("Space Invaders");
            mainStage.setResizable(false);
            primaryStage = mainStage;
            primaryStage.show();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }


}
