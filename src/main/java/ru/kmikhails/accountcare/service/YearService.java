package ru.kmikhails.accountcare.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.kmikhails.accountcare.entity.YearValues;
import ru.kmikhails.accountcare.exception.DataBaseException;
import ru.kmikhails.accountcare.repository.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class YearService {
    private static final Logger LOG = LogManager.getLogger(YearService.class);
    private static final String GET_YEAR_VALUES_QUERY = "SELECT * FROM years";
    private static final String UPDATE_YEAR_VALUES_QUERY = "UPDATE years SET range = ?, default_value = ?" +
            " WHERE id = ?";

    private final DataSource dataSource;

    public YearService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public YearValues getYearValues() {
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement statement = connection.prepareStatement(GET_YEAR_VALUES_QUERY)) {

            try (final ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToEntity(resultSet);
                }
            }
        } catch (SQLException e) {
            LOG.error(e);
            throw new DataBaseException(e);
        }
        return null;
    }

    public void updateYearValues(YearValues yearValues) {
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement statement = connection.prepareStatement(UPDATE_YEAR_VALUES_QUERY)) {

            statement.setLong(3, 1L);
            insert(statement, yearValues);

            statement.executeUpdate();

        } catch (SQLException e) {
            LOG.error(e);
            throw new DataBaseException(e);
        }
    }

    protected YearValues mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return YearValues.builder()
                .withId(resultSet.getLong("id"))
                .withRange(resultSet.getString("range"))
                .withDefaultValue(resultSet.getString("default_value"))
                .build();
    }

    protected void insert(PreparedStatement statement, YearValues yearValues) throws SQLException {
        statement.setString(1, yearValues.getRange());
        statement.setString(2, yearValues.getDefaultValue());
    }
}
