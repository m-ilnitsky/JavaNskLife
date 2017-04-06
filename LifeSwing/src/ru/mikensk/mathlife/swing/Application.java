package ru.mikensk.mathlife.swing;

import ru.mikensk.mathlife.controller.Controller;
import ru.mikensk.mathlife.core.LifeCore;

/**
 * Программа "Жизнь" с графическим интерфейсом на основе Swing
 */
public class Application {
    public static void main(String[] args) {
        SizeDialog sizeDialog = new SizeDialog(640, 480, 8);
        sizeDialog.showDialog();

        while (sizeDialog.isVisible()) ;

        GameSize gameSize = sizeDialog.getGameSize();

        try (ViewAutoCloseable view = new FrameView(gameSize.getWidth(), gameSize.getHeight(), gameSize.getCellSize())) {
            LifeCore lifeCore = new LifeCore();

            Controller controller = new Controller(lifeCore, view);

            view.setViewListener(controller);
            view.startApplication();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
