// ISU
// Tile class for ISU
// Mr. Naccarato
// ICS4U
// Tyler Liquornik
// January 20, 2022

package com.company;

// Imports
import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;

public class Tile
{
    // Tile's attributes
    int x;
    int y;
    int id;
    int sideLength;
    boolean hasMerged;
    ImageIcon selectedTile;

    // Variables for IDs
    final static int BG = 0;
    final static int TILEBG = 1;

    public Tile (int id, int x, int y, int sideLength, boolean hasMerged)
    {
        // Unscaled ImageIcons
        ImageIcon bg = new ImageIcon("bg.jpeg");
        ImageIcon tilebg = new ImageIcon("tilebg.jpeg");
        ImageIcon tile2 = new ImageIcon("tile2.jpeg");
        ImageIcon tile4 = new ImageIcon("tile4.jpeg");
        ImageIcon tile8 = new ImageIcon("tile8.jpeg");
        ImageIcon tile16 = new ImageIcon("tile16.jpeg");
        ImageIcon tile32 = new ImageIcon("tile32.jpeg");
        ImageIcon tile64 = new ImageIcon("tile64.jpeg");
        ImageIcon tile128 = new ImageIcon("tile128.jpeg");
        ImageIcon tile256 = new ImageIcon("tile256.jpeg");
        ImageIcon tile512 = new ImageIcon("tile512.jpeg");
        ImageIcon tile1024 = new ImageIcon("tile1024.jpeg");
        ImageIcon tile2048 = new ImageIcon("tile2048.jpeg");

        // List of tiles
        ImageIcon[] tiles = {bg, tilebg, tile2, tile4, tile8, tile16, tile32, tile64,
                tile128, tile256, tile512, tile1024, tile2048};

        // Select the correct tile (by id)
        if (id == BG)
            selectedTile = tiles[0];
        if (id == TILEBG)
            selectedTile = tiles[1];
        if (id == 2)
            selectedTile = tiles[2];
        if (id == 4)
            selectedTile = tiles[3];
        if (id == 8)
            selectedTile = tiles[4];
        if (id == 16)
            selectedTile = tiles[5];
        if (id == 32)
            selectedTile = tiles[6];
        if (id == 64)
            selectedTile = tiles[7];
        if (id == 128)
            selectedTile = tiles[8];
        if (id == 256)
            selectedTile = tiles[9];
        if (id == 512)
            selectedTile = tiles[10];
        if (id == 1024)
            selectedTile = tiles[11];
        if (id == 2048)
            selectedTile = tiles[12];

        // Access to parameters
        this.x = x;
        this.y = y;
        this.id = id;
        this.sideLength = sideLength;
        this.hasMerged = hasMerged;
    }

    // Paint method
    public void paint(Graphics g)
    {
        Image tile = selectedTile.getImage();
        ImageObserver imageObserver = (img, infoflags, x, y, width, height) -> false;
        g.drawImage(tile, x, y, sideLength, sideLength, imageObserver);
    }
}
