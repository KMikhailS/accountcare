package ru.kmikhails.accountcare.entity;

import java.util.Objects;

public class InspectionOrganization implements Comparable<InspectionOrganization> {
    private final long id;
    private final String inspectionOrganization;

    public long getId() {
        return id;
    }

    public String getInspectionOrganization() {
        return inspectionOrganization;
    }

    public InspectionOrganization(Builder builder) {
        this.id = builder.id;
        this.inspectionOrganization = builder.inspectionOrganization;
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
        InspectionOrganization that = (InspectionOrganization) o;
        return id == that.id &&
                inspectionOrganization.equals(that.inspectionOrganization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, inspectionOrganization);
    }

    @Override
    public String toString() {
        return inspectionOrganization;
    }

    @Override
    public int compareTo(InspectionOrganization inspectionOrganization) {
        return this.inspectionOrganization.compareTo(inspectionOrganization.getInspectionOrganization());
    }

    public static class Builder {
        private long id;
        private String inspectionOrganization;

        private Builder() {
        }

        public InspectionOrganization build() {
            return new InspectionOrganization(this);
        }

        public Builder withId(long id) {
            this.id = id;
            return this;
        }

        public Builder withInspectionOrganization(String inspectionOrganization) {
            this.inspectionOrganization = inspectionOrganization;
            return this;
        }
    }
}
