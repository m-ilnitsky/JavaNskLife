package ru.mikensk.mathlife.controller;

import ru.mikensk.mathlife.core.GameMap;

/**
 * Интерфейс представления для игры "Жизнь"
 */
public interface View {
    void addViewListener(ViewListener listener);

    void removeViewListener();

    void setMap(GameMap gameMap);

    void startTimer();

    void stopTimer();

    void updateMap();
}
