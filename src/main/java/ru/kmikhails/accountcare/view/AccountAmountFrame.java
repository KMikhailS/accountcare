package ru.kmikhails.accountcare.view;

import ru.kmikhails.accountcare.entity.Account;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class AccountAmountFrame extends JFrame {
    private static final Insets STANDARD_INSET = new Insets(10, 20, 0, 20);
    private static final Dimension FORM_SIZE = new Dimension(600, 175);
    private static final Font FONT = new Font(null, Font.PLAIN, 16);

    private final List<Account> CSMAccounts;

    public AccountAmountFrame(List<Account> csmAccounts) {
        this.CSMAccounts = csmAccounts;
    }

    public void init() {
        List<Account> ourAccounts = CSMAccounts.stream()
                .filter(account -> account.getOur() != null && account.getOur())
                .collect(Collectors.toList());
        BigDecimal allAmount = calculateAmount(CSMAccounts);
        BigDecimal ourAmount = calculateAmount(ourAccounts);
        BigDecimal notOurAmount = allAmount.subtract(ourAmount);

        this.setLayout(new GridBagLayout());
        this.setSize(FORM_SIZE);
        this.setResizable(false);
        this.setTitle("Суммы");
        JPanel contentPane = new JPanel(new GridBagLayout());
        this.getContentPane().add(contentPane);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.insets = STANDARD_INSET;

        JLabel amount1Label = new JLabel("Приборы заказчика");
        amount1Label.setFont(FONT);
        JLabel amount1ResultLabel = new JLabel(notOurAmount.toPlainString());
        amount1ResultLabel.setFont(FONT);
        constraints.gridx = 0;
        constraints.gridy = 1;
        contentPane.add(amount1Label, constraints);
        constraints.gridx = 0;
        constraints.gridy = 2;
        contentPane.add(amount1ResultLabel, constraints);

        JLabel amount2Label = new JLabel("Приборы собственные");
        amount2Label.setFont(FONT);
        JLabel amount2ResultLabel = new JLabel(ourAmount.toPlainString());
        amount2ResultLabel.setFont(FONT);
        constraints.gridx = 1;
        constraints.gridy = 1;
        contentPane.add(amount2Label, constraints);
        constraints.gridx = 1;
        constraints.gridy = 2;
        contentPane.add(amount2ResultLabel, constraints);

        JLabel amountAllLabel = new JLabel("Общая сумма");
        amountAllLabel.setFont(FONT);
        JLabel amountAllResultLabel = new JLabel(allAmount.toPlainString());
        amountAllResultLabel.setFont(FONT);
        constraints.gridx = 2;
        constraints.gridy = 1;
        contentPane.add(amountAllLabel, constraints);
        constraints.gridx = 2;
        constraints.gridy = 2;
        contentPane.add(amountAllResultLabel, constraints);

        this.setLocationRelativeTo(null);
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        this.setVisible(true);
    }

    private BigDecimal calculateAmount(List<Account> accounts) {
        BigDecimal amount = BigDecimal.ZERO;
        for (Account account : accounts) {
            if (account.getAmountWithNDS() != null && !account.getAmountWithNDS().isEmpty()) {
                String amountWithNDS = account.getAmountWithNDS().replace(',', '.');
                amount = amount.add(new BigDecimal(amountWithNDS));
            }
        }
        return amount;
    }
}
