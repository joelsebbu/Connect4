package com.Erta.Connect4;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    private Controller controller;

    private void reset() {

    }

    private void exit() {
        Platform.exit();
        System.exit(0);
    }

    private void about_Game() {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("About Game");
        info.setHeaderText("Connect4");
        info.setContentText("Connect Four is a two-player connection board game" +
                " in which the players first choose a color and" +
                " then take turns dropping one colored disc from the top into a seven-column, six-row vertically suspended grid." +
                " The pieces fall straight down, occupying the lowest available space within the column. The objective " +
                "of the game is to be the first to form a horizontal, vertical, or diagonal line of four of one's own discs.");
        info.show();
    }

    private void about_Me(){
        Alert info =new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("About Developer");
        info.setHeaderText("Joel J Sebastian");
        info.setContentText("github : joelsebbu");
        info.show();
    }

    private MenuBar def_menu() {
        Menu file = new Menu("File");
        MenuItem newGame = new MenuItem("New Game");
        newGame.setOnAction(event -> controller.resetGame());
        MenuItem resetGame = new MenuItem("Reset Game");
        resetGame.setOnAction(event -> controller.resetGame());
        SeparatorMenuItem r_qMenu = new SeparatorMenuItem();
        MenuItem quitGame = new MenuItem("Quit Game");
        quitGame.setOnAction(event -> exit());
        file.getItems().addAll(newGame, resetGame, r_qMenu, quitGame);

        Menu help = new Menu("Help");
        MenuItem aboutGame = new MenuItem("About Game");
        aboutGame.setOnAction(event -> about_Game());
        SeparatorMenuItem g_m = new SeparatorMenuItem();
        MenuItem aboutMe = new MenuItem("About Me");
        aboutMe.setOnAction(event -> about_Me());
        help.getItems().addAll(aboutGame, g_m, aboutMe);

        MenuBar menu = new MenuBar();
        menu.getMenus().addAll(file, help);
        return menu;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
        GridPane rootGrid = loader.load();

        controller = loader.getController();
        controller.playGround();
        MenuBar menu = def_menu();
        menu.prefWidthProperty().bind(primaryStage.widthProperty());
        Pane menuPane = (Pane) rootGrid.getChildren().get(0);
        menuPane.getChildren().add(menu);
        Scene scene = new Scene(rootGrid);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Connect4");
        primaryStage.setResizable(false);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
