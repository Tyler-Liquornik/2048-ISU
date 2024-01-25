// ISU Project
// ImageButton class for ISU
// Mr. Naccarato
// ICS4U
// Tyler Liquornik
// January 20, 2022

package com.company;

// Imports
import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;

public class SquareImage
{
    // ImageButton's attributes
    int x;
    int y;
    int sideLength;
    ImageIcon imageIcon;

    public SquareImage(int x, int y, int sideLength, ImageIcon imageIcon)
    {
        // Access to parameters
        this.x = x;
        this.y = y;
        this.sideLength = sideLength;
        this.imageIcon = imageIcon;
    }

    // Paint method
    public void paint(Graphics g)
    {
        Image image = imageIcon.getImage();
        ImageObserver imageObserver = (img, infoflags, x, y, width, height) -> false;
        g.drawImage(image, x, y, sideLength, sideLength, imageObserver);
    }
}
