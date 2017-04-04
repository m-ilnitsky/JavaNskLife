package ru.mikensk.mathlife.controller;

import ru.mikensk.mathlife.core.GameMap;
import ru.mikensk.mathlife.core.LifeInterface;

import java.awt.*;

/**
 * Контроллер для игры "Жизнь"
 */
public class Controller implements ViewListener {
    private LifeInterface model;
    private View view;

    public Controller(LifeInterface model, View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void setMapSize(int xSize, int ySize) {
        model.setMapSize(xSize, ySize);
        view.setMap(model.getMap());
    }

    @Override
    public void clear() {
        model.clear();
        view.updateMap();
        view.stopTimer();
    }

    @Override
    public void init() {
        model.init();
        view.updateMap();
        view.startTimer();
    }

    @Override
    public void addPoints(Point[] points) {
        model.addPoints(points);
        view.updateMap();
        view.startTimer();
    }

    @Override
    public void doSteps(int numSteps) {
        model.doSteps(numSteps);
        view.updateMap();
    }

    @Override
    public GameMap getMap() {
        return model.getMap();
    }

    @Override
    public int getStep() {
        return model.getStep();
    }
}
