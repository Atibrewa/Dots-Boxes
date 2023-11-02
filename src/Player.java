import java.awt.Color;

/**
 * A class that stores key information about a pllayers of the game
 * 
 * @author Atibrewa
 * @author ae-bii
 * @author lescobos
 * 
 */
public class Player {
    
    private String name;
    private Color colour;
    private int score;
    private Color boxColour; 

    /**
     * Initialises a player object and assigns their name, colour, and score
     * 
     * @param name name of the player
     * @param colour the player's game colour
     */
    public Player(String name, Color colour, Color boxColour) {
        this.name = name;
        this.colour = colour;
        this.boxColour = boxColour; 
        score = 0;
    }

    /**
     * Adds a point to the player's score
     */
    public void winPoint() {
        score++;
    }

    /**
     * Returns a string that contains the name and score of the player so it can be displayed on screen
     * @return string
     */
    public String label() {
        return name + ": " + String.valueOf(score);
    }

    /**
     * Returns the player's color 
     * @return color 
     */
    public Color getColor() {
        return colour;
    }

    /**
     * Returns the color that fills the boxes when completed 
     * @return color 
     */
    public Color getBoxColour() {
        return boxColour; 
    }

    /**
     * Returns the score of the player. Helps to determine the winner. 
     * @return int  
     */
    public int getScore() {
        return score;
    }

    /**
     * Returns the name of the player
     * @return string  
     */
    public String getName() {
        return name;
    }

}
