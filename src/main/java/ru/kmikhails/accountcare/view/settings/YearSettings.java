package ru.kmikhails.accountcare.view.settings;

import ru.kmikhails.accountcare.util.StringUtils;
import ru.kmikhails.accountcare.view.util.HorizontalPanel;
import ru.kmikhails.accountcare.view.util.VerticalPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

public class YearSettings extends JFrame {
    private static final int FRAME_WIDTH = 400;

    private final ResourceBundle resource;
    private JList<String> jList;
    private DefaultListModel<String> listModel;
    private JTextField defaultYearText;

    public YearSettings(ResourceBundle resource) {
        this.resource = resource;
    }

    public void init() {
        String[] years = resource.getString("years.range").split(",");
        String defaultYear = resource.getString("years.default");

        listModel = new DefaultListModel<>();
        for (String year : years) {
            listModel.addElement(year);
        }

        VerticalPanel mainPanel = new VerticalPanel(10, 0.1f);
        HorizontalPanel yearRangePanel = new HorizontalPanel();
//        yearRangePanel.setBorder(new TitledBorder("Ностройка диапазона"));
//        yearRangePanel.setPreferredSize(new Dimension(FRAME_WIDTH, 200));

        VerticalPanel newYearPanel = new VerticalPanel(10, 0.1f);
        JLabel newYearLabel = new JLabel("Введите новый год");
        JTextField newYearText = new JTextField(10);
        JButton newYearButton = new JButton("Добавить");
        newYearButton.addActionListener(e -> addNewYear(newYearText.getText()));

        JLabel deleteYearLabel = new JLabel("Удаление года");
        JButton deleteYearButton = new JButton("Удалить");
        deleteYearButton.addActionListener(e -> deleteYear(jList.getSelectedValue()));

        JLabel defaultYearLabel = new JLabel("Значение года по умолчанию");
        defaultYearText = new JTextField(20);
        defaultYearText.setText(defaultYear);
        defaultYearText.setBackground(Color.WHITE);
        defaultYearText.setEditable(false);
        JButton defaultYearButton = new JButton("Установить");
        defaultYearButton.addActionListener(e -> setDefaultYear());

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> quit(defaultYearText.getText()));


        newYearPanel.add(newYearLabel);
        newYearPanel.add(newYearText);
        newYearPanel.add(newYearButton);
        newYearPanel.add(deleteYearLabel);
        newYearPanel.add(deleteYearButton);
        newYearPanel.add(defaultYearLabel);
        newYearPanel.add(defaultYearText);
        newYearPanel.add(defaultYearButton);

        newYearPanel.add(okButton);
        yearRangePanel.add(newYearPanel);

        jList = new JList<>(listModel);
        jList.setBorder(new EmptyBorder(5, 5, 5, 5));
        jList.setFont(new Font(null, Font.PLAIN, 16));
        jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jList.setLayoutOrientation(JList.VERTICAL);
        jList.setBackground(new Color(238, 238, 238));

        JScrollPane listScroller = new JScrollPane(jList);
        listScroller.setBorder(new TitledBorder("Список годов"));
        listScroller.setPreferredSize(new Dimension(FRAME_WIDTH / 2, 400));

        yearRangePanel.add(listScroller);

//        HorizontalPanel yearDefaultPanel = new HorizontalPanel();
//        yearDefaultPanel.setBorder(new TitledBorder("Установка значения года по умолчанию"));
//        yearDefaultPanel.setPreferredSize(new Dimension(FRAME_WIDTH, 200));


        mainPanel.add(yearRangePanel);
//        mainPanel.add(yearDefaultPanel);

        add(mainPanel);
        setTitle("Настройки года");
        setSize(new Dimension(FRAME_WIDTH, 440));
        setLocationRelativeTo(null);
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        setResizable(false);
        setVisible(true);
    }

    private void quit(String defaultYear) {
        if (!listModel.contains(defaultYear)) {
            JOptionPane.showMessageDialog(this, "Значение года по умолчанию нет в списке",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        } else {
            setProperty();
            this.dispose();
        }
    }

    private void setDefaultYear() {
        String value = jList.getSelectedValue();
        if (!StringUtils.isEmpty(value)) {
            defaultYearText.setText(value);
        }
    }

    private void deleteYear(String value) {
        listModel.removeElement(value);
    }

    private void addNewYear(String value) {
        try {
            int year = Integer.parseInt(value);
            if (year < 2021 || year > 2099) {
                JOptionPane.showMessageDialog(this, "Введите адекватный год",
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            } else if (listModel.contains(value)) {
                JOptionPane.showMessageDialog(this, "Такое значение уже присутствует",
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            } else {
                listModel.addElement(value);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Введите правильный год",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setProperty() {
        Path path = Paths.get("src/main/resources/application.properties");
        try {
            String yearRange = createYearRange();
            String defaultYear = defaultYearText.getText();
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            lines.set(6, "years.range=" + yearRange);
            lines.set(7, "years.default=" + defaultYear);
            Files.write(path, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Ошибка изменения свойств",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String createYearRange() {
        StringBuilder yearRange = new StringBuilder();
        yearRange.append(listModel.get(0));
        for (int i = 1; i < listModel.getSize(); i++) {
            yearRange.append(",").append(listModel.get(i));
        }
        return yearRange.toString();
    }
}
