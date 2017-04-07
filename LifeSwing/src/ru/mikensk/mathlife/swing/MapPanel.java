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

    private ViewListener core;
    private GameMap map;
    private View view;

    private BufferedImage image;
    private Graphics2D graphics;
    private boolean inputFlag;
    private ArrayList<Point> points;
    private boolean clearFlag = false;

    public MapPanel(int cellSize, View view) {
        super();
        this.cellSize = cellSize;
        this.view = view;
        inputFlag = false;
    }

    public void init(int xSize, int ySize, GameMap map, ViewListener core) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.map = map;
        this.core = core;

        image = new BufferedImage(xSize * cellSize, ySize * cellSize, TYPE_INT_RGB);
        graphics = (Graphics2D) image.getGraphics();
        updateMap();

        addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (inputFlag && points != null) {
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
                if (e.getButton() == MouseEvent.BUTTON1) {
                    clearFlag = false;
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    clearFlag = true;
                } else {
                    return;
                }

                view.stopTimer();
                inputFlag = true;
                points = new ArrayList<>(64);

                Point panelPoint = e.getPoint();
                Point point = new Point((int) (1.0 * panelPoint.x / getWidth() * xSize), (int) (1.0 * panelPoint.y / getHeight() * ySize));
                points.add(point);
                addPointToMap(point);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                sendPoints();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                sendPoints();
            }
        });
    }

    private void sendPoints() {
        if (inputFlag && points != null) {
            inputFlag = false;
            if (clearFlag) {
                core.clearPoints(points.toArray(new Point[1]));
            } else {
                core.addPoints(points.toArray(new Point[1]));
            }
            points = null;
            view.startTimer();
        }
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
            if (clearFlag) {
                graphics.setColor(Color.WHITE);
            } else {
                graphics.setColor(Color.RED);
            }
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
