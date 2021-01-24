package ru.kmikhails.accountcare.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Account {
	private final Long id;
	private final String accountNumber;
	private final LocalDate accountDate;
	private final String company;
	private final String serviceType;
	private final List<MeasuringInstrument> instruments;
	private final Float amount;
	private final Float amountWithNDS;
	private final String invoiceNumber;
	private final LocalDate invoiceDate;
	private final LocalDate deliveryToAccountingDate;
	private final String inspectionOrganization;
	private final String notes;
	private final String accountFile;
	private AccountStatus status;

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

	public String getCompany() {
		return company;
	}

	public float getAmount() {
		return amount;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public LocalDate getInvoiceDate() {
		return invoiceDate;
	}

	public LocalDate getDeliveryToAccountingDate() {
		return deliveryToAccountingDate;
	}

	public String getInspectionOrganization() {
		return inspectionOrganization;
	}

	public String getNotes() {
		return notes;
	}

	public String getAccountFile() {
		return accountFile;
	}

	public AccountStatus getStatus() {
		return status;
	}

	public void setStatus(AccountStatus status) {
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
				", instruments=" + instruments +
				", amount=" + amount +
				", amountWithNDS=" + amountWithNDS +
				", invoiceNumber='" + invoiceNumber + '\'' +
				", invoiceDate=" + invoiceDate +
				", deliveryToAccountingDate=" + deliveryToAccountingDate +
				", inspectionOrganization='" + inspectionOrganization + '\'' +
				", notes='" + notes + '\'' +
				", accountFile='" + accountFile + '\'' +
				", status=" + status +
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
				status == account.status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, accountNumber, accountDate, company, serviceType, instruments, amount,
				amountWithNDS, invoiceNumber, invoiceDate, deliveryToAccountingDate, inspectionOrganization,
				notes, accountFile, status);
	}

	public static class Builder {
		private Long id;
		private String accountNumber;
		private LocalDate accountDate;
		private String company;
		private String serviceType;
		private List<MeasuringInstrument> instruments;
		private Float amount;
		private Float amountWithNDS;
		private String invoiceNumber;
		private LocalDate invoiceDate;
		private LocalDate deliveryToAccountingDate;
		private String inspectionOrganization;
		private String notes;
		private String accountFile;

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
		public Builder withCompany(String company) {
			this.company = company;
			return this;
		}

		public Builder withInstruments(List<MeasuringInstrument> instruments) {
			this.instruments = instruments;
			return this;
		}

		public Builder withAmount(Float amount) {
			this.amount = amount;
			return this;
		}

		public Builder withAmountWithDNS(Float amountWithNDS) {
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

		public Builder withInspectionOrganization(String inspectionOrganization) {
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
	}
}
