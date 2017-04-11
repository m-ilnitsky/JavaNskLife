package ru.mikensk.mathlife.swing;

import ru.mikensk.mathlife.controller.Controller;
import ru.mikensk.mathlife.core.LifeCore;
import ru.mikensk.mathlife.swing.view.FrameView;
import ru.mikensk.mathlife.swing.view.GameSize;
import ru.mikensk.mathlife.swing.view.SizeDialog;
import ru.mikensk.mathlife.swing.view.ViewAutoCloseable;

import javax.swing.*;

/**
 * Программа "Жизнь" с графическим интерфейсом на основе Swing
 */
public class Application {
    public static void main(String[] args) {
        JFrame sizeFrame = new JFrame("Невидимый фрейм");
        sizeFrame.setVisible(false);

        SizeDialog sizeDialog = new SizeDialog(sizeFrame);
        sizeDialog.initSize(800, 600, 8);

        GameSize gameSize = sizeDialog.showDialog();

        sizeDialog = null;
        sizeFrame = null;

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
