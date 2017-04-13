package ru.ilnitsky.nsk.java.mathlife.controller;

import ru.ilnitsky.nsk.java.mathlife.core.GameMap;

import java.awt.*;

/**
 * Интерфейс подписчика на события представления для игры "Жизнь"
 */
public interface ViewListener {
    void setMapSize(int xSize, int ySize);

    void setPeriodic(boolean periodicFlag);

    boolean isPeriodic();

    void clear();

    void init();

    void init(double ratio);

    void addPoints(Point[] points);

    void clearPoints(Point[] points);

    void doSteps(int numSteps);

    GameMap getMap();

    int getStep();

    int getNumAliveCells();
}
