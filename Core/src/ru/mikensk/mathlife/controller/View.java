package ru.mikensk.mathlife.controller;

import ru.mikensk.mathlife.core.GameMap;

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
