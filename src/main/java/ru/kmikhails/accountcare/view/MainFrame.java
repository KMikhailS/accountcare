package ru.kmikhails.accountcare.view;

import ru.kmikhails.accountcare.entity.Account;
import ru.kmikhails.accountcare.repository.impl.MockAccountRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

public class MainFrame extends JFrame implements ActionListener {
	private static final String ADD_ROW = "Добавить строку";
	private static final String DELETE_ROW = "Удалить строку";
	private static final String DELETE_ALL_ROWS = "Удалить все строки";

	private final AccountTableModel tableModel;
	private final JTable table;

	public MainFrame() {
		MockAccountRepository accountRepository = new MockAccountRepository();
		tableModel = new AccountTableModel(accountRepository);

		table = new JTable(tableModel);
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem menuItemAdd = new JMenuItem(ADD_ROW);
		JMenuItem menuItemRemove = new JMenuItem(DELETE_ROW);
		JMenuItem menuItemRemoveAll = new JMenuItem(DELETE_ALL_ROWS);
		menuItemAdd.addActionListener(this);
		menuItemRemove.addActionListener(this);
		menuItemRemoveAll.addActionListener(this);
		popupMenu.add(menuItemAdd);
		popupMenu.add(menuItemRemove);
		popupMenu.add(menuItemRemoveAll);

		table.setComponentPopupMenu(popupMenu);
		table.addMouseListener(new TableMouseListener(table));

		Font font = new Font(null, Font.BOLD, 14);
		table.setFont(font);
		table.getTableHeader().setFont(font);

		this.add(new JScrollPane(table));
		this.setTitle("Test Table");
		this.setSize(1000, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		this.pack();
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
				throw new IllegalArgumentException("");
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(MainFrame::new);
	}

	private void addNewRow() {

	}

	private void deleteRow() {
		int rowNumber = table.getSelectedRow();
		String accountNumber = (String) table.getValueAt(rowNumber, 0);
		String company = (String) table.getValueAt(rowNumber, 2);
		tableModel.deleteRow(accountNumber, company);
	}
}
