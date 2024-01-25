// ISU Project
// Main for 2048
// Mr. Naccarato
// ICS4U
// Tyler Liquornik & Kirill Dairy
// December 6, 2021

package com.company;

// Imports
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws UnsupportedAudioFileException, LineUnavailableException, IOException
    {
        // Make the game object
        Game game = new Game();

        // Make the window and add the game to the window
        JFrame frame = new JFrame("2048");
        frame.setSize(game.windowDim, game.windowDim);
        frame.add(game);

        // Make the window visible, and make it exit when the X button is pressed
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Start the music
        game.music.open(game.audioInputStream);

        // Game loop
        while(true)
        {
            game.update();

            // Close the game if the player quits
            if (game.clickedQuit.get())
                frame.dispose();

            // Screen Refresh (set to about 58.8Hz as this is the closest possible to get to 60Hz)
            try
            {
                Thread.sleep(17);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
                break;
            }
        }
    }
}
