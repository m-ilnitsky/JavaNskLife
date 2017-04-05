package ru.mikensk.mathlife.swing;

import ru.mikensk.mathlife.controller.Controller;
import ru.mikensk.mathlife.core.LifeCore;

/**
 * Программа "Жизнь" с графическим интерфейсом на основе Swing
 */
public class Application {
    public static void main(String[] args) {
        try (ViewAutoCloseable view = new FrameView(400, 500, 6)) {
            LifeCore lifeCore = new LifeCore();

            Controller controller = new Controller(lifeCore, view);

            view.setViewListener(controller);
            view.startApplication();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
