package ru.ilnitsky.nsk.java.mathlife.controller;

import ru.ilnitsky.nsk.java.mathlife.core.GameMap;

/**
 * Интерфейс представления для игры "Жизнь"
 */
public interface View {
    void setViewListener(ViewListener listener);

    void setMap(GameMap gameMap);

    void startApplication();

    void startTimer();

    void stopTimer();

    void update();
}
