package ru.mikensk.mathlife.controller;

import ru.mikensk.mathlife.core.GameMap;

import java.awt.*;

/**
 * Интерфейс подписчика на события представления для игры Жизнь
 */
public interface ViewListener {
    void setMapSize(int xSize, int ySize);

    void clear();

    void init();

    void addPoints(Point[] points);

    void doSteps(int numSteps);

    GameMap getMap();

    int getStep();
}
