package sample;

import gui.MenuScreen;
import gui.ServerScreen;
import javafx.application.Application;
import javafx.stage.Stage;
import socketclient.SocketClient;

public class Main extends Application {
    private static Stage mainStage = new Stage();
    private static MenuScreen menuScreen = new MenuScreen(mainStage);
    private static Boolean online = false;
    private static SocketClient socket;

    public static MenuScreen getMenuScreen() {
        return menuScreen;
    }

    public static Boolean isOnline() {
        return online;
    }

    public static void setOnline(Boolean online) {
        Main.online = online;
    }

    public static SocketClient getSocket() {
        return socket;
    }

    public static void connect(String ip, Integer port)
    {
        //Main
        socket = new SocketClient(ip, port);
        online = true;
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
