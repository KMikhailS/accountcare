package ru.kmikhails.accountcare.view;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import ru.kmikhails.accountcare.entity.Account;
import ru.kmikhails.accountcare.entity.Company;
import ru.kmikhails.accountcare.entity.InspectionOrganization;
import ru.kmikhails.accountcare.entity.TableType;
import ru.kmikhails.accountcare.exception.AccountException;
import ru.kmikhails.accountcare.service.AccountService;
import ru.kmikhails.accountcare.view.tablemodel.CommonTableModel;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.util.Locale;

public class AccountForm extends JFrame {

    private static final String[] COMPANIES = {"СКБ", "КТБ", "С-1"};
    private static final String[] INSPECTION_ORGANIZATIONS = {"ЧЦСМ", "УНИИМ", "ТЕРМЕКС", "УРАЛТЕСТ", "ВНИИМС",
            "ВНИИМ", "СНИИМ", "ТЕСТ ИНТЕХ", "ВНИИОФИ", "ИМЦ МИКРО", "ФИЗПРИБОР"};
    private static final String[] TABLE_TYPES = {"ЧСЦМ", "УНИИМ", "Другие", "Прочие услуги"};

    private static final Insets STANDARD_INSET = new Insets(10, 0, 0, 0);
    private static final Insets CENTER_OFFSET_INSET = new Insets(10, 10, 0, 0);
    private static final Insets BOX_INSET = new Insets(-15, 0, 0, 0);
    private static final Insets LEFT_INSET = new Insets(10, 20, 0, 0);

    private static final Dimension FORM_SIZE = new Dimension(700, 700);
    private static final Font FONT = new Font(null, Font.PLAIN, 14);
    private static final Locale LOCALE = new Locale("ru");

    private Long accountId;
    private JLabel headLabel;
    private JPanel contentPane;
    private JLabel accountNumberLabel;
    private JTextField accountNumberTextField;
    private JLabel dateLabel;
    private JLabel companyLabel;
    private JComboBox<Company> companyBox;
    private JLabel amountLabel;
    private JTextField amountTextField;
    private JLabel amountWithNDSLabel;
    private JTextField amountWithNDSField;
    private JLabel inspectionOrganizationLabel;
    private JComboBox<InspectionOrganization> inspectionOrganizationBox;
    private JLabel instrumentsLabel;
    private JTextArea instrumentsTextArea;
    private JLabel invoiceNumberLabel;
    private JTextField invoiceNumberTextField;
    private JLabel invoiceDateLabel;
    private JLabel deliveryToAccountingDateLabel;
    private JLabel serviceTypeLabel;
    private JTextArea serviceTypeTextArea;
    private JLabel tableTypeLabel;
    private JComboBox<TableType> tableTypeBox;
    private DatePicker accountDatePicker;
    private DatePicker invoiceDatePicker;
    private DatePicker deliveryToAccountingDatePicker;
    private JLabel notesLabel;
    private JTextArea notesTextArea;
    private JTextField accountChooserTextField;
    private JTextField invoiceChooserTextField;
    private JPanel chooserPanel;
    private JButton accountChooserButton;
    private JFileChooser accountFileChooser;
    private JLabel accountChooserLabel;
    private JButton invoiceChooserButton;
    private JFileChooser invoiceFileChooser;
    private JLabel invoiceChooserLabel;
    private JButton saveButton;
    private JButton cancelButton;

    private final AccountService accountService;
    private CommonTableModel tableModel;
    private final Company[] companies;
    private final InspectionOrganization[] organizations;
    private final TableType[] tableTypes;

    public AccountForm(AccountService accountService, CommonTableModel tableModel, Company[] companies, TableType[] tableTypes,
                       InspectionOrganization[] organizations) {
        this.accountService = accountService;
        this.tableModel = tableModel;
        this.companies = companies;
        this.organizations = organizations;
        this.tableTypes = tableTypes;
        init();
    }

    public void setTableMode(CommonTableModel tableModel) {
        this.tableModel = tableModel;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public void init() {
        this.setLayout(new GridBagLayout());
        this.setSize(FORM_SIZE);
        this.setResizable(false);
        contentPane = new JPanel(new GridBagLayout());
        this.getContentPane().add(contentPane);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.insets = STANDARD_INSET;

        accountNumberLabel = new JLabel("Номер счёта");
        accountNumberLabel.setFont(FONT);
        accountNumberTextField = new JTextField(13);
        accountNumberTextField.setFont(FONT);
        constraints.gridx = 0;
        constraints.gridy = 1;
        contentPane.add(accountNumberLabel, constraints);
        constraints.gridx = 0;
        constraints.gridy = 2;
        contentPane.add(accountNumberTextField, constraints);

        dateLabel = new JLabel("Дата счёта");
        dateLabel.setFont(FONT);
        accountDatePicker = new DatePicker();
        DatePickerSettings accountDateSettings = new DatePickerSettings(LOCALE);
        accountDateSettings.setFirstDayOfWeek(DayOfWeek.MONDAY);
        accountDateSettings.setFontMonthAndYearMenuLabels(FONT);
        accountDateSettings.setFontMonthAndYearNavigationButtons(FONT);
        accountDateSettings.setFontTodayLabel(FONT);
        accountDateSettings.setFontClearLabel(FONT);
        accountDateSettings.setFontCalendarDateLabels(FONT);
        accountDateSettings.setFontCalendarWeekdayLabels(FONT);
        accountDateSettings.setFontCalendarWeekNumberLabels(FONT);
        accountDateSettings.setFontValidDate(FONT);
        accountDatePicker = new DatePicker(accountDateSettings);

        constraints.insets = CENTER_OFFSET_INSET;
        constraints.gridx = 1;
        constraints.gridy = 1;
        contentPane.add(dateLabel, constraints);
        constraints.gridx = 1;
        constraints.gridy = 2;
        contentPane.add(accountDatePicker, constraints);

        companyLabel = new JLabel("Предприятие");
        companyLabel.setFont(FONT);
        companyBox = new JComboBox<>(companies);
        companyBox.setFont(FONT);
        constraints.insets = LEFT_INSET;
        constraints.gridx = 2;
        constraints.gridy = 1;
        contentPane.add(companyLabel, constraints);
        constraints.gridx = 2;
        constraints.gridy = 2;
        contentPane.add(companyBox, constraints);

        inspectionOrganizationLabel = new JLabel("Поверяющая организация");
        inspectionOrganizationLabel.setFont(FONT);
        inspectionOrganizationBox = new JComboBox<>(organizations);
        inspectionOrganizationBox.setFont(FONT);
        constraints.insets = STANDARD_INSET;
        constraints.gridx = 0;
        constraints.gridy = 3;
        contentPane.add(inspectionOrganizationLabel, constraints);
        constraints.insets = BOX_INSET;
        constraints.gridx = 0;
        constraints.gridy = 4;
        contentPane.add(inspectionOrganizationBox, constraints);

        serviceTypeLabel = new JLabel("Вид услуги");
        serviceTypeLabel.setFont(FONT);
        serviceTypeTextArea = new JTextArea(3, 20);
        serviceTypeTextArea.setFont(FONT);
        serviceTypeTextArea.setLineWrap(true);
        serviceTypeTextArea.setWrapStyleWord(true);
        constraints.insets = CENTER_OFFSET_INSET;
        constraints.gridx = 1;
        constraints.gridy = 3;
        contentPane.add(serviceTypeLabel, constraints);
        constraints.gridx = 1;
        constraints.gridy = 4;
        contentPane.add(new JScrollPane(serviceTypeTextArea), constraints);

        tableTypeLabel = new JLabel("Тип таблицы");
        tableTypeLabel.setFont(FONT);
        tableTypeBox = new JComboBox<>(tableTypes);
        tableTypeBox.setFont(FONT);
        constraints.insets = LEFT_INSET;
        constraints.gridx = 2;
        constraints.gridy = 3;
        contentPane.add(tableTypeLabel, constraints);
        constraints.insets = new Insets(-15, 20, 0, 0);
        constraints.gridx = 2;
        constraints.gridy = 4;
        contentPane.add(tableTypeBox, constraints);

        amountLabel = new JLabel("Сумма");
        amountLabel.setFont(FONT);
        amountTextField = new JTextField(13);
        amountTextField.setFont(FONT);
        constraints.insets = STANDARD_INSET;
        constraints.gridx = 0;
        constraints.gridy = 5;
        contentPane.add(amountLabel, constraints);
        constraints.insets = new Insets(-60, 0, 0, 0);
        constraints.gridx = 0;
        constraints.gridy = 6;
        contentPane.add(amountTextField, constraints);

        amountWithNDSLabel = new JLabel("Сумма с НДС");
        amountWithNDSLabel.setFont(FONT);
        amountWithNDSField = new JTextField(13);
        amountWithNDSField.setFont(FONT);
        constraints.insets = CENTER_OFFSET_INSET;
        constraints.gridx = 1;
        constraints.gridy = 5;
        contentPane.add(amountWithNDSLabel, constraints);
        constraints.insets = new Insets(-60, 10, 0, 0);
        constraints.gridx = 1;
        constraints.gridy = 6;
        contentPane.add(amountWithNDSField, constraints);

        instrumentsLabel = new JLabel("Наименования СИ");
        instrumentsLabel.setFont(FONT);
        instrumentsTextArea = new JTextArea(5, 15);
        instrumentsTextArea.setFont(FONT);
        instrumentsTextArea.setLineWrap(true);
        instrumentsTextArea.setWrapStyleWord(true);
        constraints.insets = LEFT_INSET;
        constraints.gridx = 2;
        constraints.gridy = 5;
        contentPane.add(instrumentsLabel, constraints);
        constraints.gridx = 2;
        constraints.gridy = 6;
        contentPane.add(new JScrollPane(instrumentsTextArea), constraints);

        invoiceNumberLabel = new JLabel("Номер счёта фактуры");
        invoiceNumberLabel.setFont(FONT);
        invoiceNumberTextField = new JTextField(13);
        invoiceNumberTextField.setFont(FONT);
        constraints.insets = STANDARD_INSET;
        constraints.gridx = 0;
        constraints.gridy = 7;
        contentPane.add(invoiceNumberLabel, constraints);
        constraints.gridx = 0;
        constraints.gridy = 8;
        contentPane.add(invoiceNumberTextField, constraints);

        invoiceDateLabel = new JLabel("Дата счета фактуры");
        invoiceDateLabel.setFont(FONT);
        invoiceDatePicker = new DatePicker();
        DatePickerSettings invoiceDateSettings = new DatePickerSettings(LOCALE);
        invoiceDateSettings.setFirstDayOfWeek(DayOfWeek.MONDAY);
        invoiceDateSettings.setFontMonthAndYearMenuLabels(FONT);
        invoiceDateSettings.setFontMonthAndYearNavigationButtons(FONT);
        invoiceDateSettings.setFontTodayLabel(FONT);
        invoiceDateSettings.setFontClearLabel(FONT);
        invoiceDateSettings.setFontCalendarDateLabels(FONT);
        invoiceDateSettings.setFontCalendarWeekdayLabels(FONT);
        invoiceDateSettings.setFontCalendarWeekNumberLabels(FONT);
        invoiceDateSettings.setFontValidDate(FONT);
        invoiceDatePicker = new DatePicker(invoiceDateSettings);

        constraints.insets = CENTER_OFFSET_INSET;
        constraints.gridx = 1;
        constraints.gridy = 7;
        contentPane.add(invoiceDateLabel, constraints);
        constraints.gridx = 1;
        constraints.gridy = 8;
        contentPane.add(invoiceDatePicker, constraints);

        deliveryToAccountingDateLabel = new JLabel("Дата сдачи в бухгалтерию");
        deliveryToAccountingDateLabel.setFont(FONT);
        deliveryToAccountingDatePicker = new DatePicker();
        DatePickerSettings deliveryToAccountingDateSettings = new DatePickerSettings(LOCALE);
        deliveryToAccountingDateSettings.setFirstDayOfWeek(DayOfWeek.MONDAY);
        deliveryToAccountingDateSettings.setFontMonthAndYearMenuLabels(FONT);
        deliveryToAccountingDateSettings.setFontMonthAndYearNavigationButtons(FONT);
        deliveryToAccountingDateSettings.setFontTodayLabel(FONT);
        deliveryToAccountingDateSettings.setFontClearLabel(FONT);
        deliveryToAccountingDateSettings.setFontCalendarDateLabels(FONT);
        deliveryToAccountingDateSettings.setFontCalendarWeekdayLabels(FONT);
        deliveryToAccountingDateSettings.setFontCalendarWeekNumberLabels(FONT);
        deliveryToAccountingDateSettings.setFontValidDate(FONT);
        deliveryToAccountingDatePicker = new DatePicker(deliveryToAccountingDateSettings);

        constraints.insets = LEFT_INSET;
        constraints.gridx = 2;
        constraints.gridy = 7;
        contentPane.add(deliveryToAccountingDateLabel, constraints);
        constraints.gridx = 2;
        constraints.gridy = 8;
        contentPane.add(deliveryToAccountingDatePicker, constraints);

        notesLabel = new JLabel("Примечания");
        notesLabel.setFont(FONT);
        notesTextArea = new JTextArea(5, 15);
        notesTextArea.setFont(FONT);
        notesTextArea.setLineWrap(true);
        notesTextArea.setWrapStyleWord(true);
        constraints.insets = STANDARD_INSET;
        constraints.gridx = 0;
        constraints.gridy = 9;
        contentPane.add(notesLabel, constraints);
        constraints.gridx = 0;
        constraints.gridy = 10;
        contentPane.add(new JScrollPane(notesTextArea), constraints);

        chooserPanel = new JPanel();
        accountFileChooser = new JFileChooser();
        chooserPanel.setLayout(new FlowLayout());
        accountChooserTextField = new JTextField(30);
        accountChooserTextField.setFont(FONT);
        accountChooserButton = new JButton("Открыть");
        accountChooserButton.setFont(FONT);
        accountChooserButton.addActionListener(e -> {
            if (accountFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                accountChooserTextField.setText(accountFileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        invoiceFileChooser = new JFileChooser();
        invoiceChooserTextField = new JTextField(30);
        invoiceChooserTextField.setFont(FONT);
        invoiceChooserButton = new JButton("Открыть");
        invoiceChooserButton.setFont(FONT);
        invoiceChooserButton.addActionListener(e -> {
            if (invoiceFileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                invoiceChooserTextField.setText(invoiceFileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        chooserPanel.add(accountChooserTextField);
        chooserPanel.add(accountChooserButton);
//        chooserPanel.add(invoiceChooserTextField);
//        chooserPanel.add(invoiceChooserButton);
        accountChooserLabel = new JLabel("Открыть файл скана счёта");
        accountChooserLabel.setFont(FONT);
//        invoiceChooserLabel = new JLabel("Открыть файл скана счёта фактуры");
//        invoiceChooserLabel.setFont(FONT);
        constraints.insets = new Insets(0, 10, 0, 0);
        constraints.gridx = 1;
        constraints.gridy = 10;
        constraints.gridwidth = 2;
        contentPane.add(chooserPanel, constraints);
        constraints.gridx = 1;
        constraints.gridy = 9;
        constraints.gridwidth = 2;
        constraints.insets = new Insets(30, -10, 0, 0);
        constraints.anchor = GridBagConstraints.CENTER;
        contentPane.add(accountChooserLabel, constraints);

        saveButton = new JButton("Сохранить");
        saveButton.setFont(FONT);
        saveButton.addActionListener(e -> {
            try {
                Account account;
                if (accountNumberTextField.getText() != null && accountDatePicker.getDate() != null) {
                    Account existAccount = accountService.findById(accountId);
                    if (existAccount != null && "NEW".equals(existAccount.getStatus())) {
                        account = accountService.buildAccount(existAccount.getId(), accountNumberTextField.getText(), accountDatePicker.getDate(),
                                ((Company) companyBox.getSelectedItem()).getId(), ((Company) companyBox.getSelectedItem()).getCompany(),
                                ((InspectionOrganization) inspectionOrganizationBox.getSelectedItem()).getId(),
                                ((InspectionOrganization) inspectionOrganizationBox.getSelectedItem()).getInspectionOrganization(),
                                serviceTypeTextArea.getText(), ((TableType) tableTypeBox.getSelectedItem()).getId(),
                                ((TableType) tableTypeBox.getSelectedItem()).getTableType(), amountTextField.getText(),
                                amountWithNDSField.getText(), instrumentsTextArea.getText(), invoiceNumberTextField.getText(),
                                invoiceDatePicker.getDate(), deliveryToAccountingDatePicker.getDate(), notesTextArea.getText(),
                                accountChooserTextField.getText(), existAccount.getOur(), null, existAccount.getRowColor());
                        tableModel.update(account);
                    } else {
                        account = accountService.buildAccount(null, accountNumberTextField.getText(), accountDatePicker.getDate(),
                                ((Company) companyBox.getSelectedItem()).getId(), ((Company) companyBox.getSelectedItem()).getCompany(),
                                ((InspectionOrganization) inspectionOrganizationBox.getSelectedItem()).getId(),
                                ((InspectionOrganization) inspectionOrganizationBox.getSelectedItem()).getInspectionOrganization(),
                                serviceTypeTextArea.getText(), ((TableType) tableTypeBox.getSelectedItem()).getId(),
                                ((TableType) tableTypeBox.getSelectedItem()).getTableType(), amountTextField.getText(),
                                amountWithNDSField.getText(), instrumentsTextArea.getText(), invoiceNumberTextField.getText(),
                                invoiceDatePicker.getDate(), deliveryToAccountingDatePicker.getDate(), notesTextArea.getText(),
                                accountChooserTextField.getText(), false, null, null);
                        tableModel.addRow(account);
                    }
                } else {
                    throw new AccountException("Номер счёта и дата не должны быть пустыми");
                }

                this.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });
        constraints.gridx = 0;
        constraints.gridy = 11;
        constraints.gridwidth = 3;
        constraints.insets = new Insets(50, 0, 0, 50);
        contentPane.add(saveButton, constraints);

        cancelButton = new JButton("Отмена");
        cancelButton.setFont(FONT);
        cancelButton.addActionListener(e -> this.dispose());
        constraints.gridx = 1;
        constraints.gridy = 11;
        constraints.gridwidth = 3;
        constraints.insets = new Insets(50, 25, 0, 0);
        contentPane.add(cancelButton, constraints);


        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    public void showNewForm(TableType tableType) {
        setAccountId(null);
        accountNumberTextField.setText("");
        amountTextField.setText("");
        amountWithNDSField.setText("");
        instrumentsTextArea.setText("");
        invoiceNumberTextField.setText("");
        serviceTypeTextArea.setText("");
        accountDatePicker.setText("");
        invoiceDatePicker.setText("");
        deliveryToAccountingDatePicker.setText("");
        notesTextArea.setText("");
        accountChooserTextField.setText("");
        tableTypeBox.setSelectedItem(TableType.builder()
                .withId(tableType.getId())
                .withTableType(tableType.getTableType())
                .build());
        this.setVisible(true);
    }

    public void showExistForm(Account account) {
        setAccountId(account.getId());
        accountNumberTextField.setText(account.getAccountNumber());
        companyBox.setSelectedItem(Company.builder()
                .withId(account.getCompany().getId())
                .withCompany(account.getCompany().getCompany())
                .build());
        inspectionOrganizationBox.setSelectedItem(InspectionOrganization.builder()
                .withId(account.getInspectionOrganization().getId())
                .withInspectionOrganization(account.getInspectionOrganization().getInspectionOrganization())
                .build());
        tableTypeBox.setSelectedItem(TableType.builder()
                .withId(account.getTableType().getId())
                .withTableType(account.getTableType().getTableType())
                .build());
        amountTextField.setText(account.getAmount());
        amountWithNDSField.setText(account.getAmountWithNDS());
        instrumentsTextArea.setText(account.getInstruments());
        invoiceNumberTextField.setText(account.getInvoiceNumber());
        serviceTypeTextArea.setText(account.getServiceType());
        accountDatePicker.setDate(account.getAccountDate());
        invoiceDatePicker.setDate(account.getInvoiceDate());
        deliveryToAccountingDatePicker.setDate(account.getDeliveryToAccountingDate());
        notesTextArea.setText(account.getNotes());
        accountChooserTextField.setText(account.getAccountFile());
        this.setVisible(true);
    }
}
