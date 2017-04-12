package ru.mikensk.mathlife.swing.view;

import ru.mikensk.mathlife.controller.View;
import ru.mikensk.mathlife.controller.ViewListener;
import ru.mikensk.mathlife.core.GameMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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

    private ArrayList<Point> points;
    private Point startPoint;
    private Point endPoint;

    private boolean inputFlag = false;
    private boolean figureFlag = true;
    private boolean clearFlag = false;
    private Figure figure = Figure.LINE;

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

        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

                EscPressed(e);
            }
        });

        addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (inputFlag) {
                    if (figureFlag && !clearFlag) {
                        endPoint = new Point((int) (1.0 * e.getPoint().x / getWidth() * xSize * cellSize) / cellSize * cellSize,
                                (int) (1.0 * e.getPoint().y / getHeight() * ySize * cellSize) / cellSize * cellSize);
                        addFigureToMap(startPoint, endPoint);
                    } else {
                        int x = (int) (1.0 * e.getPoint().x / getWidth() * xSize);
                        int y = (int) (1.0 * e.getPoint().y / getHeight() * ySize);

                        if (x >= 0 && x < xSize && y >= 0 && y < ySize) {
                            Point point = new Point(x, y);
                            points.add(point);
                            addPointToMap(point);
                        }
                    }
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

                if (figureFlag && !clearFlag) {
                    startPoint = new Point((int) (1.0 * e.getPoint().x / getWidth() * xSize * cellSize) / cellSize * cellSize,
                            (int) (1.0 * e.getPoint().y / getHeight() * ySize * cellSize) / cellSize * cellSize);
                    endPoint = startPoint;
                    addFigureToMap(startPoint, endPoint);
                } else {
                    points = new ArrayList<>(64);

                    int x = (int) (1.0 * e.getPoint().x / getWidth() * xSize);
                    int y = (int) (1.0 * e.getPoint().y / getHeight() * ySize);

                    if (x >= 0 && x < xSize && y >= 0 && y < ySize) {
                        Point point = new Point(x, y);
                        points.add(point);
                        addPointToMap(point);
                    }
                }

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                sendPoints();
            }
        });
    }

    public void setFigure(Figure figure) {
        figureFlag = true;
        this.figure = figure;
    }

    public void setPencil() {
        figureFlag = false;
    }

    public void EscPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE && inputFlag) {
            inputFlag = false;
            points = null;
            view.startTimer();
            updateMap();
        }
    }

    private void sendPoints() {
        if (inputFlag) {
            if (!clearFlag && figureFlag) {
                getPointsOfFigure();
            }

            if (points != null) {
                if (clearFlag) {
                    core.clearPoints(points.toArray(new Point[1]));
                } else {
                    core.addPoints(points.toArray(new Point[1]));
                }
                points = null;
            }

            inputFlag = false;
            view.startTimer();
        }
    }

    private void getPointsOfFigure() {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage figureImage = new BufferedImage(width, height, TYPE_3BYTE_BGR);
        Graphics2D figureGraphics = (Graphics2D) figureImage.getGraphics();

        figureGraphics.setColor(Color.WHITE);
        figureGraphics.fillRect(0, 0, width, height);

        int minX = Math.min(startPoint.x, endPoint.x);
        int maxX = Math.max(startPoint.x, endPoint.x);
        int minY = Math.min(startPoint.y, endPoint.y);
        int maxY = Math.max(startPoint.y, endPoint.y);

        figureGraphics.setColor(Color.BLACK);
        switch (figure) {
            case LINE:
                figureGraphics.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
                break;
            case RECT:
                figureGraphics.drawRect(minX, minY, maxX - minX, maxY - minY);
                //for (int i = 0; i < cellSize; i++) {
                //  figureGraphics.drawRect(startPoint.x + i, startPoint.y + i, endPoint.x - startPoint.x + cellSize - 1 - 2 * i, endPoint.y - startPoint.y + cellSize - 1 - 2 * i);
                //}
                break;
            case OVAL:
                figureGraphics.drawOval(minX, minY, maxX - minX, maxY - minY);
                //for (int i = 0; i < cellSize; i++) {
                //  figureGraphics.drawOval(startPoint.x + i, startPoint.y + i, endPoint.x - startPoint.x + -2 * i, endPoint.y - startPoint.y + -2 * i);
                //}
        }

        points = new ArrayList<>(64);

        int count = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int packedInt = figureImage.getRGB(i, j);
                Color color = new Color(packedInt, true);

                if (color.equals(Color.BLACK)) {
                    int x;
                    int y;
                    if (figure == Figure.RECT) {
                        x = i / cellSize;
                        y = j / cellSize;
                    } else {
                        x = (int) Math.round(1.0 * i / cellSize);
                        y = (int) Math.round(1.0 * j / cellSize);
                    }
                    if (x >= 0 && x < xSize && y >= 0 && y < ySize) {
                        points.add(new Point(x, y));
                        count++;
                    }
                }
            }
        }

        if (count == 0) {
            points = null;
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

    private void addFigureToMap(Point startPoint, Point endPoint) {
        if (map != null && image != null) {
            int minX = Math.min(startPoint.x, endPoint.x);
            int maxX = Math.max(startPoint.x, endPoint.x);
            int minY = Math.min(startPoint.y, endPoint.y);
            int maxY = Math.max(startPoint.y, endPoint.y);

            updateMap();
            switch (figure) {
                case LINE:
                    graphics.setColor(Color.RED);
                    for (int i = 0; i < cellSize; i++) {
                        graphics.drawLine(startPoint.x, startPoint.y + i, endPoint.x + i, endPoint.y);
                        graphics.drawLine(startPoint.x + cellSize - 1, startPoint.y + i, endPoint.x + i, endPoint.y + cellSize - 1);

                        graphics.drawLine(startPoint.x + i, startPoint.y, endPoint.x, endPoint.y + i);
                        graphics.drawLine(startPoint.x + i, startPoint.y + cellSize - 1, endPoint.x + cellSize - 1, endPoint.y + i);

                        graphics.drawLine(startPoint.x + i, startPoint.y, endPoint.x + i, endPoint.y);
                        graphics.drawLine(startPoint.x, startPoint.y + i, endPoint.x, endPoint.y + i);
                    }
                    break;

                case RECT:
                    graphics.setColor(Color.RED);
                    for (int i = 0; i < cellSize; i++) {
                        graphics.drawRect(minX + i, minY + i, maxX - minX + cellSize - 1 - 2 * i, maxY - minY + cellSize - 1 - 2 * i);
                    }
                    break;

                case OVAL:
                    graphics.setColor(Color.GREEN);
                    graphics.drawRect(minX, minY, maxX - minX + cellSize - 1, maxY - minY + cellSize - 1);
                    graphics.setColor(Color.RED);
                    for (int i = 0; i < cellSize; i++) {
                        graphics.drawOval(minX + i, minY + i, maxX - minX + cellSize - 1 - 2 * i, maxY - minY + cellSize - 1 - 2 * i);
                        graphics.drawOval(minX + i, minY, maxX - minX + cellSize - 1 - 2 * i, maxY - minY + cellSize - 1);
                        graphics.drawOval(minX, minY + i, maxX - minX + cellSize - 1, maxY - minY + cellSize - 1 - 2 * i);
                    }
            }
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
