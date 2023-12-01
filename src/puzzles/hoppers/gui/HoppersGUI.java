package puzzles.hoppers.gui;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersConfig;
import puzzles.hoppers.model.HoppersModel;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

public class HoppersGUI extends Application implements Observer<HoppersModel, String> {
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";

    /**
     * These are all the images that will be used for the buttons
     */
    // for demonstration purposes
    private Image redFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"red_frog.png"));
    private Image lilyPad = new Image(getClass().getResourceAsStream(RESOURCES_DIR+ "lily_pad.png"));
    private Image water = new Image(getClass().getResourceAsStream(RESOURCES_DIR+ "water.png"));
    private Image greenFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR+ "green_frog.png"));

    /**
     * These are all the private states
     */
    private HoppersModel model;
    private Button[][] Coordinates;
    private int rows;
    private int columns;
    private char[][] grid;
    private boolean initialized;
    private GridPane gridPane;
    private Label TopLabel;
    private Stage stage;
    private int a;
    private int b;
    private BorderPane borderPane;

    /**
     * This will initialize the GUI
     * @throws IOException
     */
    public void init() throws IOException {
        String filename = getParameters().getRaw().get(0);
        this.model = new HoppersModel();
        this.initialized = false;
        this.model.addObserver( this );
        model.load(filename);
        HoppersConfig Pond = model.getCurrentConfig();
        this.rows = Pond.getRows();
        this.columns = Pond.getColumns();
        this.grid = Pond.getGrid();
        this.Coordinates = new Button[Pond.getRows()][Pond.getColumns()];
        gridPane = new GridPane();
        TopLabel = new Label("Load a file!");
        TopLabel.setStyle( "-fx-font: 18px Menlo" );

    }

    /**
     * This method will start placing all the borderpanes, grid panes and the main stage;
     * @param stage This is what is being displayed in the window or the stage
     * @throws Exception
     */

    public void start(Stage stage) throws Exception {
        initialized = true;

        BorderPane borderPane = new BorderPane();
        BorderPane topPane = new BorderPane();
        BorderPane bottomPane = new BorderPane();

        topPane.setCenter(TopLabel);
        makeGrid();
        borderPane.setCenter(gridPane);
        borderPane.setTop(topPane);
        bottomPane.setCenter(makeButtons());
        borderPane.setBottom(bottomPane);

        Scene scene = new Scene(borderPane);
        this.stage = stage;
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    /**
     * This will make the grid of buttons and this will represent the grid or the pond, so that the user can select
     * where they want to move the frogs
     */
    private void makeGrid(){
    gridPane = new GridPane();
    for(int row = 0; row < model.getCurrentRow(); ++row){
        for(int col = 0; col < model.getCurrentColumn() ; ++col){
            this.Coordinates[model.getCurrentRow()][model.getCurrentColumn()] = new Button();
            ImageView icon2 = new ImageView(water);
            icon2.setFitWidth(75);
            icon2.setFitHeight(75);
            Coordinates[row][col].setGraphic(icon2);
            Coordinates[row][col].setMinSize(74,74);
            Coordinates[row][col].setMaxSize(74,74);
            int finalRow = row;
            int finalCol = col;
            Coordinates[row][col].setOnAction(Event -> this.model.enterSelection(finalRow, finalCol) );
            if(grid[row][col] == 'R'){
                Coordinates[row][col].setGraphic(new ImageView(redFrog));
            }
            if(grid[row][col] == 'G'){
                Coordinates[row][col].setGraphic(new ImageView(greenFrog));

            }
            if(grid[row][col] == '.'){
                Coordinates[row][col].setGraphic(new ImageView(lilyPad));


            }
            gridPane.add(this.Coordinates[row][col], col, row);
        }
    }

    }

    /**
     * This method will create the bottom three buttons, the buttons will load the file, give us the hint, and help us
     * reset the puzzle
     * @return it will return a gridPane of the buttons
     */
    public GridPane makeButtons(){
        FileChooser chooser = new FileChooser();
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        currentPath += File.separator + "data" + File.separator + "hoppers";
        chooser.setInitialDirectory(new File(currentPath));
        GridPane Buttons = new GridPane();
        Button Load = new Button("Load");

        Load.setOnAction(actionEvent -> {
            File fileC = chooser.showOpenDialog(new Stage());
            try {
                start(stage);
                if (fileC != null) {
                    String filenameC = fileC.getAbsolutePath();
                    model.load(filenameC);
                    filenameC = filenameC.substring(filenameC.lastIndexOf(File.separator) + 1);
                    TopLabel.setText("Loaded: " + filenameC);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Load.setStyle( "-fx-font: 16px Menlo" );

        Button Reset = new Button("Reset");
        Reset.setStyle( "-fx-font: 16px Menlo" );
        Reset.setOnAction((event) -> {
            try {
                model.newGame(); TopLabel.setText("Puzzle Reset!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Button Hint = new Button("Hint");
        Hint.setOnAction((event) -> {model.hint();});
        Hint.setStyle( "-fx-font: 16px Menlo" );

        Buttons.add(Load,0,0);
        Buttons.add(Reset,1,0);
        Buttons.add(Hint, 2, 0);
        return Buttons;
    }

    /**
     * This method will update what the user is viewing when they are controlling the game.
     * @param hoppersModel this is the model that is being used
     * @param msg this is the msg that will be printed during a gameState
     */
    @Override
    public void update(HoppersModel hoppersModel, String msg) {
        if(!initialized)return;
        this.Coordinates = new Button[model.getCurrentConfig().getRows()][model.getCurrentConfig().getColumns()];
        for(int row = 0; row < model.getCurrentConfig().getRows(); ++row) {
            for (int col = 0; col < model.getCurrentConfig().getColumns(); ++col) {
                this.Coordinates[row][col] = new Button();
                ImageView icon2 = new ImageView(water);
                Coordinates[row][col].setGraphic(icon2);
                Coordinates[row][col].setMinSize(74, 74);
                Coordinates[row][col].setMaxSize(74, 74);
                if(model.getCurrentConfig().getGrid()[row][col] == 'R'){
                    Coordinates[row][col].setGraphic(new ImageView(redFrog));
                }
                if(model.getCurrentConfig().getGrid()[row][col] == 'G'){
                    Coordinates[row][col].setGraphic(new ImageView(greenFrog));

                }
                if(model.getCurrentConfig().getGrid()[row][col] == '.'){
                    Coordinates[row][col].setGraphic(new ImageView(lilyPad));
                }
                int finalRow = row;
                int finalCol = col;
                Coordinates[row][col].setOnAction(actionEvent -> {model.enterSelection(finalRow, finalCol);
                });
                gridPane.add(this.Coordinates[row][col], col, row);
            }
        }
        stage.sizeToScene();

        if(model.gameState() == HoppersModel.GameState.FIRST_SELECTION){
            a = model.getCurrentRow(); b = model.getCurrentColumn();
            TopLabel.setText("Selected (" + a + ", " + b + ")");
        }

        if(model.gameState() == HoppersModel.GameState.SECOND_SELECTION){
            int c = model.getPrevRow();
            int d = model.getPrevColumn();
            TopLabel.setText("Jumped from (" + c +", " + d +")" + " to (" + model.getCurrentRow()  + ", "
                    + model.getCurrentColumn()  +")" );
        }

        if(model.gameState() == HoppersModel.GameState.ILLEGAL_SELECTION){
            TopLabel.setText("Invalid Selection"+ "(" + model.getCurrentRow() + ", " + model.getCurrentColumn() + ")");
        }
        if(model.gameState() == HoppersModel.GameState.ILLEGAL_JUMP){
            TopLabel.setText("Can't jump from " + "(" + model.getPrevRow() + ", " + model.getPrevColumn() + ")" +
                    " to (" + model.getCurrentRow() + ", " + model.getCurrentColumn() + ")");
        }
        if(model.gameState() == HoppersModel.GameState.WON){
            TopLabel.setText("Solution Found!!");
        }
        if(model.gameState() == HoppersModel.GameState.LOST){
            TopLabel.setText("No solution!");
        }

        if(model.gameState() == HoppersModel.GameState.ONGOING){
            TopLabel.setText(msg);
        }

    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            Application.launch(args);
        }
    }
}
