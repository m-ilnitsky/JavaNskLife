package ru.mikensk.mathlife.swing;

import ru.mikensk.mathlife.controller.View;
import ru.mikensk.mathlife.controller.ViewListener;
import ru.mikensk.mathlife.core.GameMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static java.awt.image.BufferedImage.*;

/**
 * Панель для отображения карты игры "Жизнь"
 */
public class MapPanel extends JPanel {
    private int cellSize;
    private int xSize;
    private int ySize;
    private GameMap map;
    private BufferedImage image;
    private Graphics2D graphics;
    private View view;
    private boolean inputKey;
    private ArrayList<Point> points;

    public MapPanel(int cellSize, View view) {
        super();
        this.cellSize = cellSize;
        this.view = view;
        inputKey = false;
    }

    public void init(int xSize, int ySize, GameMap map, ViewListener core) {
        this.xSize = xSize;
        this.ySize = ySize;

        this.map = map;
        image = new BufferedImage(xSize * cellSize, ySize * cellSize, TYPE_INT_RGB);
        graphics = (Graphics2D) image.getGraphics();
        updateMap();

        addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (inputKey && points != null) {
                    Point panelPoint = e.getPoint();
                    Point point = new Point((int) (1.0 * panelPoint.x / getWidth() * xSize), (int) (1.0 * panelPoint.y / getHeight() * ySize));
                    points.add(point);
                    addPointToMap(point);
                }
            }
        });

        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                view.stopTimer();
                inputKey = true;
                points = new ArrayList<>(64);

                Point panelPoint = e.getPoint();
                Point point = new Point((int) (1.0 * panelPoint.x / getWidth() * xSize), (int) (1.0 * panelPoint.y / getHeight() * ySize));
                points.add(point);
                addPointToMap(point);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (inputKey && points != null) {
                    inputKey = false;
                    core.addPoints(points.toArray(new Point[1]));
                    points = null;
                    view.startTimer();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (inputKey && points != null) {
                    inputKey = false;
                    core.addPoints(points.toArray(new Point[1]));
                    points = null;
                    view.startTimer();
                }
            }
        });
    }

    public void updateMap() {
        if (map != null && image != null) {
            for (int i = 0; i < xSize; i++) {
                for (int j = 0; j < ySize; j++) {
                    if (map.getCell(i, j)) {
                        graphics.setColor(Color.BLUE);
                    } else {
                        graphics.setColor(Color.WHITE);
                    }
                    graphics.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
                }
            }
            repaint();
        }
    }

    private void addPointToMap(Point point) {
        if (map != null && image != null) {
            graphics.setColor(Color.RED);
            graphics.fillRect(point.x * cellSize, point.y * cellSize, cellSize, cellSize);
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
