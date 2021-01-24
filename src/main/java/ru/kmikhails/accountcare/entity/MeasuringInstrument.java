package ru.kmikhails.accountcare.entity;

import java.util.Objects;

public class MeasuringInstrument {
    private final Long id;
    private final String instrument;

    private MeasuringInstrument(Builder builder) {
        this.id = builder.id;
        this.instrument = builder.instrument;
    }

    public Long getId() {
        return id;
    }

    public String getInstrument() {
        return instrument;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "MeasuringInstrument{" +
                "id=" + id +
                ", instrument='" + instrument + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MeasuringInstrument that = (MeasuringInstrument) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(instrument, that.instrument);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, instrument);
    }

    public static class Builder {
        private Long id;
        private String instrument;

        private Builder() {
        }

        public MeasuringInstrument build() {
            return new MeasuringInstrument(this);
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withInstrument(String instrument) {
            this.instrument = instrument;
            return this;
        }
    }
}
