package gui;

import java.lang.Integer;
import java.lang.String;
import java.lang.Long;
import java.lang.Double;


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
import sample.Main;
import tools.Random;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

//audioclip

public class GameScreen {

    private final Integer width = 1440;
    private final Integer height = 900;
    private AnchorPane anchorPane;
    private Scene scene;
    private Stage stage;

    private LinkedList<Enemy> enemyList;
    private LinkedList<Projectile> playerProjectileList;
    private LinkedList<Projectile> enemyProjectileList;
    private LinkedList<Block> blockList;
    private LinkedList<Integer> velocityBoost;

    private Player player;

    private Integer level;
    private Integer ovniCounter = 0;

    private Long lastNanoTime;
    private Double lastChangeDirection;
    private Double lastOvniTime;

    /**
     * Constructor
     * @param mainStage Stage principal de la aplicacion
     */
    public GameScreen(Stage mainStage) {
        level = 1;
        player = new Player();
        enemyList = new LinkedList<Enemy>();
        playerProjectileList = new LinkedList<Projectile>();
        enemyProjectileList = new LinkedList<Projectile>();
        blockList = new LinkedList<Block>();
        velocityBoost = new LinkedList<Integer>();
        anchorPane = new AnchorPane();
        scene = new Scene(anchorPane, width, height);
        stage = mainStage;
        stage.setScene(scene);
        createBackground();
        createLabels();
        createEnemies();
        createBunker(150, 720);
        createBunker(480, 720);
        createBunker(820, 720);
        createBunker(1170, 720);
        render();
    }

    /**
     * Metodo para crear los labels de la ventana
     */
    private void createLabels(){
        Integer labels_start_x = 4; //Posicion en x donde se empiezan a dibujar los labels
        Integer labels_start_y = 4; //Posicion en y donde se empiezan a dibujar los labels
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

        Label levelLabel = new Label("LEVEL: ");
        levelLabel.setTextFill(Color.WHITE);
        levelLabel.setLayoutX(labels_start_x);
        levelLabel.setLayoutY(labels_start_y + (counter * 25));
        counter++;

        //Se les coloca el tipo de letra a los labels
        try {
            scoreLabel.setFont(Font.loadFont(new FileInputStream("src/resources/kenvector_future.ttf"), 23));
            livesLabel.setFont(Font.loadFont(new FileInputStream("src/resources/kenvector_future.ttf"), 23));
            levelLabel.setFont(Font.loadFont(new FileInputStream("src/resources/kenvector_future.ttf"), 23));

        } catch (FileNotFoundException e) {
            scoreLabel.setFont(Font.font("Verdana",23));
            livesLabel.setFont(Font.font("Verdana",23));
            levelLabel.setFont(Font.font("Verdana",23));
        }

        //Se agregan los labels al anchorPane
        anchorPane.getChildren().add(livesLabel);
        anchorPane.getChildren().add(scoreLabel);
        anchorPane.getChildren().add(levelLabel);
    }

    /**
     * Metodo para crear el fondo de la ventana
     */
    private void createBackground(){
        Image backgroundImage = new Image("resources/game_background.jpg", width, height, false, true);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, null);
        anchorPane.setBackground(new Background(background));

    }

    /**
     * Metodo encargado del renderizado de la ventana
     */
    @SuppressWarnings("Duplicates")
    private void render(){
        Canvas canvas = new Canvas(width, height);
        anchorPane.getChildren().add(canvas);

        LinkedList<String> input = new LinkedList<>(); //Lista que almacena los input del usuario

        //Se crean los receptores de entradas del usuario

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                String code = keyEvent.getCode().toString(); //Se obtiene el codigo de la tecla presionada
                if(!input.contains(code)){
                    input.insertAtEnd(code); //Se inserta en la lista de inputs
                }
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                String code = keyEvent.getCode().toString(); //Se obtiene el codigo de la letra que ya no esta siendo presionada
                input.deleteElement(code); //Se elimina de la lista de inputs
            }
        });

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

        lastNanoTime = new Long(System.nanoTime());
        lastChangeDirection = 0.0;
        lastOvniTime = 0.0;

        Image heartImage = new Image("resources/heart.png");
        Sprite playerShip = player.getSprite();

        //Se crea el objeto encargado del ciclo de juego
        new AnimationTimer(){
            @Override
            public void handle(long currentNanoTime) {
                Double elapsedTime = (currentNanoTime - lastNanoTime) / 1000000000.0;
                lastNanoTime = currentNanoTime;
                player.addShootTime(elapsedTime); //Se añade el tiempo transcurrido al jugador
                lastChangeDirection += elapsedTime;
                lastOvniTime += elapsedTime;

                //Se limpia el GraphicContext
                gcontext.clearRect(0, 0, width, height);

                //Control de la nave del jugador
                player.getSprite().setVelocity(0, 0);
                if(input.contains("A") && playerShip.getPositionX() > 0)
                    playerShip.addVelocity(-300, 0);
                if (input.contains("D")&& playerShip.getPositionX() < (width - playerShip.getWidth()))
                    playerShip.addVelocity(300,0);
                if (input.contains("SPACE") && (player.getLastShootTime() > 0.30)) {
                    player.setLastShootTime(0.0);
                    Integer x = (int)(playerShip.getPositionX() + (playerShip.getWidth() / 2));
                    Integer y = (int)(playerShip.getPositionY() - playerShip.getHeight()) + 40;
                    Projectile projectile = new Projectile("resources/laser_blue.png", x, y, -800);
                    playerProjectileList.insertAtFirst(projectile);
                }

                //Se dibuja la informacion de la partida
                gcontext.fillText(player.getScore().toString(), 110, 50); //score
                gcontext.fillText(level.toString(), 110, 75); //level
                Integer heart_position_x = 100; //vidas
                for (Integer i = 0; i < player.getLives(); i++) {
                    Sprite heart = new Sprite();
                    heart.setImage(heartImage);
                    heart.setPosition(heart_position_x, 4);
                    heart_position_x += 30;
                    heart.render(gcontext);

                }

                //Se spawnea un ovni cada 15 segundos
                if(lastOvniTime > 15.0)
                    createOvni();

                checkEdge(); //Se verifica si algun enemigo llego al borde de la ventana
                checkEnemyListSize(); //Se verifica si se debe aumentar la velocidad de los enemigos

                LinkedList<Enemy> deadEnemies = new LinkedList<>(); //Lista que contiene los enemigos que deben ser borrados
                LinkedList<Projectile> destroyedPlayerProjectiles = new LinkedList<>(); //Lista que contiene a los proyectiles que deben ser eliminados
                LinkedList<Projectile> destroyedEnemyProjectiles = new LinkedList<>(); //Lista que contiene a los proyectiles que deben ser eliminados
                LinkedList<Block> blockDestroyed = new LinkedList<>(); //Lista que contiene los enemigos que deben ser borrados

                //Renderizado de las entidades

                //Renderizado de las balas del jugador
                for (Integer i = 0; i < playerProjectileList.getSize(); i++) {
                    Projectile projectile = (Projectile) playerProjectileList.getElement(i).getDataT();

                    //Se verifica que la bala se encuentre dentro de los limites de la pantalla
                    if(projectile.getSprite().getPositionY() < -projectile.getSprite().getHeight()){
                        destroyedPlayerProjectiles.insertAtFirst(projectile);
                    }

                    projectile.getSprite().update(elapsedTime);
                    projectile.getSprite().render(gcontext);
                }

                //Renderizado de los balas enemigas
                for (Integer i = 0; i < enemyProjectileList.getSize(); i++) {
                    Projectile projectile = (Projectile) enemyProjectileList.getElement(i).getDataT();

                    projectile.getSprite().update(elapsedTime);
                    projectile.getSprite().render(gcontext);
                }

                //Renderizado de los enemigos
                for (Integer i = 0; i < enemyList.getSize(); i++) {
                    Enemy enemy = (Enemy) enemyList.getElement(i).getDataT();
                    enemy.getSprite().update(elapsedTime);
                    enemy.getSprite().render(gcontext);

                    enemy.updateLastShootTime(elapsedTime);

                    //Disparo enemigos
                    if(enemy.getLastShootTime() > 3.0){
                        Integer num = Random.getRamdomNumber(0, 100000) + (level * 5);

                        if (num > 99990) {
                            Projectile projectile = enemy.shoot();

                            if (projectile != null) {
                                enemyProjectileList.insertAtFirst(projectile);
                            }
                        }
                    }
                    //Verificar si algun enemigo llego al cañon del jugador
                    if(enemy.getSprite().getPositionY() > 800){
                        this.stop();
                        new DeadScreen(stage, player.getScore());
                    }

                    if(enemy.getClass() == Ovni.class){
                        Ovni ovni = (Ovni) enemy;
                        ovni.addLastChangeDirection(elapsedTime);
                        checkOvniEdge(ovni);
                    }
                }

                //Renderizado de los bloques de los bunkers
                for (Integer i = 0; i < blockList.getSize(); i++) {
                    Block block = (Block) blockList.getElement(i).getDataT();
                    block.getSprite().render(gcontext);
                }

                //Colision de una bala del jugador contra la de un enemigo
                for (Integer i = 0; i < playerProjectileList.getSize(); i++) {
                    Projectile projectile = (Projectile) playerProjectileList.getElement(i).getDataT();

                    for (Integer j = 0; j < enemyList.getSize(); j++) {
                        Enemy enemy = (Enemy) enemyList.getElement(j).getDataT();

                        if(projectile.getSprite().intersects(enemy.getSprite())){

                            if(!deadEnemies.contains(enemy))
                                deadEnemies.insertAtFirst(enemy); //Se inserta al enemigo en la lista de enemigos muertos
                            if(!destroyedPlayerProjectiles.contains(projectile))
                                destroyedPlayerProjectiles.insertAtFirst(projectile); //Se inserta el proyectil en la lista de proyectiles destruidos

                            player.addScore(enemy.getScore()); //Se actualiza el score del jugador
                            if(enemy.getClass() == Ovni.class)
                                ovniCounter--;
                        }
                    }
                }

                //Colision de una bala del jugador contra un bunker
                for (Integer i = 0; i < playerProjectileList.getSize(); i++) {
                    Projectile projectile = (Projectile) playerProjectileList.getElement(i).getDataT();

                    for (Integer j = 0; j < blockList.getSize(); j++) {
                        Block block = (Block) blockList.getElement(j).getDataT();

                        if(projectile.getSprite().intersects(block.getSprite())){
                            if(!blockDestroyed.contains(block))
                                blockDestroyed.insertAtFirst(block); //Se inserta al enemigo en la lista de enemigos muertos
                            if (!destroyedPlayerProjectiles.contains(projectile))
                                destroyedPlayerProjectiles.insertAtFirst(projectile); //Se inserta el proyectil en la lista de proyectiles destruidos
                        }
                    }
                }

                //Colision de una bala de un enemigo contra un bunker
                for (Integer i = 0; i < enemyProjectileList.getSize(); i++) {
                    Projectile projectile = (Projectile) enemyProjectileList.getElement(i).getDataT();

                    for (Integer j = 0; j < blockList.getSize(); j++) {
                        Block block = (Block) blockList.getElement(j).getDataT();

                        if(projectile.getSprite().intersects(block.getSprite())){
                            if(!blockDestroyed.contains(block))
                                blockDestroyed.insertAtFirst(block); //Se inserta al enemigo en la lista de enemigos muertos
                            if (!destroyedEnemyProjectiles.contains(projectile))
                                destroyedEnemyProjectiles.insertAtFirst(projectile); //Se inserta el proyectil en la lista de proyectiles destruidos
                        }
                    }
                }

                //Colision de un enemigo con un bloque de un bunker
                for (Integer i = 0; i < enemyList.getSize(); i++) {
                    Enemy enemy = (Enemy) enemyList.getElement(i).getDataT();

                    for (Integer j = 0; j < blockList.getSize(); j++) {
                        Block block = (Block) blockList.getElement(j).getDataT();

                        if(enemy.getSprite().intersects(block.getSprite())){ //Se verifica si existe una colision
                            if(!blockDestroyed.contains(block)) //Se verifica que el bloque no este en la lista
                                blockDestroyed.insertAtFirst(block);
                        }
                    }
                }

                //Colision de una bala enemiga con el jugador
                for (Integer i = 0; i < enemyProjectileList.getSize(); i++) {
                    Projectile projectile = (Projectile) enemyProjectileList.getElement(i).getDataT();

                    if(projectile.getSprite().intersects(playerShip)){
                        if(!destroyedEnemyProjectiles.contains(projectile))
                            destroyedEnemyProjectiles.insertAtFirst(projectile);
                        player.decreaseLive();

                        if(player.getLives() <= 0){
                            this.stop();
                            new DeadScreen(stage, player.getScore());
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

                for (Integer i = 0; i < blockDestroyed.getSize(); i++) {
                    blockList.deleteElement((Block) blockDestroyed.getElement(i).getDataT());
                }

                for (Integer i = 0; i < destroyedEnemyProjectiles.getSize(); i++) {
                    enemyProjectileList.deleteElement((Projectile) destroyedEnemyProjectiles.getElement(i).getDataT());
                }

                //Se verifica si se han eliminado todos los enemigos del nivel
                if(enemyList.getSize() <= 1){
                    if(enemyList.getSize() == 0) //No queda ningun enemigo
                        changeLevel();
                    else{ //Se verifica si el ultimo enemigo que queda es un ovni
                        Enemy enemy = (Enemy) enemyList.getElement(0).getDataT();
                        if(enemy.getClass() == Ovni.class)
                            changeLevel();
                    }
                }

                playerShip.update(elapsedTime);
                playerShip.render(gcontext);
                sentInfo();

            }
        }.start();

    }

    /**
     * Metodo que verifica si algun enemigo llego a el borde de la ventana
     */
    private void checkEdge() {
        for (Integer i = 0; i < enemyList.getSize(); i++) {
            Enemy enemy = (Enemy) enemyList.getElement(i).getDataT();

            //Se verifica si algun enemigo llego al borde de la pantalla
            Double positionX = enemy.getSprite().getPositionX();
            if ((enemy.getClass() != Ovni.class) && (positionX < 10 || (1430 - enemy.getSprite().getWidth()) < positionX) && lastChangeDirection > 1.0) {
                lastChangeDirection = 0.0;
                changeDirectionX();
                changeLevelY();
                break;
            }
        }
    }


    /**
     * Metodo que verifica si un ovni llego a uno de los bordes de la ventana
     * @param ovni Ovni a verificar
     */
    private void checkOvniEdge(Ovni ovni){
        //Se verifica si el ovni llego al borde de la pantalla
        Double positionX = ovni.getSprite().getPositionX();
        if ((ovni.isShowedUp() && (positionX < 10) || (1430 - ovni.getSprite().getWidth()) < positionX) && ovni.getLastChangeDirection() > 1.0) {
            ovni.setShowedUp(true);
            ovni.setLastChangeDirection(0.0);
            ovni.setVelocity(ovni.getSprite().getVelocityX() * -1, 0); //Se cambia la direccion del ovni
        }
    }

    /**
     * Metodo para cambiar la velocidad en X de los enemigos
     */
    private void changeDirectionX(){
        for (Integer i = 0; i < enemyList.getSize(); i++) { //Se recorre la lista de enemigos
            Enemy enemy = (Enemy) enemyList.getElement(i).getDataT(); //Se obtiene cada enemigo
            Sprite sprite = enemy.getSprite(); //Se obtiene su sprite

            if(enemy.getClass() != Ovni.class)
                sprite.setVelocityX(sprite.getVelocityX() * -1); //Se cambia la direccion en x
        }
    }

    /**
     * Metodo para cambiar la posicion en y de todos los enemigos que no sean un ovni
     */
    private void changeLevelY(){
        for (Integer i = 0; i < enemyList.getSize(); i++) {
            Enemy enemy = (Enemy) enemyList.getElement(i).getDataT(); //Se obtiene cada enemigo
            Sprite sprite = enemy.getSprite(); //Se obtiene su sprite

            if(enemy.getClass() != Ovni.class) //Se verifica que el enemigo actual no sea un ovni
                sprite.setPositionY(sprite.getPositionY() + (sprite.getHeight()));
        }
    }

    /**
     * Metodo que verifica cuando debe aumentar la velocidad de los enemigos segun el tamaño de la lista de enemigos
     */
    private void checkEnemyListSize(){
        Integer xBoost = 10; //Numero en el que se aumenta la velocidad en x de los enemigos
        if((enemyList.getSize() % 10 == 0) && !(velocityBoost.contains(enemyList.getSize()))){
            velocityBoost.insertAtFirst(enemyList.getSize());
            for (Integer i = 0; i < enemyList.getSize(); i++) {
                Enemy enemy = (Enemy) enemyList.getElement(i).getDataT();
                Sprite sprite = enemy.getSprite();

                if (sprite.getVelocityX() < 0) { //Caso en el que los enemigos se estan moviendo hacia la izquierda
                    sprite.setVelocityX(sprite.getVelocityX() - xBoost);
                } else { //Caso en el que los enemigos se estan moviendo hacia la derecha
                    sprite.setVelocityX(sprite.getVelocityX() + xBoost);
                }
            }
        }
    }


    /**
     * Metodo para crear los enemigos
     */
    private void createEnemies(){
        Integer position_start_y = 10;

        Integer space_between_aliens_x = 74;
        Integer space_between_aliens_y = 50;

        for (Integer i = 0; i < 6; i++) {
            Integer position_x = 10;

            for (Integer j = 0; j < 15; j++) {
                Enemy enemy;

                if(i < 2){
                    enemy = new Octopus(position_x, position_start_y + (i * space_between_aliens_y));

                } else if(2 <= i && i < 4 ){
                    enemy = new Crab(position_x, position_start_y + (i * space_between_aliens_y));

                } else {
                    enemy = new Squid(position_x, position_start_y + (i * space_between_aliens_y));
                }

                enemy.setVelocity(50 + (level * 5), 0);
                enemyList.insertAtFirst(enemy);

                position_x += space_between_aliens_x;
            }
        }
    }

    /**
     * Metodo para crear un bunker
     * @param x Posicion x de la esquina superior izquierda
     * @param y Posicion y de la esquina inferior derecha
     */
    private void createBunker(Integer x, Integer y){
        Integer block_size = 12; //Tamanio de los bloques
        Integer rows = 6; //Numero de filas
        Integer colums = 10; //Numero de columnas

        for (Integer i = 0; i < rows; i++) {
            for (Integer j = 0; j < colums; j++) {
                if((i == 0) && !(j<2) && !((colums-3)<j)){
                    blockList.insertAtFirst(new Block( x + (j * block_size), y + (i * block_size)));
                } else if((i==1) && !(j<1) && !((colums-2)<j)){
                    blockList.insertAtFirst(new Block( x + (j * block_size), y + (i * block_size)));
                } else if (i > 1){
                    blockList.insertAtFirst(new Block( x + (j * block_size), y + (i * block_size)));
                }

            }
        }
    }

    /**
     * Metodo para crear un ovni
     */
    private void createOvni() {
        lastOvniTime = 0.0; //se reinicia el tiempo de aparicion del ovni

        if (ovniCounter < 1) { //Solo puede existir un ovni en pantalla
            Ovni ovni = new Ovni(-100, 50);
            ovni.setVelocity(250, 0);
            enemyList.insertAtFirst(ovni);
            ovniCounter++;
        }
    }

    /**
     * Metodo para cambiar de nivel, crea de nuevo los enemigos y restaura los bunkers
     */
    private void changeLevel(){
        level++;
        blockList.cleanList();
        velocityBoost.cleanList();
        createBunker(150, 720);
        createBunker(480, 720);
        createBunker(820, 720);
        createBunker(1170, 720);
        createEnemies();
        player.addLive();
    }


    private void sentInfo()
    {
        if(Main.isOnline())
        {
            String info = "";

            for (Integer i = 0; i < enemyList.getSize(); i++)
            {
                Enemy enemy = (Enemy) enemyList.getElement(i).getDataT();
                info += enemy + "_";
            }
            info += "-";

            info += player + "-";

            for (Integer i = 0; i < enemyProjectileList.getSize() ; i++)
            {
                Projectile projectile = (Projectile) enemyProjectileList.getElement(i).getDataT();
                info += projectile + "_";
            }
            info += "-";

            for (Integer i = 0; i < playerProjectileList.getSize() ; i++)
            {
                Projectile projectile = (Projectile) playerProjectileList.getElement(i).getDataT();
                info += projectile + "_";
            }
            info += "-";

            for (Integer i = 0; i < blockList.getSize() ; i++)
            {
               Block block = (Block) blockList.getElement(i).getDataT();
               info += block + "_";
            }

        }
    }
}
