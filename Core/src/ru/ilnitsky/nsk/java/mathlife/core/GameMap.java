package ru.ilnitsky.nsk.java.mathlife.core;

import java.awt.*;

/**
 * Игровая карта для игры "Жизнь"
 */
public class GameMap {
    private boolean[][] cells;

    public GameMap(boolean[][] cells) {
        this.cells = cells;
    }

    public GameMap(int xSize, int ySize) {
        cells = new boolean[xSize][ySize];
    }

    public int getXSize() {
        return cells.length;
    }

    public int getYSize() {
        return cells[0].length;
    }

    public int getNumAliveCells() {
        int count = 0;
        for (boolean[] cc : cells) {
            for (boolean c : cc) {
                if (c) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean getCell(Point point) {
        return getCell(point.x, point.y);
    }

    public boolean getCell(int x, int y) {
        return cells[x][y];
    }
}
