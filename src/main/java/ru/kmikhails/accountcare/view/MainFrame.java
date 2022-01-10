package ru.kmikhails.accountcare.view;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.kmikhails.accountcare.entity.*;
import ru.kmikhails.accountcare.exception.AccountException;
import ru.kmikhails.accountcare.service.AccountService;
import ru.kmikhails.accountcare.service.YearService;
import ru.kmikhails.accountcare.service.impl.CompanyService;
import ru.kmikhails.accountcare.service.impl.InspectionOrganizationService;
import ru.kmikhails.accountcare.service.impl.TableTypeService;
import ru.kmikhails.accountcare.util.ExcelExporter;
import ru.kmikhails.accountcare.util.PdfRunner;
import ru.kmikhails.accountcare.util.StringUtils;
import ru.kmikhails.accountcare.view.settings.CompanySettingsFrame;
import ru.kmikhails.accountcare.view.settings.OrganizationSettingsFrame;
import ru.kmikhails.accountcare.view.settings.ReconfigureAccountFormListener;
import ru.kmikhails.accountcare.view.settings.YearSettings;
import ru.kmikhails.accountcare.view.tablemodel.*;
import ru.kmikhails.accountcare.view.util.ChangeRowColorRenderer;
import ru.kmikhails.accountcare.view.util.ComponentBorder;
import ru.kmikhails.accountcare.view.util.TableMouseListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainFrame extends JFrame implements ActionListener, ReconfigureAccountFormListener {
    private static final Logger LOG = LogManager.getLogger(MainFrame.class);
    private static final String ADD_ROW = "Добавить счёт";
    private static final String DELETE_ROW = "Удалить счёт";
    private static final String UPDATE_ROW = "Обновить счёт";
    private static final String SHOW_SCAN = "Показать скан";
    private static final String UPDATE_TABLE = "Обновить таблицу";
    private static final String SETTINGS = "Настройки";
    private static final String COMPANY_SETTINGS = "Предприятие";
    private static final String ORGANIZATION_SETTINGS = "Поверяющая организация";
    private static final String YEAR_SETTINGS = "Год";
    private static final String LABELS = "Метки";
    private static final String OUR_LABEL = "НАШ";
    private static final String PREPAYMENT_LABEL = "АВАНС";
    private static final String EXPORT = "Экспорт";
    private static final String EXPORT_EXCEL_CSM = "Экспорт счетов без фактур ЧЦСМ";
    private static final String MENU = "Меню";

    private final AccountService accountService;
    private final CompanyService companyService;
    private final InspectionOrganizationService inspectionOrganizationService;
    private final TableTypeService tableTypeService;
    private final ResourceBundle resource;
    private final YearService yearService;

    private CSMTableModel csmTableModel;
    private UNIIMTableModel uniimTableModel;
    private OtherTableModel otherTableModel;
    private ServiceTableModel serviceTableModel;
    private JTextField searchTextField;
    private JComboBox<TableType> serviceComboBox;
    private JPopupMenu popupMenu;
    private Font font;
    private CommonTableModel commonTableModel;
    private JTable table;
    private JPanel buttonPanel;
    private JScrollPane mainScrollPane;
    private JButton searchCancelButton;
    private Company[] companies;
    private InspectionOrganization[] organizations;
    private TableType[] tableTypes;
    private AccountForm accountForm;
    private JComboBox<String> yearComboBox;
    private String year;
    private JMenuItem menuItemSetOur;
    private JMenuItem menuItemSetPrepayment;

    public MainFrame(ResourceBundle resource, AccountService accountService, CompanyService companyService,
                     TableTypeService tableTypeService, InspectionOrganizationService inspectionOrganizationService,
                     YearService yearService) {
        this.resource = resource;
        this.accountService = accountService;
        this.companyService = companyService;
        this.inspectionOrganizationService = inspectionOrganizationService;
        this.tableTypeService = tableTypeService;
        this.yearService = yearService;
    }

    public void run() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.invokeLater(() -> new MainFrame(resource, accountService, companyService,
                    tableTypeService, inspectionOrganizationService, yearService).init());
        } catch (Exception e) {
            LOG.error("Критическая ошибка отображения формы", e);
            JOptionPane.showMessageDialog(this, "Критическая ошибка отображения формы\nОбратитесь в поддержку",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void init() {
        int fontSize = Integer.parseInt(resource.getString("font.size"));
        YearValues yearValues = yearService.getYearValues();
        String[] years = yearValues.getRange().split(",");
        String defaultYear = yearValues.getDefaultValue();

        font = new Font(null, Font.PLAIN, fontSize);
        tableTypes = tableTypeService.findAll().toArray(new TableType[0]);

        configureYear(fontSize, years, defaultYear);
        configureTableModel(year);
        configureTable(fontSize);
        configureMenu();
        configurePopupMenu();
        printMainInterface(fontSize);

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private void configureYear(int fontSize, String[] years, String defaultYear) {
        yearComboBox = new JComboBox<>();
        yearComboBox.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
        yearComboBox.setModel(new DefaultComboBoxModel<>(years));
        yearComboBox.setSelectedItem(defaultYear);
        yearComboBox.addActionListener(e -> changeYear());
        year = yearComboBox.getSelectedItem().toString();
    }

    private void changeYear() {
        year = yearComboBox.getSelectedItem().toString();
        commonTableModel.updateYear(year);
    }

    private void configureTableModel(String year) {
        List<Account> CSMAccounts = accountService.findAllByTableType("ЧЦСМ", year);
        csmTableModel = new CSMTableModel(accountService, CSMAccounts);
        commonTableModel = csmTableModel;
    }

    private void configureUNIIMTableModel(String year) {
        if (uniimTableModel == null) {
            List<Account> UNIIMAccounts = accountService.findAllByTableType("УНИИМ", year);
            uniimTableModel = new UNIIMTableModel(accountService, UNIIMAccounts);
        }
    }

    private void configureOtherTableModel(String year) {
        if (otherTableModel == null) {
            List<Account> otherAccounts = accountService.findAllByTableType("другие", year);
            otherTableModel = new OtherTableModel(accountService, otherAccounts);
        }
    }

    private void configureServiceTableModel(String year) {
        if (serviceTableModel == null) {
            List<Account> serviceAccounts = accountService.findAllByTableType("прочие услуги", year);
            serviceTableModel = new ServiceTableModel(accountService, serviceAccounts);
        }
    }

    private void configureAccountForm(boolean isReconfig) {
        if (accountForm == null || isReconfig) {
            companies = companyService.findAll().toArray(new Company[0]);
            organizations = inspectionOrganizationService.findAll().toArray(new InspectionOrganization[0]);
            accountForm = new AccountForm(accountService, commonTableModel, companies, tableTypes, organizations, year);
        }
    }

    private void configureTable(int fontSize) {
        table = new JTable(commonTableModel);
        table.setDefaultRenderer(Object.class, new ChangeRowColorRenderer());
        table.setSelectionBackground(new Color(180, 180, 180));
        table.setShowVerticalLines(false);
        table.setRowHeight(fontSize + 5);
        table.setFont(font);
        table.getTableHeader().setFont(font);
        table.getTableHeader().setMaximumSize(new Dimension(50, 50));
        table.getTableHeader().setOpaque(false);
        table.getTableHeader().setBackground(new Color(40, 132, 189));
        table.getTableHeader().setForeground(new Color(255, 255, 255));
        table.getTableHeader().setPreferredSize(new Dimension(0, 50));
        mainScrollPane = new JScrollPane(table);
        table.addMouseListener(new TableMouseListener(table));
        sortTable();
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

    private void configureMenu() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu mainMenu = new JMenu(MENU);
        menuBar.add(mainMenu);

        JMenu exportMenu = new JMenu(EXPORT);
        menuBar.add(exportMenu);

        JMenu settingsMenu = new JMenu(SETTINGS);
        menuBar.add(settingsMenu);

        JMenuItem addAccountMenuItem = new JMenuItem(ADD_ROW);
        mainMenu.add(addAccountMenuItem);
        addAccountMenuItem.addActionListener(e -> addNewRow());

        JMenuItem updateTableMenuItem = new JMenuItem(UPDATE_TABLE);
        mainMenu.add(updateTableMenuItem);
        updateTableMenuItem.addActionListener(e -> updateTable());

        JMenuItem companySettingsTableMenuItem = new JMenuItem(COMPANY_SETTINGS);
        settingsMenu.add(companySettingsTableMenuItem);
        companySettingsTableMenuItem.addActionListener(e -> openCompanySettings());

        JMenuItem organizationSettingsTableMenuItem = new JMenuItem(ORGANIZATION_SETTINGS);
        settingsMenu.add(organizationSettingsTableMenuItem);
        organizationSettingsTableMenuItem.addActionListener(e -> openOrganizationSettings());

        JMenuItem yearSettingsTableMenuItem = new JMenuItem(YEAR_SETTINGS);
        settingsMenu.add(yearSettingsTableMenuItem);
        yearSettingsTableMenuItem.addActionListener(e -> openYearSettings());

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
        JMenu menuItemLabels = new JMenu(LABELS);
        menuItemSetOur = new JMenuItem(OUR_LABEL);
        menuItemSetPrepayment = new JMenuItem(PREPAYMENT_LABEL);
        menuItemLabels.add(menuItemSetOur);
        menuItemLabels.add(menuItemSetPrepayment);

        menuItemAdd.addActionListener(this);
        menuItemRemove.addActionListener(this);
        menuItemUpdate.addActionListener(this);
        menuItemShowScan.addActionListener(this);
        menuItemSetOur.addActionListener(this);
        menuItemSetPrepayment.addActionListener(this);
        
        popupMenu.add(menuItemAdd);
        popupMenu.add(menuItemUpdate);
        popupMenu.add(menuItemRemove);
        popupMenu.add(new JSeparator());
        popupMenu.add(menuItemShowScan);
        popupMenu.add(new JSeparator());
        popupMenu.add(menuItemLabels);

        popupMenu.addPopupMenuListener(setPopupMenuListener());

        table.setComponentPopupMenu(popupMenu);
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

        searchCancelButton = new JButton();
        setSearchCancelButtonIcon();

        searchTextField = new JTextField();
        searchTextField.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
        searchTextField.setBounds(850, 36, 200, fontSize + 8);
        searchTextField.getDocument().addDocumentListener(setDocumentListener());
        ComponentBorder searchComponent = new ComponentBorder(searchCancelButton);
        searchComponent.install(searchTextField);
        searchComponent.setEdge(ComponentBorder.Edge.RIGHT);
        buttonPanel.add(searchTextField);

        serviceComboBox = new JComboBox<>(tableTypes);
        serviceComboBox.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
        serviceComboBox.setBounds(181, 36, 160, fontSize + 5);
        serviceComboBox.addActionListener(e -> setServiceComboBoxListener(fontSize));
        buttonPanel.add(serviceComboBox);

        this.add(buttonPanel, BorderLayout.NORTH);
        this.add(mainScrollPane, BorderLayout.CENTER);
        this.setTitle("Счета метрологии");
        setMainIcon();
    }

    private void setSearchCancelButtonIcon() {
        byte[] crossBytes = getIconBytes("cross.png");
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

    private void setMainIcon() {
        byte[] mainIconBytes = getIconBytes("icon.png");
        if (mainIconBytes != null && mainIconBytes.length > 0) {
            ImageIcon mainIcon = new ImageIcon(mainIconBytes);
            this.setIconImage(mainIcon.getImage());
        }
    }

    private void setLabelIcon(JMenuItem menuItem, boolean isActive) {
        ImageIcon labelIcon = getLabelIcon();
        if (isActive) {
            menuItem.setIcon(labelIcon);
        } else {
            menuItem.setIcon(null);
        }
    }

    private ImageIcon getLabelIcon() {
        byte[] labelIconBytes = getIconBytes("check_mark.png");
        if (labelIconBytes != null && labelIconBytes.length > 0) {
            return new ImageIcon(labelIconBytes);
        }
        return null;
    }

    private byte[] getIconBytes(String fileName) {
        try (InputStream iconInputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (iconInputStream != null) {
                return IOUtils.toByteArray(iconInputStream);
            }
        } catch (IOException e) {
            LOG.error("Ошибка чтения файла иконки", e);
        }
        throw new AccountException("Ошибка установки иконки");
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
            case OUR_LABEL:
                if (menuItemSetOur.getIcon() == null) {
                    highlightOur();
                } else {
                    unHighlightOur();
                }
                break;
            case PREPAYMENT_LABEL:
                if (menuItemSetPrepayment.getIcon() == null) {
                    highlightPrepayment();
                } else {
                    unHighlightPrepayment();
                }
                break;
            default:
                JOptionPane.showMessageDialog(this, String.format("Ошибка при выборе меню [%s]", menuItem.getText()),
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addNewRow() {
        configureAccountForm(false);
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
            commonTableModel.deleteRow(accountNumber, date, year);
        }
    }

    private void updateRow() {
        Account account = findAccountForRow();
        configureAccountForm(false);
        accountForm.setUpdate(true);
        accountForm.showExistForm(account);
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
                account.getDeliveryToAccountingDate(), account.getNotes(), account.getAccountFile(), true, account.getPrepayment(),
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
                account.getDeliveryToAccountingDate(), account.getNotes(), account.getAccountFile(), false, account.getPrepayment(),
                account.getInvoiceFile(), account.getRowColor());
        accountService.update(ourAccount);
        updateTable();
    }

    private void highlightPrepayment() {
        Account account = findAccountForRow();
        Account ourAccount = accountService.buildAccount(account.getId(), account.getAccountNumber(), account.getAccountDate(),
                account.getCompany().getId(), account.getCompany().getCompany(), account.getInspectionOrganization().getId(),
                account.getInspectionOrganization().getInspectionOrganization(), account.getServiceType(),
                account.getTableType().getId(), account.getTableType().getTableType(), account.getAmount(),
                account.getAmountWithNDS(), account.getInstruments(), account.getInvoiceNumber(), account.getInvoiceDate(),
                account.getDeliveryToAccountingDate(), account.getNotes(), account.getAccountFile(), account.getOur(), true,
                account.getInvoiceFile(), account.getRowColor());
        accountService.update(ourAccount);
        updateTable();
    }

    private void unHighlightPrepayment() {
        Account account = findAccountForRow();
        Account ourAccount = accountService.buildAccount(account.getId(), account.getAccountNumber(), account.getAccountDate(),
                account.getCompany().getId(), account.getCompany().getCompany(), account.getInspectionOrganization().getId(),
                account.getInspectionOrganization().getInspectionOrganization(), account.getServiceType(),
                account.getTableType().getId(), account.getTableType().getTableType(), account.getAmount(),
                account.getAmountWithNDS(), account.getInstruments(), account.getInvoiceNumber(), account.getInvoiceDate(),
                account.getDeliveryToAccountingDate(), account.getNotes(), account.getAccountFile(), account.getOur(), false,
                account.getInvoiceFile(), account.getRowColor());
        accountService.update(ourAccount);
        updateTable();
    }

    private void exportToExel() {
        SwingUtilities.invokeLater(() -> {
            List<Account> accounts = accountService.findAllByTableType("ЧЦСМ", year).stream()
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
        if (accountForm != null) {
            accountForm.setTableMode(tableModel);
        }
        this.revalidate();
        this.repaint();
    }

    private void updateTable() {
        csmTableModel.updateTable(year);
        if (uniimTableModel == null) {
            configureUNIIMTableModel(year);
        }
        uniimTableModel.updateTable(year);
        if (otherTableModel == null) {
            configureOtherTableModel(year);
        }
        otherTableModel.updateTable(year);
        if (serviceTableModel == null) {
            configureServiceTableModel(year);
        }
        serviceTableModel.updateTable(year);
    }

    private void openCompanySettings() {
        List<String> companies = companyService.findAll().stream()
                .map(Company::getCompany)
                .collect(Collectors.toList());
        new CompanySettingsFrame(companyService, companies, COMPANY_SETTINGS, this).init();
    }

    private void openOrganizationSettings() {
        List<String> organizations = inspectionOrganizationService.findAll().stream()
                .map(InspectionOrganization::getInspectionOrganization)
                .collect(Collectors.toList());
        new OrganizationSettingsFrame(inspectionOrganizationService, organizations, ORGANIZATION_SETTINGS, this).init();
    }

    private void openYearSettings() {
        new YearSettings(yearService).init();
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

    private DocumentListener setDocumentListener() {
        return new DocumentListener() {
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
                    int rowIndex = table.getRowSorter().convertRowIndexToView(i);
                    String valueAt = (String) table.getModel().getValueAt(i, 0);
                    if (searchTextField.getText().equalsIgnoreCase(valueAt)) {
                        table.setRowSelectionInterval(rowIndex, rowIndex);
                    } else {
                        table.removeRowSelectionInterval(rowIndex, rowIndex);
                    }
                }
            }
        };
    }

    private PopupMenuListener setPopupMenuListener() {
        return new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(() -> {
                    Account account = findAccountForRow();
                    setLabelIcon(menuItemSetOur, account.getOur());
                    setLabelIcon(menuItemSetPrepayment, account.getPrepayment());
                });
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        };
    }

    private void setServiceComboBoxListener(int fontSize) {
        String serviceType = ((TableType) serviceComboBox.getSelectedItem()).getTableType();
        if (serviceType != null) {
            switch (serviceType) {
                case "ЧЦСМ":
                    resetTableModel(csmTableModel, fontSize);
                    break;
                case "УНИИМ":
                    configureUNIIMTableModel(year);
                    resetTableModel(uniimTableModel, fontSize);
                    break;
                case "другие":
                    configureOtherTableModel(year);
                    resetTableModel(otherTableModel, fontSize);
                    break;
                case "прочие услуги":
                    configureServiceTableModel(year);
                    resetTableModel(serviceTableModel, fontSize);
                    break;
                default:
                    throw new IllegalArgumentException("Нет такой модели таблицы");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Не выбран тип таблицы",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void reconfigureAccountForm() {
        configureAccountForm(true);
    }
}
