package ru.kmikhails.accountcare.entity;

import java.util.Objects;

public class TableType {
    private final long id;
    private final String tableType;

    public TableType(Builder builder) {
        this.id = builder.id;
        this.tableType = builder.tableType;
    }

    public long getId() {
        return id;
    }

    public String getTableType() {
        return tableType;
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
        TableType tableType1 = (TableType) o;
        return id == tableType1.id &&
                tableType.equals(tableType1.tableType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableType);
    }

    @Override
    public String toString() {
        return tableType;
    }

    public static class Builder {
        private long id;
        private String tableType;

        private Builder() {
        }

        public TableType build() {
            return new TableType(this);
        }

        public Builder withId(long id) {
            this.id = id;
            return this;
        }

        public Builder withTableType(String tableType) {
            this.tableType = tableType;
            return this;
        }
    }
}
