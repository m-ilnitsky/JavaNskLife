package ru.mikensk.mathlife.core;

import java.awt.*;

/**
 * Интерфейс модели игры "Жизнь"
 */
public interface LifeInterface {
    void setMapSize(int xSize, int ySize);

    void clear();

    void init();

    void init(double ratio);

    void addPoint(Point point);

    void addPoints(Point[] points);

    void doStep();

    void doSteps(int numSteps);

    GameMap getMap();

    int getStep();
}
