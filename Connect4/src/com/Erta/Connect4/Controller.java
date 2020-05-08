package com.Erta.Connect4;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {
    private static final int ROWS=6;
    private static final int COLUMNS=7;
    private static final int DIAMETER=80;
    private static final String DISC1="#24303E";
    private static final String DISC2="#4CAA88";

    private static String player1="Player One";
    private static String player2="Player Two";

    private boolean isPlayer1=true;

    public GridPane rootGrid;
    public Pane gamePane;
    public Label playerLabel;

    private static class Disc extends Circle{
        private final boolean isPlayer1;
        Disc(boolean isPlayer1){
            this.isPlayer1=isPlayer1;
            setFill(isPlayer1 ? Color.valueOf(DISC1):Color.valueOf(DISC2));
            setRadius(DIAMETER/2);
            setCenterX(DIAMETER/2);
            setCenterY(DIAMETER/2);
        }
    }

    private Disc[][] discs=new Disc[ROWS][COLUMNS];

    public void resetGame(){
        for(int i=0;i<discs.length;i++){
            for(int j=0;j<discs[i].length;j++){
                discs[i][j]=null;
            }
        }
        isPlayer1=true;
        playerLabel.setText(player1);
        playGround();
    }
    private void gameOver(){
        String winner=isPlayer1? player1:player2;
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("Winner is "+winner);
        alert.setContentText("Wanna Play again ?");
        ButtonType yes=new ButtonType("Yes");
        ButtonType no=new ButtonType("Exit");
        alert.getButtonTypes().setAll(yes,no);
        Optional<ButtonType> btn=alert.showAndWait();
        if(btn.isPresent() && btn.get()==yes){
            resetGame();
        }else{
            Platform.exit();
            System.exit(0);
        }
    }

    private Disc getDisc(int row,int col){
        if(row>=ROWS || row<0 || col >=COLUMNS || col<0) return null;
        return discs[row][col];
    }

    private boolean checkCombo(List<Point2D> points){
        int chain=0;
        for(Point2D point:points){
            int x= (int) point.getX();
            int y= (int) point.getY();
            Disc disc=getDisc(x,y);
            if(disc != null && disc.isPlayer1 ==isPlayer1){
                chain++;
                if(chain==4) return true;
            }
            else chain=0;
        }
        return false;
    }

    private boolean gameEnded(int row,int col){
    List<Point2D> vertical= IntStream.rangeClosed(row-3,row+3).mapToObj(r ->new Point2D(r,col)).collect(Collectors.toList());
    List<Point2D> horizontal= IntStream.rangeClosed(col-3,col+3).mapToObj(c ->new Point2D(row,c)).collect(Collectors.toList());
    Point2D startPoint1=new Point2D(row-3,col+3);
    List<Point2D> diagonal=IntStream.rangeClosed(0,6).mapToObj(i -> startPoint1.add(i,-i)).collect(Collectors.toList());
    Point2D startPoint2=new Point2D(row-3,col+3);
    List<Point2D> revDiagonal=IntStream.rangeClosed(0,6).mapToObj(i -> startPoint2.add(i,i)).collect(Collectors.toList());

    boolean isEnded=checkCombo(vertical) || checkCombo(horizontal) || checkCombo(diagonal) || checkCombo(revDiagonal);
        System.out.println(isEnded);
        return isEnded;
    }

    private void switchPlayer(){
        isPlayer1=!isPlayer1;
        playerLabel.setText(isPlayer1?player1:player2);
    }

    private void insertDisc(Disc disc,int col){
        int row=ROWS-1;
        while(row>=0){
            if(getDisc(row,col)==null) break;
            row--;
        }
        if(row<0)return;
        discs[row][col]=disc;
        disc.setTranslateX(col *(DIAMETER+7) +10);
        TranslateTransition animation=new TranslateTransition(Duration.seconds(0.5),disc);
        animation.setToY(row *(DIAMETER+7) +20);
        animation.play();
        if(gameEnded(row,col)) {
            gameOver();
            return;
        }
        switchPlayer();
        gamePane.getChildren().add(disc);
    }

    private List<Rectangle> gameColumn(){
        List<Rectangle> rectangles=new ArrayList<>();
        for(int col=0;col<COLUMNS;col++) {
            Rectangle rectangle = new Rectangle(DIAMETER, (ROWS + 1) * DIAMETER);
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setTranslateX(col *(DIAMETER+7)+10);
            rectangle.setOnMouseEntered(event ->rectangle.setFill(Color.valueOf("#D9F7F073")));
            rectangle.setOnMouseExited(event -> rectangle.setFill(Color.TRANSPARENT));
            final int col_num=col;
            rectangle.setOnMouseClicked(event -> {
                insertDisc(new Disc(isPlayer1),col_num);
            });
            rectangles.add(rectangle);
        }
        return rectangles;
    }

    private Shape createGround(){
        Shape ground=new Rectangle((COLUMNS+1)*DIAMETER,(ROWS+1)*DIAMETER);
        for(int row=0;row<ROWS;row++){
            for(int col=0;col<COLUMNS;col++){
                Circle circle=new Circle();
                circle.setRadius(DIAMETER/2);
                circle.setCenterX(col *(DIAMETER+7) +50);
                circle.setCenterY(row *(DIAMETER+7) +60);
                ground=Shape.subtract(ground,circle);
            }
        }
        ground.setFill(Color.WHITE);
        return ground;
    }

    void playGround(){
        Shape ground=createGround();
        rootGrid.add(ground,0,1);

        List<Rectangle> rectangles=gameColumn();
        rectangles.forEach(rectangle ->rootGrid.add(rectangle,0,1));

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
