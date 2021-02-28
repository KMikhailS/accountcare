package ru.kmikhails.accountcare.view;

import ru.kmikhails.accountcare.service.AccountService;
import ru.kmikhails.accountcare.view.tablemodel.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

public class MainFrame extends JFrame implements ActionListener {
    private static final String ADD_ROW = "Добавить счёт";
    private static final String DELETE_ROW = "Удалить строку";
    private static final String DELETE_ALL_ROWS = "Удалить все строки";
    private static final String MENU = "Меню";
    private static final String[] TABLE_TYPES = new String[]{"ЧЦСМ", "УНИИМ", "другие", "прочие услуги"};
    private static final String[] YEARS = new String[]{"2021"};

    private final AccountService accountService;

    private JPanel mainPanel;
    JPanel buttonPanel;
    private CSMTableModel csmTableModel;
    private UNIIMTableModel uniimTableModel;
    private OtherTableModel otherTableModel;
    private ServiceTableModel serviceTableModel;
    private JPopupMenu popupMenu;
    private Font font;
    private CommonTableModel commonTableModel;
    private JTable table;
    private JScrollPane mainScrollPane;

    public MainFrame(AccountService accountService) {
        this.accountService = accountService;
    }

    private void init() {
//        mainPanel = new JPanel();
        csmTableModel = new CSMTableModel(accountService);
        uniimTableModel = new UNIIMTableModel(accountService);
        otherTableModel = new OtherTableModel(accountService);
        serviceTableModel = new ServiceTableModel(accountService);
        commonTableModel = csmTableModel;

        int fontSize = Integer.parseInt(System.getProperty("font.size"));

        table = new JTable(commonTableModel);
        font = new Font(null, Font.PLAIN, fontSize);
        table.setFont(font);
        table.getTableHeader().setFont(font);
        mainScrollPane = new JScrollPane(table);

        popupMenu = new JPopupMenu();
        JMenuItem menuItemAdd = new JMenuItem(ADD_ROW);
        JMenuItem menuItemRemove = new JMenuItem(DELETE_ROW);
        JMenuItem menuItemRemoveAll = new JMenuItem(DELETE_ALL_ROWS);
        menuItemAdd.addActionListener(this);
        menuItemRemove.addActionListener(this);
        menuItemRemoveAll.addActionListener(this);
        popupMenu.add(menuItemAdd);
        popupMenu.add(menuItemRemove);
        popupMenu.add(menuItemRemoveAll);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu mainMenu = new JMenu(MENU);
        menuBar.add(mainMenu);

        JMenuItem addAccountMenuItem = new JMenuItem(ADD_ROW);
        mainMenu.add(addAccountMenuItem);
        addAccountMenuItem.addActionListener(e -> addNewRow());

        table.setComponentPopupMenu(popupMenu);
        table.addMouseListener(new TableMouseListener(table));

        this.setLayout(new BorderLayout());
        buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(0, 70));
        buttonPanel.setLayout(null);

        JLabel yearLabel = new JLabel("Год");
        yearLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        yearLabel.setBounds(43, 11, 46, 14);
        buttonPanel.add(yearLabel);

        JComboBox<String> yearComboBox = new JComboBox<>();
        yearComboBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
        yearComboBox.setModel(new DefaultComboBoxModel<>(YEARS));
        yearComboBox.setBounds(43, 36, 105, 22);
        buttonPanel.add(yearComboBox);

        JLabel serviceLabel = new JLabel("Услуга");
        serviceLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        serviceLabel.setBounds(181, 11, 46, 14);
        buttonPanel.add(serviceLabel);

//        JLabel testLabel = new JLabel(System.getProperty("font.size"));
//        testLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
//        testLabel.setBounds(400, 20, 100, 14);
//        buttonPanel.add(testLabel);

//        JButton testButton = new JButton("change propertie");
//        testButton.setBounds(600, 20, 100, 50);
//        testButton.addActionListener(e -> {
//            changeProperty();
//        });
//        buttonPanel.add(testButton);

        JComboBox<String> serviceComboBox = new JComboBox<>();
        serviceComboBox.setModel(new DefaultComboBoxModel<>(TABLE_TYPES));
        serviceComboBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
        serviceComboBox.setBounds(181, 36, 125, 22);
        serviceComboBox.addActionListener(e -> {
            String serviceType = (String) serviceComboBox.getSelectedItem();
            if (serviceType != null) {
                switch (serviceType) {
                    case "ЧЦСМ":
                        resetTableModel(csmTableModel);
                        break;
                    case "УНИИМ":
                        resetTableModel(uniimTableModel);
                        break;
                    case "другие":
                        resetTableModel(otherTableModel);
                        break;
                    case "прочие услуги":
                        resetTableModel(serviceTableModel);
                        break;
                    default:
                        throw new IllegalArgumentException("Нет такой модели таблицы");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Не выбран тип таблицы",
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(serviceComboBox);

        this.add(buttonPanel, BorderLayout.NORTH);
        this.add(mainScrollPane, BorderLayout.CENTER);
        this.setTitle("Test Table");
        this.setSize(1500, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JMenuItem menuItem = (JMenuItem) event.getSource();
        switch (menuItem.getText()) {
            case ADD_ROW:
                addNewRow();
                break;
            case DELETE_ROW:
                deleteRow();
                break;
            default:
                JOptionPane.showMessageDialog(this, String.format("Ошибка при выборе меню [%s]", menuItem.getText()),
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetTableModel(CommonTableModel tableModel) {
        this.remove(mainScrollPane);
        this.commonTableModel = tableModel;
        table = new JTable(commonTableModel);
        table.setComponentPopupMenu(popupMenu);
        table.setFont(font);
        table.getTableHeader().setFont(font);
        table.addMouseListener(new TableMouseListener(table));
        mainScrollPane = new JScrollPane(table);
        this.add(mainScrollPane, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }

    public void run() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.invokeLater(() -> new MainFrame(accountService).init());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Критическая ошибка отображения формы\nОбратитесь в поддержку",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void addNewRow() {
        new AccountForm(commonTableModel);
    }

    private void deleteRow() {
        int rowNumber = table.getSelectedRow();
        String accountNumber = (String) table.getValueAt(rowNumber, 0);
        LocalDate date = (LocalDate) table.getValueAt(rowNumber, 1);
        commonTableModel.deleteRow(accountNumber, date);
        this.revalidate();
        this.repaint();
    }

    private void changeProperty() {
        Path path = Paths.get("src/main/resources/config.properties");
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            lines.set(1, "font.size=22");
            Files.write(path, lines, StandardCharsets.UTF_8);
//            buttonPanel.revalidate();
//            buttonPanel.repaint();
//            this.dispose();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Ошибка изменения свойств",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}
