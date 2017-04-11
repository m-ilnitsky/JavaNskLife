package ru.mikensk.mathlife.swing.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Диалог выбора размеров окна и ячейки для игры "Жизнь"
 */
public class SizeDialog {
    private JFrame frame;

    private int width;
    private int height;
    private int cellSize;

    private int minWidth = 200;
    private int minHeight = 200;
    private int minCellSize = 1;

    private GameSize gameSize;

    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public SizeDialog(JFrame frame) {
        this.frame = frame;
    }

    public void initSize(int width, int height, int cellSize){
        this.width = width;
        this.height = height;
        this.cellSize = cellSize;

        gameSize = new GameSize(width, height, cellSize);
    }

    public GameSize showDialog() {
        final JDialog dialog = new JDialog(frame);

        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setTitle("Выбор произвольных параметров игры");
        dialog.setModal(true);
        dialog.setSize(400, 160);
        dialog.setMinimumSize(dialog.getSize());

        JPanel panel = new JPanel();

        JLabel labelWidth = new JLabel("Введите ширину окна [пиксел]:");
        JLabel labelHeight = new JLabel("Введите высоту окна [пиксел]:");
        JLabel labelCellSize = new JLabel("Введите размер ячейки [пиксел]:");

        JTextField inputWidth = new JTextField(Integer.toString(width));
        JTextField inputHeight = new JTextField(Integer.toString(height));
        JTextField inputCellSize = new JTextField(Integer.toString(cellSize));

        JButton buttonCancel = new JButton("Отмена   (Alt+C)");
        JButton buttonOk = new JButton("Применить   (Alt+Enter)");

        buttonCancel.setMnemonic(KeyEvent.VK_C);
        buttonOk.setMnemonic(KeyEvent.VK_ENTER);

        buttonCancel.addActionListener(actionEvent -> {
            dialog.setVisible(false);
            System.exit(0);
        });

        buttonOk.addActionListener(actionEvent -> {
            try {
                width = Integer.parseInt(inputWidth.getText());
                height = Integer.parseInt(inputHeight.getText());
                cellSize = Integer.parseInt(inputCellSize.getText());

                if (width < minWidth) {
                    showErrorMessage(dialog, "Ширина должна быть не меньше " + minWidth);
                } else if (height < minHeight) {
                    showErrorMessage(dialog, "Высота должна быть не меньше " + minHeight);
                } else if (cellSize < minCellSize) {
                    showErrorMessage(dialog, "Размер ячейки должна быть не меньше " + minCellSize);
                } else if (width > screenSize.getWidth()) {
                    showErrorMessage(dialog, "Ширина должна быть не больше ширины экрана (" + screenSize.getWidth() + ")");
                } else if (height > screenSize.getHeight()) {
                    showErrorMessage(dialog, "Высота должна быть не больше высоты экрана (" + screenSize.getHeight() + ")");
                } else if (cellSize > Math.min(width, height) / 4) {
                    showErrorMessage(dialog, "Размер ячейки должен быть не менее чем в 4 раза меньше размера окна");
                } else {
                    this.gameSize = new GameSize(width, height, cellSize);

                    dialog.setVisible(false);
                }
            } catch (NumberFormatException ex) {
                showErrorMessage(dialog, "Вводить нужно целые положительные числа");
            }
        });

        panel.setLayout(new GridLayout(4, 2));
        panel.add(labelWidth);
        panel.add(inputWidth);
        panel.add(labelHeight);
        panel.add(inputHeight);
        panel.add(labelCellSize);
        panel.add(inputCellSize);
        panel.add(buttonCancel);
        panel.add(buttonOk);

        dialog.add(panel);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        return gameSize;
    }

    private void showErrorMessage(Window fromFrame, String message) {
        JOptionPane.showMessageDialog(fromFrame,
                message,
                "Ошибка",
                JOptionPane.INFORMATION_MESSAGE);
    }
}

