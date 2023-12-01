package puzzles.hoppers.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;

import java.io.IOException;
import java.util.*;

import static puzzles.common.solver.Solver.Solve;

public class HoppersModel {
    public enum GameState { ONGOING, WON, LOST, ILLEGAL_SELECTION,ILLEGAL_JUMP,FIRST_SELECTION, SECOND_SELECTION, LOADED}



    /** the collection of observers of this model */
    private final List<Observer<HoppersModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private HoppersConfig currentConfig;


    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<HoppersModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String msg) {
        for (var observer : observers) {
            observer.update(this, msg);
        }
    }

    /**
     * These are the private states
     */
    private GameState gameState;
    private static int currentRow;
    private static int currentColumn;
    private static String filename1;
    private boolean firstSelection;
    private int prevRow;
    private int prevColumn;

    private static final EnumMap< HoppersModel.GameState, String > STATE_MSGS =
            new EnumMap<>( Map.of(
                    HoppersModel.GameState.WON, "Solution found!",
                    HoppersModel.GameState.LOST, "No Solution",
                    HoppersModel.GameState.ONGOING, "Next Step!",
                    HoppersModel.GameState.LOADED,"loaded",
                    HoppersModel.GameState.ILLEGAL_SELECTION, "Invalid Selection.",
                    HoppersModel.GameState.ILLEGAL_JUMP,"Invalid Jump",
                    HoppersModel.GameState.FIRST_SELECTION,"Selected (",
                    HoppersModel.GameState.SECOND_SELECTION,"Jump"

            ) );


    /**
     * This is the method that will load in a new game using the load method and the file name
     * @throws IOException
     */
    public void newGame() throws IOException {
        load(filename1);
    }

    /**
     * This method enters the select and stores the first selections dnd the second selections
     * @param row it takes the row of
     * @param column it takes the column and
     */
    public void enterSelection(int row, int column ) {
        gameState = GameState.FIRST_SELECTION;
        currentRow = row;
        currentColumn = column;
        validSelection();
        this.alertObservers(HoppersModel.STATE_MSGS.get(this.gameState));
        if(this.gameState == GameState.ILLEGAL_SELECTION) {
            gameState = GameState.SECOND_SELECTION;
            firstSelection = false;
            currentRow = row;
            currentColumn = column;
            validSelection();
            this.alertObservers(HoppersModel.STATE_MSGS.get(this.gameState));

        }
    }

    /**
     * This method will check if the rows and the columns are valid or not and whether the first selection is valid and
     * if the second selection is valid or not too
     */
    public void validSelection() {
        char[][] gridCopy = currentConfig.getGrid();
        if (!firstSelection) {
            if (currentRow > -1 && currentColumn > -1 && currentRow < currentConfig.getColumns()
                    && currentRow < currentConfig.getRows()
                    && gridCopy[currentRow][currentColumn] == 'G' || gridCopy[currentRow][currentColumn] == 'R') {
                firstSelection = true;
                this.prevRow = currentRow;
                this.prevColumn = currentColumn;
                this.gameState = GameState.FIRST_SELECTION;
            }
            else {
                this.gameState = GameState.ILLEGAL_SELECTION;
            }
        } else {
            if (currentRow > -1 && currentColumn > -1 && currentRow < currentConfig.getColumns()
                    && currentRow < currentConfig.getRows()
                    && gridCopy[currentRow][currentColumn] == '.') {
                //moving right
                if (currentRow % 2 == 0 && currentColumn % 2 == 0) {
                    if (currentColumn + 4 >= prevColumn && currentRow == prevRow
                            && currentColumn - 4 == prevColumn && gridCopy[prevRow][prevColumn + 2] == 'G') {
                        gridCopy[currentRow][currentColumn] = gridCopy[prevRow][prevColumn];
                        gridCopy[prevRow][prevColumn] = '.';
                        gridCopy[prevRow][prevColumn + 2] = '.';
                        firstSelection = false;
                        this.gameState = GameState.SECOND_SELECTION;
                    }
                    //moving left
                    else if (currentColumn + 4 >= 0 && currentRow == prevRow && currentColumn + 4 == prevColumn
                            && gridCopy[prevRow][prevColumn - 2] == 'G') {
                        gridCopy[currentRow][currentColumn] = gridCopy[prevRow][prevColumn];
                        gridCopy[prevRow][prevColumn] = '.';
                        gridCopy[prevRow][prevColumn - 2] = '.';
                        firstSelection = false;
                        this.gameState = GameState.SECOND_SELECTION;

                    }
                    //moving down
                    else if (currentRow - 4 >= 0 && currentColumn == prevColumn && currentRow - 4 == prevRow
                            && gridCopy[currentRow - 2][prevColumn] == 'G') {
                        gridCopy[currentRow][currentColumn] = gridCopy[prevRow][prevColumn];
                        gridCopy[prevRow][prevColumn] = '.';
                        gridCopy[prevRow + 2][prevColumn] = '.';
                        firstSelection = false;
                        this.gameState = GameState.SECOND_SELECTION;

                    }
                    //moving up
                    else if (currentRow + 4 >= 0 && currentColumn == prevColumn && currentRow + 4 == prevRow
                            && gridCopy[currentRow + 2][prevColumn] == 'G') {
                        gridCopy[currentRow][currentColumn] = gridCopy[prevRow][prevColumn];
                        gridCopy[prevRow][prevColumn] = '.';
                        gridCopy[prevRow - 2][prevColumn] = '.';
                        firstSelection = false;
                        this.gameState = GameState.SECOND_SELECTION;
                    }
                    //top left
                    if (currentRow + 2 >= 0 && currentColumn + 2 >= 0 && currentRow + 2 == prevRow
                            && currentColumn + 2 == prevColumn && gridCopy[prevRow - 1][prevColumn - 1] == 'G') {
                        gridCopy[currentRow][currentColumn] = gridCopy[prevRow][prevColumn];
                        gridCopy[prevRow][prevColumn] = '.';
                        gridCopy[prevRow - 1][prevColumn - 1] = '.';
                        firstSelection = false;
                        this.gameState = GameState.SECOND_SELECTION;
                    }
                    //top right
                    else if (currentRow + 2 >= 0 && currentColumn - 2 >= 0 && currentRow + 2 == prevRow
                            && currentColumn - 2 == prevColumn && gridCopy[prevRow - 1][prevColumn + 1] == 'G') {
                        gridCopy[currentRow][currentColumn] = gridCopy[prevRow][prevColumn];
                        gridCopy[prevRow][prevColumn] = '.';
                        gridCopy[prevRow - 1][prevColumn + 1] = '.';
                        firstSelection = false;
                        this.gameState = GameState.SECOND_SELECTION;

                    }
                    //bottom left
                    else if (currentRow + 2 >= 0 && prevColumn - 2 >= 0 && currentRow - 2 == prevRow
                            && currentColumn + 2 == prevColumn && gridCopy[prevRow + 1][prevColumn - 1] == 'G') {
                        gridCopy[currentRow][currentColumn] = gridCopy[prevRow][prevColumn];
                        gridCopy[prevRow][prevColumn] = '.';
                        gridCopy[prevRow + 1][prevColumn - 1] = '.';
                        firstSelection = false;
                        this.gameState = GameState.SECOND_SELECTION;
                    }
                    //bottom right
                     else if (currentRow + 2 >= 0 && prevColumn + 2 >= 0 && currentRow - 2 == prevRow
                            && currentColumn - 2 == prevColumn && gridCopy[prevRow + 1][prevColumn + 1] == 'G') {
                        gridCopy[currentRow][currentColumn] = gridCopy[prevRow][prevColumn];
                        gridCopy[prevRow][prevColumn] = '.';
                        gridCopy[prevRow + 1][prevColumn + 1] = '.';
                        firstSelection = false;
                        this.gameState = GameState.SECOND_SELECTION;
                    }

                }

                else if (currentRow % 2 != 0 && currentColumn % 2 != 0) {
                    //top left
                    if (currentRow + 2 >= 0 && currentColumn + 2 >= 0 && currentRow + 2 == prevRow
                            && currentColumn + 2 == prevColumn && gridCopy[prevRow - 1][prevColumn - 1] == 'G') {
                        gridCopy[currentRow][currentColumn] = gridCopy[prevRow][prevColumn];
                        gridCopy[prevRow][prevColumn] = '.';
                        gridCopy[prevRow - 1][prevColumn - 1] = '.';
                        firstSelection = false;
                        this.gameState = GameState.SECOND_SELECTION;
                    }
                    //top right
                    else if (currentRow + 2 >= 0 && currentColumn - 2 >= 0 && currentRow + 2 == prevRow
                            && currentColumn - 2 == prevColumn && gridCopy[prevRow - 1][prevColumn + 1] == 'G') {
                        gridCopy[currentRow][currentColumn] = gridCopy[prevRow][prevColumn];
                        gridCopy[prevRow][prevColumn] = '.';
                        gridCopy[prevRow - 1][prevColumn + 1] = '.';
                        firstSelection = false;
                        this.gameState = GameState.SECOND_SELECTION;
                    }
                    //bottom left
                    else if (currentRow + 2 >= 0 && prevColumn - 2 >= 0 && currentRow - 2 == prevRow
                            && currentColumn + 2 == prevColumn && gridCopy[prevRow + 1][prevColumn - 1] == 'G') {
                        gridCopy[currentRow][currentColumn] = gridCopy[prevRow][prevColumn];
                        gridCopy[prevRow][prevColumn] = '.';
                        gridCopy[prevRow + 1][prevColumn - 1] = '.';
                        firstSelection = false;
                        this.gameState = GameState.SECOND_SELECTION;
                    }
                    //bottom right
                    else if (currentRow + 2 >= 0 && prevColumn + 2 >= 0 && currentRow - 2 == prevRow
                            && currentColumn - 2 == prevColumn && gridCopy[prevRow + 1][prevColumn + 1] == 'G') {
                        gridCopy[currentRow][currentColumn] = gridCopy[prevRow][prevColumn];
                        gridCopy[prevRow][prevColumn] = '.';
                        gridCopy[prevRow + 1][prevColumn + 1] = '.';
                        firstSelection = false;
                        this.gameState = GameState.SECOND_SELECTION;
                    }
                    else{
                        this.gameState = GameState.ILLEGAL_JUMP;
                    }

                }
                else{
                    this.gameState = GameState.ILLEGAL_JUMP;
                }
            }
            else{
                this.gameState = GameState.ILLEGAL_SELECTION;
            }
        }
        if(currentConfig.isSolution()){
            this.gameState = GameState.WON;
        }

    }

    /**
     * This method will take the filename and create a new configuration and set that as its current configuration
     * @param filename this is the name of the file that will be used in order to make the configuration
     * @throws IOException
     */
    public void load(String filename) throws IOException {
        currentConfig = new HoppersConfig(filename);
        filename1 = filename;
        this.gameState = GameState.LOADED;
        this.alertObservers(HoppersModel.STATE_MSGS.get(this.gameState));
    }

    /**
     *This method will go through the list of configurations and find the next step and that will be the hint of
     * the current configuration. (Its slow when the puzzle is huge)
     */
    public void hint() {
        this.gameState = GameState.ONGOING;
        LinkedList<Configuration> a;
        a = (LinkedList<Configuration>) Solve(currentConfig);
        if(a.size() <= 1){
            this.gameState = GameState.LOST;
        }
        else{
            currentConfig = (HoppersConfig) a.get(1);
        }
        if(currentConfig.isSolution()){
            this.gameState = GameState.WON;
        }
        this.alertObservers(HoppersModel.STATE_MSGS.get(this.gameState));
    }

    /**
     * This method will return the game state of the configuration
     * @return it returns the game state
     */
    public GameState gameState() {
        return this.gameState;
    }

    /**
     * This method will return the current row selection that the user selected
     * @return it returns the current row
     */
    public int getCurrentRow(){
        return currentRow;
    }

    /**
     * This method will get the current column that the user selected
     * @return it returns the current column
     */
    public int getCurrentColumn(){
        return currentColumn;
    }

    /**
     * This method will get the previous row that the user selected
     * @return it returns the previous row
     */
    public int getPrevRow(){
        return prevRow;
    }

    /**
     * This method will get the previous column that the user selected
     * @return it returns the previous column
     */
    public int getPrevColumn(){
        return prevColumn;
    }

    /**
     * This is the method that will get the filename for the configuration to use
     * @return it returns the filename
     */
    public String getFilename1(){
        return filename1;
    }

    /**
     * This method will return the current configuration that the model is using
     * @return it returns the current configuration
     */
    public HoppersConfig getCurrentConfig() {
        return currentConfig;
    }
}
