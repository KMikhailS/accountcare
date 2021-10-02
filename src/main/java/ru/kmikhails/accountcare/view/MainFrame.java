package ru.kmikhails.accountcare.view;

import org.apache.commons.io.IOUtils;
import ru.kmikhails.accountcare.entity.Account;
import ru.kmikhails.accountcare.entity.Company;
import ru.kmikhails.accountcare.entity.InspectionOrganization;
import ru.kmikhails.accountcare.entity.TableType;
import ru.kmikhails.accountcare.exception.AccountException;
import ru.kmikhails.accountcare.service.AccountService;
import ru.kmikhails.accountcare.service.impl.CompanyService;
import ru.kmikhails.accountcare.service.impl.InspectionOrganizationService;
import ru.kmikhails.accountcare.service.impl.TableTypeService;
import ru.kmikhails.accountcare.util.ExcelExporter;
import ru.kmikhails.accountcare.util.PdfRunner;
import ru.kmikhails.accountcare.util.StringUtils;
import ru.kmikhails.accountcare.view.renderer.ChangeRowColorRenderer;
import ru.kmikhails.accountcare.view.tablemodel.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainFrame extends JFrame implements ActionListener {
    private static final String ADD_ROW = "Добавить счёт";
    private static final String DELETE_ROW = "Удалить счёт";
    private static final String UPDATE_ROW = "Обновить счёт";
    private static final String SHOW_SCAN = "Показать скан";
    private static final String UPDATE_TABLE = "Обновить таблицу";
    private static final String HIGHLIGHT_OUR = "Выделить \"наш\"";
    private static final String UNSET_HIGHLIGHT_OUR = "Снять выделение \"наш\"";
    private static final String EXPORT = "Экспорт";
    private static final String EXPORT_EXCEL_CSM = "Экспорт счетов без фактур ЧЦСМ";
    private static final String MENU = "Меню";
    private static final String[] YEARS = new String[]{"2021"};

    private final AccountService accountService;
    private final CompanyService companyService;
    private final InspectionOrganizationService inspectionOrganizationService;
    private final TableTypeService tableTypeService;
    private final ResourceBundle resource;

    private CSMTableModel csmTableModel;
    private UNIIMTableModel uniimTableModel;
    private OtherTableModel otherTableModel;
    private ServiceTableModel serviceTableModel;
    private JTextField searchTextField;
    private JPopupMenu popupMenu;
    private Font font;
    private CommonTableModel commonTableModel;
    private JTable table;
    private JPanel buttonPanel;
    private JScrollPane mainScrollPane;
    private JTableHeader header;
    private Company[] companies;
    private InspectionOrganization[] organizations;
    private TableType[] tableTypes;
    private AccountForm accountForm;
    private List<Account> accounts;

    public MainFrame(ResourceBundle resource, AccountService accountService, CompanyService companyService,
                     TableTypeService tableTypeService, InspectionOrganizationService inspectionOrganizationService) {
        this.resource = resource;
        this.accountService = accountService;
        this.companyService = companyService;
        this.inspectionOrganizationService = inspectionOrganizationService;
        this.tableTypeService = tableTypeService;
    }

    private void init() {
        configureTableModel();
        companies = companyService.findAll().toArray(new Company[0]);
        organizations = inspectionOrganizationService.findAll().toArray(new InspectionOrganization[0]);
        tableTypes = tableTypeService.findAll().toArray(new TableType[0]);
        accountForm = new AccountForm(accountService, commonTableModel, companies, tableTypes, organizations);

        int fontSize = Integer.parseInt(resource.getString("font.size"));

        accounts = accountService.findAllByTableType("ЧЦСМ");

        table = new JTable(commonTableModel);
        table.setDefaultRenderer(Object.class, new ChangeRowColorRenderer());
        table.setSelectionBackground(new Color(180, 180, 180));
        table.setShowVerticalLines(false);


        font = new Font(null, Font.PLAIN, fontSize);
        table.setRowHeight(fontSize + 5);
        table.setFont(font);
        table.getTableHeader().setFont(font);
        table.getTableHeader().setMaximumSize(new Dimension(50, 50));
        table.getTableHeader().setOpaque(false);
        table.getTableHeader().setBackground(new Color(40, 132, 189));
        table.getTableHeader().setForeground(new Color(255, 255, 255));
        table.getTableHeader().setPreferredSize(new Dimension(0, 50));
        sortTable();

        mainScrollPane = new JScrollPane(table);

        configurePopupMenu();

        configureMenu();

        table.setComponentPopupMenu(popupMenu);
        table.addMouseListener(new TableMouseListener(table));

        printMainInterface(fontSize);
//        this.setSize(1600, 500);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private void printMainInterface(int fontSize) {
        this.setLayout(new BorderLayout());
        buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(0, 70));
        buttonPanel.setLayout(null);

        JLabel yearLabel = new JLabel("Год");
        yearLabel.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
        yearLabel.setBounds(43, 11, 46, fontSize + 5);
        buttonPanel.add(yearLabel);

        JComboBox<String> yearComboBox = new JComboBox<>();
        yearComboBox.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
        yearComboBox.setModel(new DefaultComboBoxModel<>(YEARS));
        yearComboBox.setBounds(43, 36, 105, fontSize + 5);
        buttonPanel.add(yearComboBox);

        JLabel serviceLabel = new JLabel("Таблица");
        serviceLabel.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
        serviceLabel.setBounds(181, 11, 75, fontSize + 5);
        buttonPanel.add(serviceLabel);

        JLabel searchLabel = new JLabel("Поиск");
        searchLabel.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
        searchLabel.setBounds(780, 36, 75, fontSize + 5);
        buttonPanel.add(searchLabel);

        JButton addNewRowButton = new JButton();
        addNewRowButton.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
        addNewRowButton.setText("Добавить счёт");
        addNewRowButton.setBounds(380, 34, 180, fontSize + 10);
        addNewRowButton.addActionListener(e -> addNewRow());
        buttonPanel.add(addNewRowButton);

        JButton searchCancelButton = new JButton();
        try {
            InputStream crossInputStream = getClass().getClassLoader().getResourceAsStream("cross.png");
            if (crossInputStream != null) {
                byte[] crossBytes = IOUtils.toByteArray(crossInputStream);
                if (crossBytes != null && crossBytes.length > 0) {
                    Icon icon = new ImageIcon(crossBytes);
                    searchCancelButton.addActionListener(e -> searchTextField.setText(""));
                    searchCancelButton.setIcon(icon);
                    searchCancelButton.setBorderPainted(false);
                    searchCancelButton.setBackground(Color.WHITE);
                    searchCancelButton.setOpaque(true);
                    searchCancelButton.setPreferredSize(new Dimension(20, 20));
                    searchCancelButton.setContentAreaFilled(false);
                    searchCancelButton.setForeground(Color.WHITE);
                    searchCancelButton.setVisible(false);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        searchTextField = new JTextField();
        searchTextField.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
        searchTextField.setBounds(850, 36, 200, fontSize + 8);
        searchTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchCancelButton.setVisible(true);
                highlight();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (searchTextField.getText().isEmpty()) {
                    searchCancelButton.setVisible(false);
                }
                highlight();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                highlight();
            }

            private void highlight() {
                for (int i = 0; i < table.getRowCount(); i++) {
                    int i1 = table.getRowSorter().convertRowIndexToView(i);
                    String valueAt = (String) table.getModel().getValueAt(i, 0);
                    if (searchTextField.getText().equalsIgnoreCase(valueAt)) {
                        table.setRowSelectionInterval(i1, i1);
                    } else {
                        table.removeRowSelectionInterval(i1, i1);
                    }
                }
            }
        });
        ComponentBorder searchComponent = new ComponentBorder(searchCancelButton);
        searchComponent.install(searchTextField);
        searchComponent.setEdge(ComponentBorder.Edge.RIGHT);
        buttonPanel.add(searchTextField);

        JComboBox<TableType> serviceComboBox = new JComboBox<>(tableTypes);
        serviceComboBox.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
        serviceComboBox.setBounds(181, 36, 160, fontSize + 5);
        serviceComboBox.addActionListener(e -> {
            String serviceType = ((TableType) serviceComboBox.getSelectedItem()).getTableType();
            if (serviceType != null) {
                switch (serviceType) {
                    case "ЧЦСМ":
                        resetTableModel(csmTableModel, fontSize);
                        break;
                    case "УНИИМ":
                        resetTableModel(uniimTableModel, fontSize);
                        break;
                    case "другие":
                        resetTableModel(otherTableModel, fontSize);
                        break;
                    case "прочие услуги":
                        resetTableModel(serviceTableModel, fontSize);
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
        this.setTitle("Счета метрологии");
        try {
            InputStream mainIconInputStream = getClass().getClassLoader().getResourceAsStream("icon.png");
            if (mainIconInputStream != null) {
                byte[] mainIconBytes = IOUtils.toByteArray(mainIconInputStream);
                if (mainIconBytes != null && mainIconBytes.length > 0) {
                    ImageIcon mainIcon = new ImageIcon(mainIconBytes);
                    this.setIconImage(mainIcon.getImage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void configureMenu() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu mainMenu = new JMenu(MENU);
        menuBar.add(mainMenu);

        JMenu exportMenu = new JMenu(EXPORT);
        menuBar.add(exportMenu);

        JMenuItem addAccountMenuItem = new JMenuItem(ADD_ROW);
        mainMenu.add(addAccountMenuItem);
        addAccountMenuItem.addActionListener(e -> addNewRow());

        JMenuItem updateTableMenuItem = new JMenuItem(UPDATE_TABLE);
        mainMenu.add(updateTableMenuItem);
        updateTableMenuItem.addActionListener(e -> updateTable());

        JMenuItem exportForCsmMenuItem = new JMenuItem(EXPORT_EXCEL_CSM);
        exportMenu.add(exportForCsmMenuItem);
        exportForCsmMenuItem.addActionListener(e -> exportToExel());
    }

    private void configurePopupMenu() {
        popupMenu = new JPopupMenu();
        JMenuItem menuItemAdd = new JMenuItem(ADD_ROW);
        JMenuItem menuItemRemove = new JMenuItem(DELETE_ROW);
        JMenuItem menuItemUpdate = new JMenuItem(UPDATE_ROW);
        JMenuItem menuItemShowScan = new JMenuItem(SHOW_SCAN);
        JMenuItem menuItemSetOur = new JMenuItem(HIGHLIGHT_OUR);
        JMenuItem menuItemUnSetOur = new JMenuItem(UNSET_HIGHLIGHT_OUR);
        menuItemAdd.addActionListener(this);
        menuItemRemove.addActionListener(this);
        menuItemUpdate.addActionListener(this);
        menuItemShowScan.addActionListener(this);
        menuItemSetOur.addActionListener(this);
        menuItemUnSetOur.addActionListener(this);
        popupMenu.add(menuItemAdd);
        popupMenu.add(menuItemUpdate);
        popupMenu.add(menuItemRemove);
        popupMenu.add(new JSeparator());
        popupMenu.add(menuItemShowScan);
        popupMenu.add(new JSeparator());
        popupMenu.add(menuItemSetOur);
        popupMenu.add(menuItemUnSetOur);
    }

    private void configureTableModel() {
        List<Account> CSMAccounts = accountService.findAllByTableType("ЧЦСМ");
        List<Account> otherAccounts = accountService.findAllByTableType("другие");
        List<Account> UNIIMAccounts = accountService.findAllByTableType("УНИИМ");
        List<Account> serviceAccounts = accountService.findAllByTableType("прочие услуги");
        csmTableModel = new CSMTableModel(accountService, CSMAccounts);
        uniimTableModel = new UNIIMTableModel(accountService, UNIIMAccounts);
        otherTableModel = new OtherTableModel(accountService, otherAccounts);
        serviceTableModel = new ServiceTableModel(accountService, serviceAccounts);
        commonTableModel = csmTableModel;
    }

    private void sortTable() {
        table.setAutoCreateRowSorter(true);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        int dateColumn = 1;
        sortKeys.add(new RowSorter.SortKey(dateColumn, SortOrder.DESCENDING));
        int accountNumberColumn = 0;
        sortKeys.add(new RowSorter.SortKey(accountNumberColumn, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
        sorter.sort();
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
            case UPDATE_ROW:
                updateRow();
                break;
            case SHOW_SCAN:
                showScan();
                break;
            case HIGHLIGHT_OUR:
                highlightOur();
                break;
            case UNSET_HIGHLIGHT_OUR:
                unHighlightOur();
                break;
            default:
                JOptionPane.showMessageDialog(this, String.format("Ошибка при выборе меню [%s]", menuItem.getText()),
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportToExel() {
        SwingUtilities.invokeLater(() -> {
            List<Account> accounts = accountService.findAllByTableType("ЧЦСМ").stream()
                    .filter(account -> account.getInvoiceNumber().isEmpty())
                    .sorted(Comparator.comparing(Account::getAccountNumber))
                    .collect(Collectors.toList());
            ExcelExporter.export(accounts);
        });
    }

    private void resetTableModel(CommonTableModel tableModel, int fontSize) {
        this.remove(mainScrollPane);
        this.commonTableModel = tableModel;
        table = new JTable(commonTableModel);
        table.setComponentPopupMenu(popupMenu);
        table.setFont(font);
        table.setRowHeight(fontSize + 5);

        table.getTableHeader().setFont(font);
        table.getTableHeader().setMaximumSize(new Dimension(50, 50));
        table.getTableHeader().setOpaque(false);
        table.getTableHeader().setBackground(new Color(40, 132, 189));
        table.getTableHeader().setForeground(new Color(255, 255, 255));
        table.getTableHeader().setPreferredSize(new Dimension(0, 50));

        table.addMouseListener(new TableMouseListener(table));
        table.setDefaultRenderer(Object.class, new ChangeRowColorRenderer());
        table.setSelectionBackground(new Color(180, 180, 180));
        table.setShowVerticalLines(false);
        sortTable();
        mainScrollPane = new JScrollPane(table);
        this.add(mainScrollPane, BorderLayout.CENTER);
        accountForm.setTableMode(tableModel);
        this.revalidate();
        this.repaint();
    }

    public void run() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.invokeLater(() -> new MainFrame(resource, accountService, companyService,
                    tableTypeService, inspectionOrganizationService).init());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Критическая ошибка отображения формы\nОбратитесь в поддержку",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void addNewRow() {
        accountForm.setUpdate(false);
        accountForm.showNewForm(pickTableType());
    }

    private void deleteRow() {
        int option = JOptionPane.showConfirmDialog(this, "Вы уверены, что хотите удалить этот счёт?",
                "Удаление счёта", JOptionPane.YES_NO_OPTION);
        if (option == 0) {
            int rowNumber = table.getSelectedRow();
            String accountNumber = (String) table.getValueAt(rowNumber, 0);
            LocalDate date = (LocalDate) table.getValueAt(rowNumber, 1);
            commonTableModel.deleteRow(accountNumber, date);
        }
    }

    private void updateRow() {
        Account account = findAccountForRow();
        accountForm.setUpdate(true);
        accountForm.showExistForm(account);
    }

    private void updateTable() {
        csmTableModel.updateTable();
        uniimTableModel.updateTable();
        otherTableModel.updateTable();
        serviceTableModel.updateTable();
    }

    private void showScan() {
        try {
            Account account = findAccountForRow();
            String filename = account.getAccountFile();
            if (StringUtils.isEmpty(filename)) {
                JOptionPane.showMessageDialog(this, "К этому счёту не приложен скан",
                        "Внимание", JOptionPane.INFORMATION_MESSAGE);
            } else {
                PdfRunner pdfRunner = new PdfRunner();
                pdfRunner.showScan(filename);
            }
        } catch (AccountException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void highlightOur() {
        Account account = findAccountForRow();
        Account ourAccount = accountService.buildAccount(account.getId(), account.getAccountNumber(), account.getAccountDate(),
                account.getCompany().getId(), account.getCompany().getCompany(), account.getInspectionOrganization().getId(),
                account.getInspectionOrganization().getInspectionOrganization(), account.getServiceType(),
                account.getTableType().getId(), account.getTableType().getTableType(), account.getAmount(),
                account.getAmountWithNDS(), account.getInstruments(), account.getInvoiceNumber(), account.getInvoiceDate(),
                account.getDeliveryToAccountingDate(), account.getNotes(), account.getAccountFile(), true,
                account.getInvoiceFile(), account.getRowColor());
        accountService.update(ourAccount);
        updateTable();
    }

    private void unHighlightOur() {
        Account account = findAccountForRow();
        Account ourAccount = accountService.buildAccount(account.getId(), account.getAccountNumber(), account.getAccountDate(),
                account.getCompany().getId(), account.getCompany().getCompany(), account.getInspectionOrganization().getId(),
                account.getInspectionOrganization().getInspectionOrganization(), account.getServiceType(),
                account.getTableType().getId(), account.getTableType().getTableType(), account.getAmount(),
                account.getAmountWithNDS(), account.getInstruments(), account.getInvoiceNumber(), account.getInvoiceDate(),
                account.getDeliveryToAccountingDate(), account.getNotes(), account.getAccountFile(), false,
                account.getInvoiceFile(), account.getRowColor());
        accountService.update(ourAccount);
        updateTable();
    }

    private Account findAccountForRow() {
        int rowNumber = table.getSelectedRow();
        String accountNumber = (String) table.getValueAt(rowNumber, 0);
        LocalDate date = (LocalDate) table.getValueAt(rowNumber, 1);

        return commonTableModel.findAccount(accountNumber, date);
    }

    private TableType pickTableType() {
        String tableTypeName = commonTableModel.getTableTypeName();

        return tableTypeService.findByName(tableTypeName);
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
