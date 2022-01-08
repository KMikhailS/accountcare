package ru.kmikhails.accountcare.entity;

import java.util.Objects;

public class YearValues {
    private Long id;
    private String range;
    private String defaultValue;

    public YearValues(YearValues.Builder builder) {
        this.id = builder.id;
        this.range = builder.range;
        this.defaultValue = builder.defaultValue;
    }

    public Long getId() {
        return id;
    }

    public String getRange() {
        return range;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        YearValues that = (YearValues) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(range, that.range) &&
                Objects.equals(defaultValue, that.defaultValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, range, defaultValue);
    }

    public static YearValues.Builder builder() {
        return new YearValues.Builder();
    }

    public static class Builder {
        private Long id;
        private String range;
        private String defaultValue;

        private Builder() {
        }

        public YearValues build() {
            return new YearValues(this);
        }

        public YearValues.Builder withId(long id) {
            this.id = id;
            return this;
        }

        public YearValues.Builder withRange(String range) {
            this.range = range;
            return this;
        }

        public YearValues.Builder withDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }
    }
}
