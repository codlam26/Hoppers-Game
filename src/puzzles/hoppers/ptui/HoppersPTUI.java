package puzzles.hoppers.ptui;

import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersConfig;
import puzzles.hoppers.model.HoppersModel;

import java.io.IOException;
import java.io.PrintWriter;

public class HoppersPTUI extends ConsoleApplication implements Observer<HoppersModel, String> {
    private HoppersModel model;

    private boolean initialized;

    private PrintWriter out;

    public void init(){
        this.initialized = false;
        this.model = new HoppersModel();
        this.model.addObserver(this);

    }

    public void start( PrintWriter out ) {
        this.out = out;
        this.initialized = true;

        super.setOnCommand( "h", 0, "hint next move",
                args -> this.model.hint()
        );

        super.setOnCommand( "load", 1, "load new puzzle file",
                args -> {
                    try {
                        this.model.load(args[0]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
        super.setOnCommand("reset", 0, "reset the current game",
                args -> {
                    try {
                        this.model.newGame();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        super.setOnCommand( "s", 2, "select cell at r, c",
                args -> this.model.enterSelection(Integer.parseInt(args[0]),Integer.parseInt(args[1]))

        );


        // TODO Add code here to cause the "new" command to start a new game.
    }


    @Override
    public void update(HoppersModel model, String msg) {
        final HoppersModel.GameState gameState = model.gameState();
        if(gameState == HoppersModel.GameState.LOST){
            this.out.println(msg);
        }
        if(gameState == HoppersModel.GameState.ONGOING){
            this.out.println(msg);
        }
        if(gameState == HoppersModel.GameState.FIRST_SELECTION){
            this.out.println( "Selected (" + model.getCurrentRow() + ", " + model.getCurrentColumn() + ")");
        }
        if(gameState == HoppersModel.GameState.SECOND_SELECTION){
            this.out.println( "Jumped from (" + model.getPrevRow() + ", " + model.getPrevColumn() + ")" +
                    " to (" + model.getCurrentRow() + ", " + model.getCurrentColumn() + ")" );
        }
        if(gameState == HoppersModel.GameState.LOADED){
            this.out.println("Loaded: " + model.getFilename1());
        }
        if(gameState == HoppersModel.GameState.ILLEGAL_SELECTION){
            this.out.println("Invalid selection" + "(" + model.getCurrentRow() + ", " + model.getCurrentColumn() + ")");
        }
        if(gameState == HoppersModel.GameState.ILLEGAL_JUMP){
            this.out.println("Can't jump from " + "(" + model.getPrevRow() + ", " + model.getPrevColumn() + ")" +
                    " to (" + model.getCurrentRow() + ", " + model.getCurrentColumn() + ")");
        }
        if(gameState == HoppersModel.GameState.WON){
            this.out.println(msg);
        }

            System.out.print(" ");
            for(int col = 0; col < model.getCurrentConfig().getColumns(); col++){
                System.out.print(" " + col);
            }
            System.out.println();
            System.out.print(" ");
            for(int col = 0; col < model.getCurrentConfig().getColumns(); col++){
                System.out.print("--");
            }
            System.out.println();
            for(int r = 0; r < model.getCurrentConfig().getRows(); r++){
                System.out.print(r + "|");
                for(int c = 0; c < model.getCurrentConfig().getColumns(); c++){
                    System.out.print(model.getCurrentConfig().getGrid()[r][c] + " ");
                }
                System.out.println();
            }

    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        }
        else{
            ConsoleApplication.launch( HoppersPTUI.class, args );
        }


    }

}
