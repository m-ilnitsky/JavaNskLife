package ru.mikensk.mathlife.swing.view;

import ru.mikensk.mathlife.controller.ViewListener;
import ru.mikensk.mathlife.core.GameMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.Instant;

import static com.sun.java.accessibility.util.AWTEventMonitor.addKeyListener;

/**
 * Графический интерфейс (представление) на основе Swing для игры "Жизнь"
 */
public class FrameView implements ViewAutoCloseable {
    private ViewListener core;
    private GameMap gameMap;

    private int cellSize;
    private int height;
    private int width;
    private int xSize;
    private int ySize;

    private final Timer timer = new Timer(0, l -> doStep());
    private boolean timerOn;

    private long lastTime;
    private int lastStep;

    private final static String stepStr = "Шаг: ";
    private final static String numStr = "Количество живых ячеек: ";
    private final static String rateStr = "Шагов/сек: ";

    private final JFrame frame = new JFrame("Жизнь");
    private final JPanel topPanel = new JPanel();
    private final JPanel bottomPanel = new JPanel();

    private final JPopupMenu popUpMenu = new JPopupMenu();

    private final JPanel infoPanel = new JPanel();
    private final JLabel stepLabel = new JLabel(stepStr);
    private final JLabel numLabel = new JLabel(numStr);
    private final JLabel rateLabel = new JLabel(rateStr);

    private final JPanel radioButtonPanel = new JPanel();
    private final JRadioButton radioButtonOff = new JRadioButton("Стоп");
    private final String[] radioButtonText = {"1сек", "0.5сек", "0.2сек", "0.1сек", "0.05сек", "0.02сек", "0.01сек"};
    private final int[] radioButtonDelay = {1000, 500, 200, 100, 50, 20, 10};
    private final JRadioButton[] radioButtons = new JRadioButton[radioButtonText.length];
    private final ButtonGroup delayButtonGroup = new ButtonGroup();

    private final JPanel buttonPanel = new JPanel();
    private final JToggleButton buttonPeriodicity = new JToggleButton("Периодичность");
    private final String[] buttonText = {"+1шаг", "+10шагов", "+100шагов", "+1000шагов"};
    private final int[] buttonStep = {1, 10, 100, 1000};
    private final JButton[] buttons = new JButton[buttonText.length];

    private final JButton buttonClear = new JButton("Очистить");
    private final JButton buttonRandom = new JButton("Наполнить");
    private final JToggleButton buttonPencil = new JToggleButton("Карандаш");
    private final JToggleButton buttonLine = new JToggleButton("Линия");
    private final JToggleButton buttonRect = new JToggleButton("Прямоугольник");
    private final JToggleButton buttonOval = new JToggleButton("Эллипс");
    private final ButtonGroup toolButtonGroup = new ButtonGroup();

    private final MapPanel mapPanel;

    public FrameView(int width, int height, int cellSize) {
        this.width = width;
        this.height = height;
        this.cellSize = cellSize;
        mapPanel = new MapPanel(cellSize, this);

        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                mapPanel.EscPressed(e);
            }
        });
    }

    private void initRadioButtons() {
        delayButtonGroup.add(radioButtonOff);

        for (int i = 0; i < radioButtons.length; i++) {
            radioButtons[i] = new JRadioButton(radioButtonText[i]);
            delayButtonGroup.add(radioButtons[i]);
        }

        radioButtonOff.addActionListener(e -> {
            timer.setDelay(0);
            timerOn = false;
            timer.stop();
            rateLabel.setVisible(false);
        });

        radioButtonOff.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                mapPanel.EscPressed(e);
            }
        });

        for (int i = 0; i < radioButtons.length; i++) {
            Integer index = i;
            radioButtons[i].addActionListener(e -> {
                timer.setDelay(radioButtonDelay[index]);
                timerOn = true;
                timer.start();
                rateLabel.setVisible(true);
            });

            radioButtons[i].addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    super.keyPressed(e);
                    mapPanel.EscPressed(e);
                }
            });
        }

        radioButtonOff.doClick();
    }

    private void initButtons() {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton(buttonText[i]);

            Integer index = i;
            buttons[i].addActionListener(e ->
                    core.doSteps(buttonStep[index]));

            buttons[i].addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    super.keyPressed(e);
                    mapPanel.EscPressed(e);
                }
            });
        }

        buttonPeriodicity.addActionListener(e -> {
            if (core.isPeriodic()) {
                core.setPeriodic(false);
                buttonPeriodicity.setSelected(false);
            } else {
                core.setPeriodic(true);
                buttonPeriodicity.setSelected(true);
            }
        });

        buttonPeriodicity.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                mapPanel.EscPressed(e);
            }
        });

        buttonClear.addActionListener(e -> core.clear());

        buttonClear.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                mapPanel.EscPressed(e);
            }
        });

        buttonRandom.addActionListener(e -> core.init());

        buttonRandom.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                mapPanel.EscPressed(e);
            }
        });

        buttonPencil.addActionListener(e -> mapPanel.setPencil());

        buttonPencil.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                mapPanel.EscPressed(e);
            }
        });

        buttonLine.addActionListener(e -> mapPanel.setFigure(Figure.LINE));

        buttonLine.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                mapPanel.EscPressed(e);
            }
        });

        buttonRect.addActionListener(e -> mapPanel.setFigure(Figure.RECT));

        buttonRect.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                mapPanel.EscPressed(e);
            }
        });

        buttonOval.addActionListener(e -> mapPanel.setFigure(Figure.OVAL));

        buttonOval.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                mapPanel.EscPressed(e);
            }
        });

        toolButtonGroup.add(buttonPencil);
        toolButtonGroup.add(buttonLine);
        toolButtonGroup.add(buttonRect);
        toolButtonGroup.add(buttonOval);

        buttonPeriodicity.doClick();
        buttonPencil.doClick();
    }

    private void initInfoPanel() {
        infoPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 16, 8));

        Font groupFont = new Font("TimesRoman", Font.BOLD, 14);

        stepLabel.setFont(groupFont);
        numLabel.setFont(groupFont);
        rateLabel.setFont(groupFont);

        infoPanel.add(stepLabel);
        infoPanel.add(numLabel);
        infoPanel.add(rateLabel);

        infoPanel.setVisible(true);
    }

    private void initRadioButtonPanel() {
        radioButtonPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 0));

        radioButtonPanel.add(radioButtonOff);

        for (JRadioButton b : radioButtons) {
            radioButtonPanel.add(b);
        }

        radioButtonPanel.setVisible(true);
    }

    private void initButtonPanel() {
        buttonPanel.setLayout(new GridLayout(1, 1 + buttons.length));

        buttonPanel.add(buttonPeriodicity);

        for (JButton b : buttons) {
            buttonPanel.add(b);
        }

        buttonPanel.setVisible(true);
    }

    private void initTopPanel() {
        topPanel.setLayout(new GridLayout(3, 1));

        topPanel.add(infoPanel);
        topPanel.add(radioButtonPanel);
        topPanel.add(buttonPanel);

        topPanel.setVisible(true);
    }

    private void initBottomPanel() {
        bottomPanel.setLayout(new GridLayout(1, 6));

        bottomPanel.add(buttonClear);
        bottomPanel.add(buttonRandom);
        bottomPanel.add(buttonPencil);
        bottomPanel.add(buttonLine);
        bottomPanel.add(buttonRect);
        bottomPanel.add(buttonOval);

        Font groupFont = new Font("TimesRoman", Font.BOLD, 14);

        buttonClear.setFont(groupFont);
        buttonRandom.setFont(groupFont);
        buttonPencil.setFont(groupFont);
        buttonLine.setFont(groupFont);
        buttonRect.setFont(groupFont);
        buttonOval.setFont(groupFont);

        bottomPanel.setVisible(true);
    }

    private void initMapPanel() {
        mapPanel.setVisible(true);
    }

    private void initPopUpMenu() {
        JMenuItem itemAbout = new JMenuItem("О программе");
        popUpMenu.add(itemAbout);

        itemAbout.addActionListener(l ->
                JOptionPane.showMessageDialog(frame,
                        new String[]{"Математическая игра Жизнь (Game of Life)",
                                "Графический интерфейс на основе Swing",
                                "М.Ильницкий, Новосибирск, 2017"},
                        "О программе",
                        JOptionPane.INFORMATION_MESSAGE)
        );

        topPanel.setComponentPopupMenu(popUpMenu);
        infoPanel.setComponentPopupMenu(popUpMenu);
        buttonPanel.setComponentPopupMenu(popUpMenu);
        bottomPanel.setComponentPopupMenu(popUpMenu);
    }

    private void initFrame() {
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(mapPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Dimension dimension = new Dimension(width, height);

        frame.setMinimumSize(dimension);
        frame.setMaximumSize(dimension);

        frame.setLocationRelativeTo(null);

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
            initRadioButtons();
            initButtons();

            initRadioButtonPanel();
            initButtonPanel();
            initInfoPanel();
            initTopPanel();
            initMapPanel();
            initBottomPanel();
            initFrame();

            initPopUpMenu();

            setMapSize();
            core.init();
            mapPanel.init(xSize, ySize, gameMap, core);

            lastStep = 0;
            lastTime = Instant.now().getEpochSecond();
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
        if (timerOn) {
            timer.start();
        }
    }

    @Override
    public void stopTimer() {
        timer.stop();
    }

    @Override
    public void update() {
        int step = core.getStep();
        stepLabel.setText(stepStr + step);
        numLabel.setText(numStr + core.getNumAliveCells());

        mapPanel.updateMap();

        long time = Instant.now().getEpochSecond();
        if (step != lastStep && time != lastTime) {
            rateLabel.setText(rateStr + ((step - lastStep) / (time - lastTime)));
            lastTime = time;
            lastStep = step;
        }
    }

    private void doStep() {
        if (core.getNumAliveCells() > 0) {
            core.doSteps(1);
        }
    }
}
