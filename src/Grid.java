import java.awt.Color;

import Graphics.CanvasWindow;
import Graphics.Ellipse;
import Graphics.Point;
import Graphics.Rectangle;
import Graphics.events.MouseButtonEvent;

import java.util.ArrayList;

/**
 * A class that creates the grid and all the edges and boxes in the game. 
 * It also handles the logic of 4-cycles being formed
 * 
 * @author Atibrewa
 * @author ae-bii
 * @author lescobos
 * 
 */
public class Grid {
    public static final int STANDARD_DOT_SIZE = 10;

    private Graph<Ellipse> game;
    private int m;
    private int n;
    private int lineLength;
    private double startingPoint;
    private Ellipse[][] dots;
    private Rectangle[][] edges;
    private ArrayList<Rectangle> boxes;

    private CanvasWindow canvas;
    int counter = 0;

    /**
     * Initializes the grid, dots, and edges. 
     * Assigns a starting point and a length for the lines in the grid
     * 
     * @param m number of rows
     * @param n number of columns
     * @param canvas the main canvas that the game is drawn on
     */
    public Grid (int m, int n, CanvasWindow canvas) {
        this.m = m;
        this.n = n;
        lineLength = GameManager.CANVAS_WIDTH / (n + 10);
        this.startingPoint = (GameManager.CANVAS_WIDTH / 2) - ((n + 1)/ 2) * lineLength ;
        game = new Graph<Ellipse>();
        this.canvas = canvas;
        
        dots = new Ellipse[m][n];
        edges = new Rectangle[m][n];
        boxes = new ArrayList<>();

        makeDots();
        drawBoxes();
        drawEdges();
    }

    /**
     * Colours the edge selected on the canvas, and adds a corresponding edge to the graph
     * 
     * @param event the mouse event that caused this selection to be called
     * @param color the colour that edge needs to be turned to (the current player's colour)
     * @return the edge that has just been selected
     */
    public Rectangle selectEdge(MouseButtonEvent event, Color color) {
        if (canvas.getElementAt(event.getPosition()) instanceof Rectangle && !boxes.contains(canvas.getElementAt(event.getPosition()))) {
            Rectangle edgenew = (Rectangle) canvas.getElementAt(event.getPosition());
            if (!edgenew.isFilled()) {
                Ellipse source;
                Ellipse destination;
                
                colourObject(edgenew, color);

                int row = (int) (edgenew.getCenter().getY() - startingPoint)/lineLength;
                int column = (int) (edgenew.getCenter().getX() - startingPoint)/lineLength;

                try {
                    if (isHorizontal(edgenew) && column + 1 < n) {
                        source = dots[row][column];
                        destination = dots[row][column+1];
                    } else {
                        source = dots[row][column];
                        destination = dots[row+1][column];
                    }
                    game.addEdge(source, destination);
                } catch (Exception e) {
                    
                }

                return edgenew;
            }
        }
        return null;
    }

    /**
     * Adds the dots onto the canvas window
     */
    private void makeDots() {
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                Ellipse dot = new Ellipse(0,0, STANDARD_DOT_SIZE, STANDARD_DOT_SIZE);
                dot.setCenter(startingPoint +  j* lineLength, startingPoint + i * lineLength);
                dot.setFillColor(Color.BLACK);

                dots[i-1][j-1] = dot;
                game.addVertex(dot);
                canvas.add(dot);
                canvas.draw();
            }
        }
    }

    /**
     * Adds invisible boxes to the screen
     */
    private void drawBoxes() {
        for (int i = 0; i < m-1; i++) {
            for (int j = 0; j < n-1; j++) {
                Ellipse a = dots[i][j];
                Ellipse b = dots[i+1][j+1];
                Rectangle box = new Rectangle(0, 0, lineLength, lineLength);
                double xPos = (a.getCenter().getX() + b.getCenter().getX())/2;
                double yPos = (a.getCenter().getY() + b.getCenter().getY())/2;
                box.setCenter(xPos, yPos);
                box.setFilled(false);
                box.setStroked(false);
                boxes.add(box);
                canvas.add(box);
            }
        }
    }

    /**
     * Draws the edges onto the canvas window, orginally as invisible
     */
    private void drawEdges() {
        for (int i = 1; i <= m; i++) {
            for (int j = 0; j < n; j++) {
                Point a = dots[i-1][j].getCenter();

                if (i*n + j >= m*n) {
                    break;
                }

                Point b = dots[i][j].getCenter();

                double xPos = (a.getX() + b.getX())/2;
                double yPos = (a.getY() + b.getY())/2;

                Rectangle edge = new Rectangle(0, 0, 5, lineLength);
                edge.setCenter(xPos, yPos);
                edge.setStroked(false);
                edge.setFilled(false);

                edges[i-1][j] = edge;
                canvas.add(edge);
                canvas.draw();
            }
        }

        for (int i = 1; i <= m+1; i++) {
            for (int j = 0; j < n; j++) {
                if ((i-1)*n + j >= m*n) {
                    break;
                }

                Point a = dots[i-1][j].getCenter();

                if (n-1 == j) {
                    break;
                }

                Point b = dots[i-1][j+1].getCenter();

                double xPos = (a.getX() + b.getX())/2;
                double yPos = (a.getY() + b.getY())/2;

                Rectangle edge = new Rectangle(0, 0, lineLength, 5);
                edge.setCenter(xPos, yPos);
                edge.setStroked(false);
                edge.setFilled(false);

                edges[i-1][j+1] = edge;
                canvas.add(edge);
                canvas.draw();
            }
        }
    }

    /**
     * Checks where the edge made a 4 cycle or not
     * 
     * @param edge the edge that the player added
     * @param player the current player
     * @return true or false depending on whether a cycle exists
     */
    public boolean checkFourCycle(Rectangle edge, Player player) {
        if (isHorizontal(edge)) {
            return horizontalEdgeCycle(edge, player);

        } else {
            return verticalEdgeCycle(edge, player);
        }
    }

    /**
     * Checks whether an edge is horizontal
     * 
     * @param edge the inputted edge
     * @return true or false
     */
    public boolean isHorizontal(Rectangle edge) {
        return edge.getWidth() > edge.getHeight();
    }

    /**
     * Checks whether if a horizontal edge made a cycle or not
     * 
     * @param edge
     * @param player
     * @return true or false depending of a cycle was made
     */
    private boolean horizontalEdgeCycle(Rectangle edge, Player player) {
        boolean isCycle = false;

        int row = (int) (edge.getCenter().getY() - startingPoint)/lineLength;
        int column = (int) (edge.getCenter().getX() - startingPoint)/lineLength;

        Ellipse left = dots[row][column];
        Ellipse leftTop;
        Ellipse rightTop;
        Ellipse leftBottom;
        Ellipse rightBottom;
        Ellipse right;

        if (row - 1 >= 0 && column + 1 < n) {
            leftTop = dots[row-1][column];
            rightTop = dots[row-1][column+1];
            right = dots[row][column+1];
            if (game.hasEdge(left, leftTop)) {
                if (game.hasEdge(leftTop, rightTop)) {
                    if (game.hasEdge(rightTop, right)) {
                        
                        double boxCenterX = edge.getCenter().getX();
                        double boxCenterY = edge.getCenter().getY()-lineLength/2;
    
                        Rectangle box = (Rectangle) canvas.getElementAt(boxCenterX,boxCenterY);
                        box.setFillColor(player.getBoxColour());
                        isCycle = true;
                        player.winPoint();
                    }
                }
            }
        }

        if (row + 1 < m) {
            leftBottom = dots[row+1][column];
            rightBottom = dots[row+1][column+1];
            right = dots[row][column+1];
            if (game.hasEdge(left, leftBottom)) {
                if (game.hasEdge(leftBottom, rightBottom)) {
                    if (game.hasEdge(rightBottom, right)) {
    
                        double boxCenterX = edge.getCenter().getX();
                        double boxCenterY = edge.getCenter().getY()+lineLength/2;
    
                        Rectangle box = (Rectangle) canvas.getElementAt(boxCenterX,boxCenterY);
                        box.setFillColor(player.getBoxColour());
                        isCycle = true;
                        player.winPoint();
                    }
                }
            }
        }
        return isCycle;
    }
    
    /**
     * Checks whether a verticle edge made a cycle or not
     * 
     * @param edge
     * @param player
     * @return true or false depending of a cycle was made
     */
    private boolean verticalEdgeCycle(Rectangle edge, Player player) {
        boolean isCycle = false;

        int row = (int) (edge.getCenter().getY() - startingPoint)/lineLength ;
        int column = (int) (edge.getCenter().getX() - startingPoint)/lineLength;

        Ellipse top = dots[row][column];
        Ellipse topRight;
        Ellipse bottomRight;
        Ellipse bottom;
        Ellipse topLeft;
        Ellipse bottomLeft;

        if (column + 1 < n) {
            topRight = dots[row][column+1];
            bottomRight = dots[row+1][column+1];
            bottom = dots[row+1][column];
            if (game.hasEdge(top, topRight)) {
                if (game.hasEdge(topRight, bottomRight)) {
                    if (game.hasEdge(bottomRight, bottom)) {
    
                        double boxCenterX = edge.getCenter().getX()+lineLength/2;
                        double boxCenterY = edge.getCenter().getY();
    
                        Rectangle box = (Rectangle) canvas.getElementAt(boxCenterX,boxCenterY);
                        box.setFillColor(player.getBoxColour());
                        isCycle = true;
                        player.winPoint();
                    }
                }
            }
        }

        if (column - 1 >= 0) {
            bottom = dots[row+1][column];
            topLeft = dots[row][column-1];
            bottomLeft = dots[row+1][column-1];
            if (game.hasEdge(top, topLeft)) {
                if (game.hasEdge(topLeft, bottomLeft)) {
                    if (game.hasEdge(bottomLeft, bottom)) {
    
                        double boxCenterX = edge.getCenter().getX()-lineLength/2;
                        double boxCenterY = edge.getCenter().getY();
    
                        Rectangle box = (Rectangle) canvas.getElementAt(boxCenterX,boxCenterY);
                        box.setFillColor(player.getBoxColour());
                        isCycle = true;
                        player.winPoint();
                    }
                }
            }
        }

        return isCycle;
    }

    /**
     * Colors the edge the specified color
     * 
     * @param edge selected edge
     * @param colour the specified color
     */
    public void colourObject(Rectangle edge, Color colour) {
        edge.setFilled(true);
        edge.setFillColor(colour);
    }

    public Graph<Ellipse> getGraph() {
        return game;
    }

    public boolean boxesExist() {
        for (Rectangle box : boxes) {
            if (!box.isFilled()) {
                return true;
            }
        }
        return false;
    }

}