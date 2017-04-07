package ru.mikensk.mathlife.core;

import java.awt.*;
import java.time.LocalTime;
import java.util.Random;

/**
 * Ядро (модель) игры "Жизнь"
 */
public class LifeCore implements LifeInterface {
    private boolean[][] nextState;
    private boolean[][] cells;
    private GameMap map;
    private int numAliveCells = 0;
    private int step = 0;

    private int maxX;
    private int maxY;

    private boolean periodicFlag = false;
    private int[] xCoordinates = new int[3];
    private int[] yCoordinates = new int[3];

    @Override
    public void setMapSize(int xSize, int ySize) {
        nextState = new boolean[xSize][ySize];
        cells = new boolean[xSize][ySize];
        map = new GameMap(cells);

        maxX = xSize - 1;
        maxY = ySize - 1;
    }

    @Override
    public void setPeriodic(boolean periodicFlag) {
        this.periodicFlag = periodicFlag;
    }

    @Override
    public boolean isPeriodic() {
        return periodicFlag;
    }

    @Override
    public void clear() {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                cells[i][j] = false;
            }
        }
        numAliveCells = 0;
        step = 0;
    }

    @Override
    public void init() {
        init(0.1);
    }

    @Override
    public void init(double ratio) {
        if (ratio >= 1) {
            throw new IllegalArgumentException("ratio >= 1 : ratio = " + ratio);
        }

        int size = 1000;
        int threshold = (int) ((1 - ratio) * size);
        Random random = new Random(LocalTime.now().toNanoOfDay());

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                if (random.nextInt(size) > threshold) {
                    cells[i][j] = true;
                }
            }
        }

        numAliveCells = map.getNumAliveCells();
    }

    private void checkPoint(Point point) {
        if (point.x < 0) {
            throw new IllegalArgumentException("point.x < 0 : point.x = " + point.x);
        }
        if (point.y < 0) {
            throw new IllegalArgumentException("point.y < 0 : point.y = " + point.y);
        }
        if (point.x >= cells.length) {
            throw new IllegalArgumentException("point.x >= xSize : point.x = " + point.x);
        }
        if (point.y >= cells[0].length) {
            throw new IllegalArgumentException("point.y >= ySize : point.y = " + point.y);
        }
    }

    private void addPoint(Point point) {
        checkPoint(point);
        cells[point.x][point.y] = true;
    }

    @Override
    public void addPoints(Point[] points) {
        for (Point p : points) {
            addPoint(p);
        }
        numAliveCells = map.getNumAliveCells();
    }

    private void clearPoint(Point point) {
        checkPoint(point);
        cells[point.x][point.y] = false;
    }

    @Override
    public void clearPoints(Point[] points) {
        for (Point p : points) {
            clearPoint(p);
        }
        numAliveCells = map.getNumAliveCells();
    }

    private int calcAroundSimple(int xPosition, int yPosition) {
        int xMin;
        int xMax = maxX;
        int yMin;
        int yMax = maxY;

        if (xPosition < xMax) {
            xMax = xPosition + 1;
        }
        if (yPosition < yMax) {
            yMax = yPosition + 1;
        }

        if (xPosition == 0) {
            xMin = 0;
        } else {
            xMin = xPosition - 1;
        }
        if (yPosition == 0) {
            yMin = 0;
        } else {
            yMin = yPosition - 1;
        }

        int count = 0;
        for (int i = xMin; i <= xMax; i++) {
            for (int j = yMin; j <= yMax; j++) {
                if ((i != xPosition) || (j != yPosition)) {
                    if (cells[i][j]) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private int calcAliveCellsAround(int xPosition, int yPosition) {
        if (periodicFlag) {
            return calcAroundPeriodic(xPosition, yPosition);
        } else {
            return calcAroundSimple(xPosition, yPosition);
        }
    }

    private int calcAroundPeriodic(int xPosition, int yPosition) {
        if (xPosition > 0 && yPosition > 0 && xPosition < maxX && yPosition < maxY) {
            return calcAroundSimple(xPosition, yPosition);
        } else {
            for (int x = xPosition - 1, i = 0; i < 3; x++, i++) {
                if (x < 0) {
                    xCoordinates[i] = maxX;
                } else if (x > maxX) {
                    xCoordinates[i] = 0;
                } else {
                    xCoordinates[i] = x;
                }
            }

            for (int y = yPosition - 1, i = 0; i < 3; y++, i++) {
                if (y < 0) {
                    yCoordinates[i] = maxY;
                } else if (y > maxY) {
                    yCoordinates[i] = 0;
                } else {
                    yCoordinates[i] = y;
                }
            }

            int count = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (j != 1 || i != 1) {
                        if (cells[xCoordinates[i]][yCoordinates[j]]) {
                            count++;
                        }
                    }
                }
            }
            return count;
        }
    }

    @Override
    public void doStep() {
        if (numAliveCells == 0) {
            return;
        }

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                int numAliveCells = calcAliveCellsAround(i, j);
                if (cells[i][j]) {
                    nextState[i][j] = (numAliveCells == 2 || numAliveCells == 3);
                } else {
                    nextState[i][j] = (numAliveCells == 3);
                }
            }
        }

        for (int i = 0; i < cells.length; i++) {
            System.arraycopy(nextState[i], 0, cells[i], 0, cells[i].length);
        }

        numAliveCells = map.getNumAliveCells();
        step++;
    }

    @Override
    public void doSteps(int numSteps) {
        for (int i = 0; i < numSteps; i++) {
            if (numAliveCells == 0) {
                break;
            } else {
                doStep();
            }
        }
    }

    @Override
    public GameMap getMap() {
        return map;
    }

    @Override
    public int getStep() {
        return step;
    }

    @Override
    public int getNumAliveCells() {
        return numAliveCells;
    }
}
