package ru.mikensk.mathlife.swing;

import ru.mikensk.mathlife.core.GameMap;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_BYTE_BINARY;
import static java.awt.image.BufferedImage.TYPE_BYTE_GRAY;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;

/**
 * Created by UserLabView on 05.04.17.
 */
public class MapPanel extends JPanel {
    private int xSize;
    private int ySize;
    private GameMap map;
    private BufferedImage image;
    private int cellSize;

    public MapPanel(int cellSize) {
        super();
        this.cellSize = cellSize;
    }

    public void setSize(int xSize, int ySize, GameMap map) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.map = map;
        image = new BufferedImage(xSize * cellSize, ySize * cellSize, TYPE_INT_RGB);

        image.getGraphics().setColor(Color.GREEN);
        for (int i = 0; i < Math.min(xSize, ySize); i++) {
            image.getGraphics().fillRect(i * cellSize, i * cellSize, cellSize, cellSize);
        }

        updateMap();
    }

    public void updateMap() {
        if (map != null && image != null) {
            for (int i = 0; i < xSize; i++) {
                for (int j = 0; j < ySize; j++) {
                    if (map.getCell(i, j)) {
                        image.getGraphics().setColor(Color.BLACK);
                    } else {
                        image.getGraphics().setColor(Color.WHITE);
                    }
                    image.getGraphics().fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                }
            }
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
