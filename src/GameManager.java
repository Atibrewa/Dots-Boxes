import Graphics.CanvasWindow;
import Graphics.Rectangle;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * The main class that handles the mouse event for the game and maintains a list of players, and instances of all other classes
 * Win logic also takes place here
 * 
 * @author Atibrewa
 * @author ae-bii
 * @author lescobos
 * 
 */
public class GameManager {
    public static final int CANVAS_WIDTH = 800;
    public static final int CANVAS_HEIGHT = 800;
    public static final Color BG_COLOR = new Color(247, 247, 247);
    public static boolean gameStart;
    private CanvasWindow canvas;
    private UI ui;
    public static Grid grid;
    private List<Player> players;
    private int playerCount;
    private Player currentPlayer;

    /**
     * Initialses instances of the other classes and maintains a list of the players
     */
    public GameManager() {
        canvas = new CanvasWindow("Dots and Boxes", CANVAS_WIDTH, CANVAS_HEIGHT);
        canvas.setBackground(BG_COLOR);

        players = new ArrayList<>();
        addPlayer("Player 1", Color.RED, new Color(255, 102,102));
        addPlayer("Player 2", Color.BLUE, new Color(51,153,255));
        currentPlayer = players.get(0);

        ui = new UI(canvas);
        
        run();
    }

    /**
     * Handles the main mouse even during the course of the game and calls methods to colour edges, update the screen and calls the 
     * winCheck() method
     */
    private void run() {
        canvas.onMouseDown(event -> {
            if (gameStart && grid != null) {
                if (grid.boxesExist()) {
                    Rectangle edge = grid.selectEdge(event, currentPlayer.getColor());
                    if (edge != null) {
                        Boolean point = grid.checkFourCycle(edge, currentPlayer);
                        if (point) {
                            ui.updateScoreOnScreen(players);
                        } else {
                            playerCount++;
                            currentPlayer = players.get(playerCount % players.size());
                            ui.setPlayerTurn(currentPlayer.getName());
                        }
                    }
                } else if (!grid.boxesExist()) {
                    Player winner = winCheck();
                    canvas.removeAll();
                    ui.winScreen(winner);
                }
            }
        }
        );
    }

    /**
     * Compares the scores of all players and returns the one with highest score
     * @return The winner of the game
     */
    private Player winCheck() {
        Player winner = players.get(0);
        for (Player p : players) {
            if (p.getScore() > winner.getScore()) {
                winner = p;
            }
        }
        return winner;
    }

    /**
     * Adds a new player to the game
     * @param name name of the player
     * @param colour player's game colour
     * @param boxColour player's box colour
     */
    public void addPlayer(String name, Color colour, Color boxColour) {
        players.add(new Player(name, colour, boxColour));
    }

    public static void main(String[] args) {
        new GameManager();
    }
}