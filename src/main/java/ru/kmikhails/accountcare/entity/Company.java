package ru.kmikhails.accountcare.entity;

import java.util.Objects;

public class Company {
    private final long id;
    private final String company;

    public Company(Builder builder) {
        this.id = builder.id;
        this.company = builder.company;
    }

    public long getId() {
        return id;
    }

    public String getCompany() {
        return company;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Company company1 = (Company) o;
        return id == company1.id &&
                company.equals(company1.company);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, company);
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", company='" + company + '\'' +
                '}';
    }

    public static class Builder {
        private long id;
        private String company;

        private Builder() {
        }

        public Company build() {
            return new Company(this);
        }

        public Builder withId(long id) {
            this.id = id;
            return this;
        }

        public Builder withCompany(String company) {
            this.company = company;
            return this;
        }
    }
}
