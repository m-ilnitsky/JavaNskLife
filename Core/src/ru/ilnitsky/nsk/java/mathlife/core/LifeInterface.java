package ru.ilnitsky.nsk.java.mathlife.core;

import java.awt.*;

/**
 * Интерфейс модели игры "Жизнь"
 */
public interface LifeInterface {
    void setMapSize(int xSize, int ySize);

    void setPeriodic(boolean periodicFlag);

    boolean isPeriodic();

    void clear();

    void init();

    void init(double ratio);

    void addPoints(Point[] points);

    void clearPoints(Point[] points);

    void doStep();

    void doSteps(int numSteps);

    GameMap getMap();

    int getStep();

    int getNumAliveCells();
}
