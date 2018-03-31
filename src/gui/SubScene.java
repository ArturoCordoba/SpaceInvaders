package gui;

import java.lang.Integer;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class SubScene extends javafx.scene.SubScene {

    private final static String font_path = "resources/kenvector_future.ttf";
    private final static String background_path = "resources/game_background.jpg";
    public static final Integer width = 1440;
    public static final Integer height = 900;

    public SubScene() {
        super(new AnchorPane(), width, height);
        prefWidth(width);
        prefHeight(height);
        BackgroundImage backgroundImage = new BackgroundImage(new Image(background_path, width, height, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, null);

        AnchorPane root2 = (AnchorPane) this.getRoot();
        root2.setBackground(new Background(backgroundImage));
    }
}
