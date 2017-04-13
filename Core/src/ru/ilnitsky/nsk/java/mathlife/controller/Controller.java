package ru.ilnitsky.nsk.java.mathlife.controller;

import ru.ilnitsky.nsk.java.mathlife.core.GameMap;
import ru.ilnitsky.nsk.java.mathlife.core.LifeInterface;

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
    public void setPeriodic(boolean periodicFlag) {
        model.setPeriodic(periodicFlag);
    }

    @Override
    public boolean isPeriodic() {
        return model.isPeriodic();
    }

    @Override
    public void clear() {
        model.clear();
        view.update();
        view.stopTimer();
    }

    @Override
    public void init() {
        model.init();
        view.update();
        view.startTimer();
    }

    @Override
    public void init(double ratio) {
        model.init(ratio);
        view.update();
        view.startTimer();
    }

    @Override
    public void addPoints(Point[] points) {
        model.addPoints(points);
        view.update();
        view.startTimer();
    }

    @Override
    public void clearPoints(Point[] points) {
        model.clearPoints(points);
        view.update();
        view.startTimer();
    }

    @Override
    public void doSteps(int numSteps) {
        model.doSteps(numSteps);
        view.update();
    }

    @Override
    public GameMap getMap() {
        return model.getMap();
    }

    @Override
    public int getStep() {
        return model.getStep();
    }

    @Override
    public int getNumAliveCells() {
        return model.getNumAliveCells();
    }
}
