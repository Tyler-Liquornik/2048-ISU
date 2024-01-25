// ISU Project
// Game components for 2048
// Mr. Naccarato
// ICS4U
// Tyler Liquornik
// January 20, 2022

package com.company;

// Imports
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public class Game extends JPanel implements KeyListener
{
    // Window Dimensions (always a square, therefore only one variable is needed)
    int windowDim = 750;

    // Background
    Tile bg = new Tile(Tile.BG, windowDim / 6, windowDim / 6, windowDim * 2 / 3, false);

    // Padding and Tile Side length
    int padding = windowDim / 75;
    int tileSideLength = (bg.sideLength - 5 * padding) / 4;

    // Keep track of where tiles are in the 4 by 4 grid;
    static Tile[][] tileGrid = new Tile[4][4];

    // Variables for when the player starts, and loses
    boolean started = true;
    boolean lost = false;

    // Variable if the player has clicked the quit button
    AtomicBoolean clickedQuit = new AtomicBoolean(false);

    // Variables for audio
    boolean muted = false;
    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("music.wav").getAbsoluteFile());
    Clip music = AudioSystem.getClip();

    // Mute and unmute button attributes
    int muteButtonSideLength = windowDim / 30;
    int muteButtonX = windowDim * 9 / 10 - muteButtonSideLength;
    int muteButtonY = windowDim * 9 / 10 - muteButtonSideLength;

    // Mute and unmute buttons
    SquareImage muteButton = new SquareImage(muteButtonX, muteButtonY, muteButtonSideLength,
            new ImageIcon("muted.png"));
    SquareImage unmuteButton = new SquareImage(muteButtonX, muteButtonY, muteButtonSideLength,
            new ImageIcon("unmuted.png"));

    // Get points at the correct coordinates that correspond to the 4 by 4 grid in game
    public Point gridEquivalent(int x, int y)
    {
        // Array for the points
        Point[][] points = new Point[4][4];

        // Loop through grid x coordinates
        for (int i = 0; i <= 3; i++)
        {
            // X position on screen at given grid coordinate
            int xPos = bg.x + padding + i * (padding + tileSideLength);

            // Loop through grid y coordinates
            for (int j = 0; j <= 3; j++)
            {
                // Y position on screen at given grid coordinate
                int yPos = bg.y + padding + j * (padding + tileSideLength);

                // Create the point and put it into the array at the correct index
                Point point = new Point(xPos, yPos);
                points[i][j] = point;
            }
        }

        // Return the desired point from the given grid coordinates
        return points[x - 1][y - 1];
    }

    // Random point on the 4 by 4 grid
    public Point randPoint()
    {
        // Random X and Y coordinates
        int randX = ThreadLocalRandom.current().nextInt(1, 5);
        int randY = ThreadLocalRandom.current().nextInt(1, 5);

        // Return a random point
        return new Point(randX, randY);
    }

    // Update which grid spots are occupied
    public void updateGrid(int x, int y, Tile tile)
    {
        tileGrid[x - 1][y - 1] = tile;
    }

    public void loseCheck() // change so the user doesn't lose if there are available moves (implement after up/down)
    {
        // Assume the grid is full
        boolean isGridFull = true;

        // Update the variable if the grid is not full
        for (int i = 0; i <= 3; i++)
        {
            for (int j = 0; j <= 3; j++)
            {
                if (tileGrid[i][j] == null)
                {
                    isGridFull = false;
                    break;
                }
            }

            if (!isGridFull)
                break;
        }

        // If the grid is full, the user loses
        if (isGridFull)
        {
            // Assume no possible moves
            boolean noPossibleMoves = true;

            // Check if there are any possible moves
            for (int i = 0; i <= 3; i++)
            {
                for (int j = 0; j <= 3; j++)
                {
                    // Selected tile
                    Tile selectedTile = tileGrid[i][j];

                    // Get the position of adjacent tiles and check if id's match making (meaning there is a valid move)
                    if (i >= 1)
                    {
                        Tile leftTile = tileGrid[i - 1][j];
                        if (selectedTile.id == leftTile.id)
                            noPossibleMoves = false;
                    }
                    if (i <= 2)
                    {
                        Tile rightTile = tileGrid[i + 1][j];
                        if (selectedTile.id == rightTile.id)
                            noPossibleMoves = false;
                    }
                    if (j >= 1)
                    {
                        Tile topTile = tileGrid[i][j - 1];
                        if (selectedTile.id == topTile.id)
                            noPossibleMoves = false;
                    }
                    if (j <= 2)
                    {
                        Tile bottomTile = tileGrid[i][j + 1];
                        if (selectedTile.id == bottomTile.id)
                            noPossibleMoves = false;
                    }
                }
            }

            // If there are no possible moves, the user loses
            if (noPossibleMoves)
            {
                lost = true;
                popUp("You Lose :(");
            }
        }
    }

    // Check if the user has won
    public void winCheck()
    {
        // Assume the user has not won
        boolean won = false;

        // Iterate through the grid
        for (int i = 0; i <= 3; i++)
        {
            for (int j = 0; j <= 3; j++)
            {
                // If there is a tile occupying the location being checked
                if (tileGrid[i][j] != null)
                {
                    // If the tile is a 2048 tile
                    if (tileGrid[i][j].id == 2048)
                    {
                        won = true;
                        break;
                    }
                }
            }
        }

        // Call the popup method if the user wins
        if (won)
            popUp("You Win!");
    }

    // Restart the game
    public void restart()
    {
        // Empty the tile grid
        tileGrid = new Tile[4][4];

        // The user has just started a fresh game and hasn't yet lost
        started = true;
        lost = false;
    }


    // Lose procedure
    public void popUp(String message)
    {
        // Make the menu window with the message as the title
        JFrame menu = new JFrame(message);
        menu.setSize(250, 125);

        // Layout Manager objects
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();

        // Make the menu panel with a layout manager
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(layout);

        // Restart Player button
        JButton restart = new JButton("Restart");
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        menuPanel.add(restart, constraints);

        // Quit button
        JButton quit = new JButton("Quit");
        constraints.gridx = 2;
        constraints.gridy = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        menuPanel.add(quit, constraints);

        // Action listener for the restart button
        restart.addActionListener
                (Click ->
                    {
                        // Close the menu and restart
                        menu.dispose();
                        restart();
                    }
                );

        // Action listener for the quit button
        quit.addActionListener
                (Click ->
                    {
                        // Close the menu and update the variable that keeps track of if the user has quit
                        menu.dispose();
                        clickedQuit.set(true);
                    }
                );

        // Add the panel to the window and make the window visible
        menu.add(menuPanel);
        menu.setVisible(true);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Generate a new tile
    public void generateTile()
    {
        // Tile number to spawn
        int spawnNumber;

        // 10% chance of spawning a 4, 90% chance of spawning a 2
        int randSpawn = ThreadLocalRandom.current().nextInt(1, 11);

        if (randSpawn == 10)
            spawnNumber = 4;
        else
            spawnNumber = 2;

        // Random point and screen coordinate equivalent
        Point point = randPoint();
        Point coords = gridEquivalent(point.x, point.y);

        // Check if the coordinate is already occupied
        if (tileGrid[point.x - 1][point.y - 1] == null)
        {
            // Create the tile and update the grid status
            Tile tile = new Tile(spawnNumber, coords.x, coords.y, tileSideLength, false);
            updateGrid(point.x, point.y, tile);
        }

        // Try to generate the tile in a new place
        else
            generateTile();
    }

    // Paints the background tiles
    public void paintbgTiles(Graphics g)
    {
        for (int i = 1; i <= 4; i++)
        {
            for (int j = 1; j <= 4; j++)
            {
                // Create and paint the tile at the correct grid coordinate
                Tile bgTile = new Tile(Tile.TILEBG, gridEquivalent(i, j).x, gridEquivalent(i, j).y,
                        tileSideLength, false);
                bgTile.paint(g);
            }
        }
    }

    // Paints the non-background tiles
    public void paintTiles(Graphics g)
    {

        for (int i = 0; i <= 3; i++)
        {
            for (int j = 0; j <= 3; j++)
            {
                if (tileGrid[i][j] != null)
                    tileGrid[i][j].paint(g);
            }
        }
    }

    // Paints and updates the mute/unmute button
    public void paintAudioButton(Graphics g) throws LineUnavailableException
    {
        // Paint the correct button
        if (muted)
            muteButton.paint(g);

        else
            unmuteButton.paint(g);
    }

    // Add the key listener to JPanel
    public Game() throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        super();
        super.setFocusable(true);
        super.addKeyListener(this);
    }

    // Update the game by checking for pressed keys, and repainting the screen
    public void update()
    {
        // Play music upon starting, and when the music ends in order to loop
        if (started || music.getFramePosition() >= music.getFrameLength())
        {
            music.stop();
            music.setFramePosition(0);
            music.start();
        }

        // Checks for key presses
        keyCheck();

        // Start the game with 2 tiles
        if (started)
        {
            generateTile();
            generateTile();
            started = false;
        }

        // Calls the paint method
        repaint();
    }

    // Clear the screen, then paint everything
    public void paint(Graphics g)
    {
        g.clearRect(0, 0, getWidth(), getHeight());
        bg.paint(g);
        paintbgTiles(g);
        paintTiles(g);

        try
        {
            paintAudioButton(g);
        }
        catch (LineUnavailableException e)
        {
            e.printStackTrace();
        }
    }

    // Sets hasMerged back to false for all tile
    public void mergeStatusReset()
    {
        // Reset all the tiles hasMerged to prevents multiple merges in a line
        for (int i = 0; i <= 3; i++)
        {
            for (int j = 0; j <= 3; j++)
            {
                if (tileGrid[i][j] != null)
                    tileGrid[i][j].hasMerged = false;
            }
        }
    }

    // Methods to move in each direction (only apply if the user hasn't lost)
    public void left()
    {
        // If the game is still ongoing
        if (!lost)
        {

            // To stop tile generation if no moves are successful, assuming no successful moves to start
            boolean stopGenerate = true;

            // Iterate horizontally from left to right
            for (int i = 1; i <= 4; i++)
            {
                // Iterate vertically (iteration direction irrelevant)
                for (int j = 1; j <= 4; j++)
                {
                    // Maximum distance left a tile can move (assuming no other tiles in the way)
                    int maxMoveDist = i - 1;

                    // Keep track of if the current tile has moved
                    boolean moved = false;

                    // Check how many spaces the tile can move to move by iteration from max to min
                    for (int k = maxMoveDist; k >= 0; k--)
                    {
                        // If the tile is touching not the edge nor another tile
                        if (maxMoveDist != 0)
                        {
                            // Possible New Coords
                            Point newCoords = gridEquivalent(i - k, j);

                            // If there is a tile at the initial/old location, and no tile and the final/new location
                            if (tileGrid[i - 1][j - 1] != null && tileGrid[i - k - 1][j - 1] == null)
                            {
                                // Old and new tiles (with different locations)
                                Tile oldTile = tileGrid[i - 1][j - 1];
                                Tile newTile = new Tile (oldTile.id, newCoords.x, newCoords.y,
                                        tileSideLength, false);

                                // Move the tile to new coordinates and set the initial coords to null
                                updateGrid(i - k, j, newTile);
                                updateGrid(i, j, null);

                                // Update the moved variable
                                moved = true;

                                // Update the stopGenerate variable
                                stopGenerate = false;
                            }
                        }

                        // Try to merge the tile with the tile next to it
                        try
                        {
                            // If neither tiles have been merged already, and they have the same id
                            if (!(tileGrid[i - k - 1][j - 1].hasMerged ||  tileGrid[i - k - 2][j - 1].hasMerged) &&
                                    tileGrid[i - k - 2][j - 1].id == tileGrid[i - k - 1][j - 1].id)
                            {
                                // Merge the tiles
                                Tile tile = new Tile(tileGrid[i - k - 2][j - 1].id * 2,
                                        gridEquivalent(i - k - 1, j).x,
                                        gridEquivalent(i - k - 1, j).y, tileSideLength, true);
                                updateGrid(i - k - 1, j, tile);
                                updateGrid(i - k, j, null);

                                // Update the stopGenerate variable
                                stopGenerate = false;
                            }
                        }

                        // Ignore if the try block fails
                        catch (Exception ignored)
                        {
                        }

                        // Stop checking if the tile has moved
                        if (moved)
                            break;
                    }
                }
            }

            // Generate a new tile, and check if this makes the player lose as long as a move has been executed
            if (!stopGenerate)
            {
                generateTile();
                winCheck();
                loseCheck();
            }
        }
    }

    public void right()
    {
        // If the game is still ongoing
        if (!lost)
        {
            // To stop tile generation if no moves are successful, assuming no successful moves to start
            boolean stopGenerate = true;

            // Iterate horizontally from right to left
            for (int i = 4; i >= 1; i--)
            {
                // Iterate vertically (iteration direction irrelevant)
                for (int j = 1; j <= 4; j++)
                {
                    // Maximum distance right a tile can move (assuming no other tiles in the way)
                    int maxMoveDist = 4 - i;

                    // Keep track of if the current tile has moved
                    boolean moved = false;

                    // Check how many spaces the tile can move to move by iteration from max to min
                    for (int k = maxMoveDist; k >= 0; k--)
                    {
                        // If the tile is touching not the edge nor another tile
                        if (maxMoveDist != 0)
                        {
                            // Possible New Coords
                            Point newCoords = gridEquivalent(i + k, j);

                            // If there is a tile at the initial/old location, and no tile and the final/new location
                            if (tileGrid[i - 1][j - 1] != null && tileGrid[i + k - 1][j - 1] == null)
                            {
                                // Old and new tiles (with different locations)
                                Tile oldTile = tileGrid[i - 1][j - 1];
                                Tile newTile = new Tile (oldTile.id, newCoords.x, newCoords.y,
                                        tileSideLength, false);

                                // Move the tile to new coordinates and set the initial coords to null
                                updateGrid(i + k, j, newTile);
                                updateGrid(i, j, null);

                                // Update the moved variable
                                moved = true;

                                // Update the stopGenerate variable
                                stopGenerate = false;
                            }
                        }

                        // Try to merge the tile with the tile next to it
                        try
                        {
                            // If neither tiles have been merged already, and they have the same id
                            if (!(tileGrid[i + k - 1][j - 1].hasMerged || tileGrid[i + k][j - 1].hasMerged) &&
                                    tileGrid[i + k][j - 1].id == tileGrid[i + k - 1][j - 1].id)
                            {
                                // Merge the tiles
                                Tile tile = new Tile(tileGrid[i + k - 1][j - 1].id * 2,
                                        gridEquivalent(i + k + 1, j).x,
                                        gridEquivalent(i + k + 1, j).y, tileSideLength, true);
                                updateGrid(i + k + 1, j, tile);
                                updateGrid(i + k, j, null);

                                // Update the stopGenerate variable
                                stopGenerate = false;
                            }
                        }

                        // Ignore if the try block fails
                        catch (Exception ignored)
                        {
                        }

                        // Stop checking if the tile has moved
                        if (moved)
                            break;
                    }
                }
            }

            // Generate a new tile, and check if this makes the player lose as long as a move has been executed
            if (!stopGenerate)
            {
                generateTile();
                winCheck();
                loseCheck();
            }
        }
    }

    public void up()
    {
        // If the game is still ongoing
        if (!lost)
        {
            // To stop tile generation if no moves are successful, assuming no successful moves to start
            boolean stopGenerate = true;

            // Iterate vertically (iteration direction irrelevant)
            for (int j = 1; j <= 4; j++)
            {
                // Iterate horizontally (iteration direction irrelevant)
                for (int i = 1; i <= 4; i++)
                {
                    // Maximum distance up a tile can move (assuming no other tiles in the way)
                    int maxMoveDist = j - 1;

                    // Keep track of if the current tile has moved
                    boolean moved = false;

                    // Check how many spaces the tile can move to move by iteration from max to min
                    for (int k = maxMoveDist; k >= 0; k--)
                    {
                        // If the tile is touching not the edge nor another tile
                        if (maxMoveDist != 0)
                        {
                            // Possible New Coords
                            Point newCoords = gridEquivalent(i, j - k);

                            // If there is a tile at the initial/old location, and no tile and the final/new location
                            if (tileGrid[i - 1][j - 1] != null && tileGrid[i - 1][j - k - 1] == null)
                            {
                                // Old and new tiles (with different locations)
                                Tile oldTile = tileGrid[i - 1][j - 1];
                                Tile newTile = new Tile (oldTile.id, newCoords.x, newCoords.y,
                                        tileSideLength, false);

                                // Move the tile to new coordinates and set the initial coords to null
                                updateGrid(i, j - k, newTile);
                                updateGrid(i, j, null);

                                // Update the moved variable
                                moved = true;

                                // Update the stopGenerate variable
                                stopGenerate = false;
                            }
                        }

                        // Try to merge the tile with the tile next to it
                        try
                        {
                            // If neither tiles have been merged already, and they have the same id
                            if (!(tileGrid[i - 1][j - k - 1].hasMerged || tileGrid[i - 1][j - k - 2].hasMerged) &&
                                    tileGrid[i - 1][j - k - 1].id == tileGrid[i - 1][j - k - 2].id)
                            {
                                // Merge the tiles
                                Tile tile = new Tile(tileGrid[i - 1][j - k - 2].id * 2,
                                        gridEquivalent(i, j - k - 1).x,
                                        gridEquivalent(i, j - k - 1).y, tileSideLength, true);
                                updateGrid(i, j - k - 1, tile);
                                updateGrid(i, j - k, null);

                                // Update the stopGenerate variable
                                stopGenerate = false;
                            }
                        }

                        // Ignore if the try block fails
                        catch (Exception ignored)
                        {
                        }

                        // Stop checking if the tile has moved
                        if (moved)
                            break;
                    }
                }
            }

            // Generate a new tile, and check if this makes the player lose as long as a move has been executed
            if (!stopGenerate)
            {
                generateTile();
                winCheck();
                loseCheck();
            }
        }
    }

    public void down()
    {
        // If the game is still ongoing
        if (!lost)
        {
            // To stop tile generation if no moves are successful, assuming no successful moves to start
            boolean stopGenerate = true;

            // Iterate vertically from bottom to top
            for (int j = 4; j >= 1; j--)
            {
                // Iterate horizontally (iteration direction irrelevant)
                for (int i = 1; i <= 4; i++)
                {
                    // Maximum distance down a tile can move (assuming no other tiles in the way)
                    int maxMoveDist = 4 - j;

                    // Keep track of if the current tile has moved
                    boolean moved = false;

                    // Check how many spaces the tile can move to move by iteration from max to min
                    for (int k = maxMoveDist; k >= 0; k--)
                    {
                        // If the tile is touching not the edge nor another tile
                        if (maxMoveDist != 0)
                        {
                            // Possible New Coords
                            Point newCoords = gridEquivalent(i, j + k);

                            // If there is a tile at the initial/old location, and no tile and the final/new location
                            if (tileGrid[i - 1][j - 1] != null && tileGrid[i - 1][j + k - 1] == null)
                            {
                                // Old and new tiles (with different locations)
                                Tile oldTile = tileGrid[i - 1][j - 1];
                                Tile newTile = new Tile (oldTile.id, newCoords.x, newCoords.y,
                                        tileSideLength, false);

                                // Move the tile to new coordinates and set the initial coords to null
                                updateGrid(i, j + k, newTile);
                                updateGrid(i, j, null);

                                // Update the moved variable
                                moved = true;

                                // Update the stopGenerate variable
                                stopGenerate = false;
                            }
                        }

                        // Try to merge the tile with the tile next to it
                        try
                        {
                            // If neither tiles have been merged already, and they have the same id
                            if (!(tileGrid[i - 1][j + k - 1].hasMerged || tileGrid[i - 1][j + k].hasMerged) &&
                                    tileGrid[i - 1][j + k - 1].id == tileGrid[i - 1][j + k].id)
                            {
                                // Merge the tiles
                                Tile tile = new Tile(tileGrid[i - 1][j + k - 1].id * 2,
                                        gridEquivalent(i,j + k + 1).x,
                                        gridEquivalent(i,j + k + 1).y, tileSideLength, true);
                                updateGrid(i, j + k + 1, tile);
                                updateGrid(i, j + k, null);

                                // Update the stopGenerate variable
                                stopGenerate = false;
                            }
                        }

                        // Ignore if the try block fails
                        catch (Exception ignored)
                        {
                        }

                        // Stop checking if the tile has moved
                        if (moved)
                            break;
                    }
                }
            }

            // Generate a new tile, and check if this makes the player lose as long as a move has been executed
            if (!stopGenerate)
            {
                generateTile();
                loseCheck();
            }
        }
    }

    // Variables to keep track of buttons pressed
    boolean left;
    boolean right;
    boolean up;
    boolean down;

    // Check for key presses, resetting them and the mergeStatus of all tiles
    public void keyCheck()
    {
        if (left)
        {
            left();
            mergeStatusReset();
            left = false;
        }

        else if (right)
        {
            right();
            mergeStatusReset();
            right = false;
        }

        else if (up)
        {
            up();
            mergeStatusReset();
            up = false;
        }

        else if (down)
        {
            down();
            mergeStatusReset();
            down = false;
        }
    }

    // Check for released keys (released rather than pressed in order to prevent spamming)
    @Override
    public void keyReleased(KeyEvent e)
    {
        // Controls (WASD or arrow keys)
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A)
            left = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D)
            right = true;
        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W)
            up = true;
        if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S)
            down = true;

        // M for mute and unmute
        if (e.getKeyCode() == KeyEvent.VK_M)
        {

            // Mute or unmute depending on the current state
            if (muted)
            {
                muted = false;
                music.start();
            }

            else
            {
                muted = true;
                music.stop();
                music.setFramePosition(0);
            }
        }
    }

    // Blank override methods for keyListener (needed to not cause an error)
    @Override
    public void keyPressed(KeyEvent e)
    {
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
    }
}
