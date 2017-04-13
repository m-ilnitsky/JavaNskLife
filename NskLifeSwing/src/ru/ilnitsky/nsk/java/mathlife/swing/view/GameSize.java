package ru.ilnitsky.nsk.java.mathlife.swing.view;

/**
 * Размеры окна и ячейки для игры "Жизнь"
 */
public class GameSize {
    private int width;
    private int height;
    private int cellSize;

    public GameSize(int width, int height, int cellSize) {
        this.width = width;
        this.height = height;
        this.cellSize = cellSize;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getCellSize() {
        return cellSize;
    }
}
