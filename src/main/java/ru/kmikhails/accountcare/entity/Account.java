package ru.kmikhails.accountcare.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Account {
	private final Long id;
	private final String accountNumber;
	private final LocalDate accountDate;
	private final Company company;
	private final String serviceType;
	private final String instruments;
	private final String amount;
	private final String amountWithNDS;
	private final String invoiceNumber;
	private final LocalDate invoiceDate;
	private final LocalDate deliveryToAccountingDate;
	private final InspectionOrganization inspectionOrganization;
	private final String notes;
	private final String accountFile;
	private String status;
	private final TableType tableType;

	private Account(Builder builder) {
		this.id = builder.id;
		this.accountNumber = builder.accountNumber;
		this.accountDate = builder.accountDate;
		this.company = builder.company;
		this.serviceType = builder.serviceType;
		this.amount = builder.amount;
		this.instruments = builder.instruments;
		this.amountWithNDS = builder.amountWithNDS;
		this.invoiceNumber = builder.invoiceNumber;
		this.invoiceDate = builder.invoiceDate;
		this.deliveryToAccountingDate = builder.deliveryToAccountingDate;
		this.inspectionOrganization = builder.inspectionOrganization;
		this.notes = builder.notes;
		this.accountFile = builder.accountFile;
		this.status = builder.status;
		this.tableType = builder.tableType;
	}

	public Long getId() {
		return id;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public LocalDate getAccountDate() {
		return accountDate;
	}

	public Company getCompany() {
		return company;
	}

	public String getAmount() {
		return amount;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public String getServiceType() {
		return serviceType;
	}

	public String getInstruments() {
		return instruments;
	}

	public String getAmountWithNDS() {
		return amountWithNDS;
	}

	public LocalDate getInvoiceDate() {
		return invoiceDate;
	}

	public LocalDate getDeliveryToAccountingDate() {
		return deliveryToAccountingDate;
	}

	public InspectionOrganization getInspectionOrganization() {
		return inspectionOrganization;
	}

	public String getNotes() {
		return notes;
	}

	public String getAccountFile() {
		return accountFile;
	}

	public String getStatus() {
		return status;
	}

	public TableType getTableType() {
		return tableType;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static Builder builder() {
		return new Builder();
	}

	@Override
	public String toString() {
		return "Account{" +
				"id=" + id +
				", accountNumber='" + accountNumber + '\'' +
				", accountDate=" + accountDate +
				", company='" + company + '\'' +
				", serviceType='" + serviceType + '\'' +
				", instruments='" + instruments + '\'' +
				", amount='" + amount + '\'' +
				", amountWithNDS='" + amountWithNDS + '\'' +
				", invoiceNumber='" + invoiceNumber + '\'' +
				", invoiceDate=" + invoiceDate +
				", deliveryToAccountingDate=" + deliveryToAccountingDate +
				", inspectionOrganization='" + inspectionOrganization + '\'' +
				", notes='" + notes + '\'' +
				", accountFile='" + accountFile + '\'' +
				", status='" + status + '\'' +
				", tableType='" + tableType + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o){
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Account account = (Account) o;
		return Objects.equals(id, account.id) &&
				Objects.equals(accountNumber, account.accountNumber) &&
				Objects.equals(accountDate, account.accountDate) &&
				Objects.equals(company, account.company) &&
				Objects.equals(serviceType, account.serviceType) &&
				Objects.equals(instruments, account.instruments) &&
				Objects.equals(amount, account.amount) &&
				Objects.equals(amountWithNDS, account.amountWithNDS) &&
				Objects.equals(invoiceNumber, account.invoiceNumber) &&
				Objects.equals(invoiceDate, account.invoiceDate) &&
				Objects.equals(deliveryToAccountingDate, account.deliveryToAccountingDate) &&
				Objects.equals(inspectionOrganization, account.inspectionOrganization) &&
				Objects.equals(notes, account.notes) &&
				Objects.equals(accountFile, account.accountFile) &&
				Objects.equals(tableType, account.tableType) &&
				status == account.status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, accountNumber, accountDate, company, serviceType, instruments, amount,
				amountWithNDS, invoiceNumber, invoiceDate, deliveryToAccountingDate, inspectionOrganization,
				notes, accountFile, status, tableType);
	}

	public static class Builder {
		private Long id;
		private String accountNumber;
		private LocalDate accountDate;
		private Company company;
		private String serviceType;
		private String instruments;
		private String amount;
		private String amountWithNDS;
		private String invoiceNumber;
		private LocalDate invoiceDate;
		private LocalDate deliveryToAccountingDate;
		private InspectionOrganization inspectionOrganization;
		private String notes;
		private String accountFile;
		private String status;
		private TableType tableType;

		private Builder() {
		}

		public Account build() {
			return new Account(this);
		}

		public Builder withId(Long id) {
			this.id = id;
			return this;
		}

		public Builder withAccountNumber(String accountNumber) {
			this.accountNumber = accountNumber;
			return this;
		}

		public Builder withAccountDate(LocalDate accountDate) {
			this.accountDate = accountDate;
			return this;
		}

		public Builder withServiceType(String serviceType) {
			this.serviceType = serviceType;
			return this;
		}
		public Builder withCompany(Company company) {
			this.company = company;
			return this;
		}

		public Builder withInstruments(String instruments) {
			this.instruments = instruments;
			return this;
		}

		public Builder withAmount(String amount) {
			this.amount = amount;
			return this;
		}

		public Builder withAmountWithDNS(String amountWithNDS) {
			this.amountWithNDS = amountWithNDS;
			return this;
		}

		public Builder withInvoiceNumber(String invoiceNumber) {
			this.invoiceNumber = invoiceNumber;
			return this;
		}

		public Builder withInvoiceDate(LocalDate invoiceDate) {
			this.invoiceDate = invoiceDate;
			return this;
		}

		public Builder withDeliveryToAccountingDate(LocalDate deliveryToAccountingDate) {
			this.deliveryToAccountingDate = deliveryToAccountingDate;
			return this;
		}

		public Builder withInspectionOrganization(InspectionOrganization inspectionOrganization) {
			this.inspectionOrganization = inspectionOrganization;
			return this;
		}

		public Builder withNotes(String notes) {
			this.notes = notes;
			return this;
		}

		public Builder withAccountFile(String accountFile) {
			this.accountFile = accountFile;
			return this;
		}

		public Builder withStatus(String status) {
			this.status = status;
			return this;
		}

		public Builder withTableType(TableType tableType) {
			this.tableType = tableType;
			return this;
		}
	}
}
