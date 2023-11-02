import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import Graphics.CanvasWindow;
import Graphics.GraphicsObject;
import Graphics.GraphicsText;
import Graphics.Image;
import Graphics.Point;
import Graphics.Ellipse;
import Graphics.FontStyle;

/**
 * A class that creates and manages all the user interfaces of the game
 * 
 * @author Atibrewa
 * @author ae-bii
 * @author lescobos
 * 
 */
public class UI {
    private CanvasWindow canvas;

    private Image title;
    private Image play;
    private Image howToPlay; 
    private Image back;
    private Image instructions; 
    private Image instructionsText; 
    private Ellipse blueDot, pinkDot, orangeDot, greenDot, purpleDot; 
    private List<GraphicsText> playerLabels;
    private GraphicsText turnLabel;
    
    /**
    * Initialises parts of the UI
    * @param canvas The main CanvasWindow being used
    * @param grid The grid being used in the game
    */
    public UI(CanvasWindow canvas) {
        this.canvas = canvas;

        title = new Image(0,0, "title.png");
        title.setCenter(canvas.getCenter().getX(),canvas.getCenter().getY()-150);
        title.setScale(1.1);

        play = new Image(0,0, "play.png");
        play.setCenter(canvas.getCenter());
        play.setScale(0.8);

        howToPlay = new Image(0,0, "howtoplay.png");
        howToPlay.setCenter(canvas.getCenter().getX(),canvas.getCenter().getY()+85); //TODO change 85 to smth else - using height
        howToPlay.setScale(0.8);

        instructions = new Image(50,50, "instructions.png");
        instructions.setCenter(canvas.getCenter().getX(), 100);
        instructions.setScale(0.4);

        back = new Image(0,0, "back.png");  
        back.setCenter(canvas.getCenter().getX()+300, 50);
        back.setScale(0.2);

        instructionsText = new Image(0,0, "description.png"); 
        instructionsText.setCenter(canvas.getCenter());
        instructionsText.setScale(0.8);

        playerLabels = new ArrayList<>();
        playerLabels.add(new GraphicsText("Player 1: 0"));
        playerLabels.add(new GraphicsText("Player 2: 0"));

        playerLabels.get(0).setCenter(80, 50);
        playerLabels.get(0).setFont("Comic Sans MS", FontStyle.PLAIN, 30);
        playerLabels.get(1).setCenter(GameManager.CANVAS_WIDTH-150, 50);
        playerLabels.get(1).setFont("Comic Sans MS", FontStyle.PLAIN, 30);

        turnLabel = new GraphicsText("Player 1's Turn");
        turnLabel.setCenter(GameManager.CANVAS_WIDTH/2-40,50);
        turnLabel.setFont("Comic Sans MS", FontStyle.PLAIN, 30);

        menuScreen();
    }

    /**
     * Creates the menu screen by adding buttons and managing mouse listeners.
     */
    private void menuScreen() {
        canvas.add(title);
        canvas.add(play);
        canvas.add(howToPlay);
        canvas.draw();

        canvas.onMouseMove(event -> {
            Point position = event.getPosition();
            if (checkObjectAtPosition(position, play)) {
                play.setScale(0.9);
                play.setImagePath("hoverplay.png");
            } else {
                play.setScale(0.8);
                play.setImagePath("play.png");
            }

            if (checkObjectAtPosition(position, howToPlay)) {
                howToPlay.setScale(0.9);
                howToPlay.setImagePath("hoverhowtoplay.png");
            } else {
                howToPlay.setScale(0.8);
                howToPlay.setImagePath("howtoplay.png");
            }
        });

        canvas.onMouseDown(event -> {
            Point position = event.getPosition();
            if (checkObjectAtPosition(position, play)) {
                canvas.removeAll();
                addPlayersToScreen();
                GameManager.grid = new Grid(8,8,canvas);
                GameManager.gameStart = true;
            }

            if (checkObjectAtPosition(position, howToPlay)) {
                canvas.removeAll();
                instructionScreen();
            }
        });
    }

    /**
     * Adds the names of the players to the screen
     */
    private void addPlayersToScreen() {
        canvas.add(turnLabel);
        for (GraphicsText text : playerLabels) {
            canvas.add(text);
            canvas.draw();
        }
    }

    /**
     * Updates the labels on the creeen to display the players' scores
     * 
     * @param players A list of players in the game
     */
    public void updateScoreOnScreen(List<Player> players) {
        for (int i = 0; i < players.size(); i++) {
            GraphicsText label = playerLabels.get(i);
            label.setText(players.get(i).label());
        }
    }

    /**
     * Adds buttons and handles mouse listeners on the instructions screen 
     */
    private void instructionScreen() {
        addDots();
        canvas.add(back);
        canvas.add(instructions);
        canvas.add(instructionsText);
        
        canvas.onMouseMove(event -> {
            if (checkObjectAtPosition(event.getPosition(), back)) {
                back.setScale(0.3);
                back.setImagePath("hoverback.png");
            } else {
                back.setScale(0.2);
                back.setImagePath("back.png");
            }
        });

        canvas.onMouseDown(event -> {
            if (checkObjectAtPosition(event.getPosition(), back)) {
                canvas.removeAll();
                menuScreen();
            }
        }
        );
    }

    /**
     * Initializes and adds background dots to the instructions screen 
     */
    private void addDots() {
        blueDot = new Ellipse(320, 600, 100, 100);
        blueDot.setFillColor(Color.BLUE);
        blueDot.setStrokeColor(GameManager.BG_COLOR);

        pinkDot = new Ellipse(640, 300, 150, 150);
        pinkDot.setFillColor(Color.PINK);
        pinkDot.setStrokeColor(GameManager.BG_COLOR);

        orangeDot = new Ellipse(50, 100, 160, 160);
        orangeDot.setFillColor(Color.ORANGE);
        orangeDot.setStrokeColor(GameManager.BG_COLOR);

        greenDot = new Ellipse(50, 400, 50, 50);
        greenDot.setFillColor(Color.GREEN);
        greenDot.setStrokeColor(GameManager.BG_COLOR);
        
        purpleDot = new Ellipse(600, 150, 50, 50);
        purpleDot.setFillColor(Color.MAGENTA);
        purpleDot.setStrokeColor(GameManager.BG_COLOR);
       
        canvas.add(blueDot); 
        canvas.add(pinkDot); 
        canvas.add(orangeDot); 
        canvas.add(greenDot);
        canvas.add(purpleDot);
    }

    /**
     * Checks if an object on the canvas is equal to the given object
     * @param position the position on canvas where the object is
     * @param object the object to check against
     * @return true if the element at position is equal the object 
     */
    private boolean checkObjectAtPosition(Point position, GraphicsObject object) {
        return (canvas.getElementAt(position) == object);
    }

    /**
     * Changes the label on top of the screen that shows who plays next
     * 
     * @param playerName the name of the player who plays next
     */
    public void setPlayerTurn(String playerName) {
        turnLabel.setText(playerName + "'s Turn");
        canvas.draw();
    }

    /**
     * Displays the winner's name on the canvas
     * @param winner the Player who won the game
     */
    public void winScreen(Player winner) {
        GraphicsText win = new GraphicsText(winner.getName() + " WINS!");
        win.setFont("Comic Sans MS", FontStyle.PLAIN, 30);
        win.setCenter(canvas.getCenter());
        win.setFillColor(winner.getColor());
        canvas.add(win);
        addDots();
        canvas.draw();
    }
}
