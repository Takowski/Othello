import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GameScreen extends Application {

    private Stage window;
    private Board board;
    private int[][] boardGrid;
    private GridPane grid = new GridPane();
    private int windowSize = 600;
    private int gap = 5;
    private int tileSize;

    File discBlack = new File("src/Assets/disc_blackBgr.png");
    File discWhite = new File("src/Assets/disc_whiteBgr.png");
    File bgr = new File("src/Assets/Bgr.png");
    File discBlackMenu = new File("src/Assets/disc_blackMenu.png");
    File discWhiteMenu = new File("src/Assets/disc_whiteMenu.png");

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {

        this.board = new Board();
        this.boardGrid = board.getBoardGrid();
        this.tileSize = windowSize/ boardGrid[0].length;
        this.window = primaryStage;
        window.setTitle("Othello");

        //grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setGridLinesVisible(false);
        grid.setVgap(gap);
        grid.setHgap(gap);

        redrawBoard();

        BorderPane bPane = new BorderPane();
        bPane.setCenter(grid); //can directly create scene from grid if borderpane layout is not gonna be used

        Scene scene = new Scene(bPane, windowSize + tileSize*3 + gap*(boardGrid.length+2), windowSize + gap*(boardGrid.length-1), Color.rgb(128, 128, 128));
        window.setScene(scene);
        window.show();
    }

    public void redrawBoard (){
        Image discBlackImg = new Image(discBlack.toURI().toString(), tileSize, tileSize, false,false);
        Image discWhiteImg = new Image(discWhite.toURI().toString(), tileSize, tileSize, false,false);
        Image bgrImg = new Image(bgr.toURI().toString(), tileSize, tileSize, false,false);
        Image discBlackMenuImg = new Image(discBlackMenu.toURI().toString(), tileSize, tileSize, false,false);
        Image discWhiteMenuImg = new Image(discWhiteMenu.toURI().toString(), tileSize, tileSize, false,false);
        Button button = new Button("Start a game");

        List<ImageView> toAdd = new ArrayList<>();
        for (int r = 0; r < boardGrid.length; r++) {
            for (int c = 0; c < boardGrid[r].length; c++) {
                if(boardGrid[r][c] == 0) {
                    toAdd.add(new ImageView(bgrImg));
                }
                if(boardGrid[r][c] == 1) {
                    toAdd.add(new ImageView(discBlackImg));
                }
                if(boardGrid[r][c] == 2) {
                    toAdd.add(new ImageView(discWhiteImg));
                }
                toAdd.get(toAdd.size()-1).addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    if(1==1) {
                         //= new ImageView(bgrImg);
                    }
                    event.consume();
                });
                GridPane.setConstraints(toAdd.get(toAdd.size()-1), r, c);
            }
        }

        toAdd.add(new ImageView(discBlackMenuImg));
        GridPane.setConstraints(toAdd.get(toAdd.size()-1), boardGrid.length, 3);
        toAdd.add(new ImageView(discWhiteMenuImg));
        GridPane.setConstraints(toAdd.get(toAdd.size()-1), boardGrid.length + 2, 3);
        GridPane.setConstraints(button, boardGrid.length, 5);

        Label blackDiscs = new Label(Integer.toString(board.getNumberOfBlackSquares()));
        GridPane.setConstraints(blackDiscs, boardGrid.length, 4);
        Label whiteDiscs = new Label(Integer.toString(board.getNumberOfWhiteSquares()));
        GridPane.setConstraints(whiteDiscs, boardGrid.length + 2, 4);

        grid.getChildren().addAll(toAdd);
        grid.getChildren().addAll(button, blackDiscs, whiteDiscs);
    }
}