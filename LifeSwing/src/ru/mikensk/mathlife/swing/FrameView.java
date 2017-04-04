package ru.mikensk.mathlife.swing;

import ru.mikensk.mathlife.controller.ViewListener;
import ru.mikensk.mathlife.core.GameMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Графический интерфейс (представление) на основе Swing для игры "Жизнь"
 */
public class FrameView implements ViewAutoCloseable {
    private ViewListener core;
    private GameMap gameMap;

    private final static int cellSize = 4;

    int height;
    int width;
    int xSize;
    int ySize;

    private final Timer timer = new Timer(10, l -> doStep());

    private final static String stepStr = "Шаг: ";
    private final static String numStr = "Количество живых ячеек: ";

    private final JFrame frame = new JFrame(":Жизнь");
    private final JPanel mapPanel = new JPanel();
    private final JPanel infoPanel = new JPanel();
    private final JLabel stepLabel = new JLabel(stepStr);
    private final JLabel numLabel = new JLabel(numStr);

    public FrameView(int width, int height) {
        this.width = width;
        this.height = height;
    }

    private void initInfoPanel() {
        infoPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 16, 8));

        Font groupFont = new Font("TimesRoman", Font.BOLD, 14);

        stepLabel.setFont(groupFont);
        numLabel.setFont(groupFont);

        infoPanel.add(stepLabel);
        infoPanel.add(numLabel);

        infoPanel.setVisible(true);
    }

    private void initMapPanel() {
        mapPanel.setVisible(true);
    }

    private void initFrame() {
        frame.add(infoPanel, BorderLayout.NORTH);
        frame.add(mapPanel, BorderLayout.CENTER);

        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        Dimension dimension = new Dimension(width, height);

        frame.setMinimumSize(dimension);
        frame.setMaximumSize(dimension);

        frame.setVisible(true);
    }

    private void setMapSize() {
        xSize = (int) (mapPanel.getSize().getWidth() / cellSize);
        ySize = (int) (mapPanel.getSize().getHeight() / cellSize);
        core.setMapSize(xSize, ySize);
    }

    @Override
    public void startApplication() {
        SwingUtilities.invokeLater(() -> {
            initInfoPanel();
            initMapPanel();
            initFrame();

            setMapSize();
            core.init();

            timer.start();
        });
    }

    @Override
    public void close() throws Exception {
        frame.setVisible(false);
    }

    @Override
    public void setViewListener(ViewListener listener) {
        core = listener;
    }

    @Override
    public void setMap(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    @Override
    public void startTimer() {
        timer.start();
    }

    @Override
    public void stopTimer() {
        timer.stop();
    }

    @Override
    public void update() {
        stepLabel.setText(stepStr+core.getStep());
        numLabel.setText(numStr+core.getNumAliveCells());
    }

    private void doStep() {
        if (core.getNumAliveCells() > 0) {
            core.doSteps(1);
        }
    }
}
