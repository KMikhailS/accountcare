package ru.kmikhails.accountcare.repository.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.kmikhails.accountcare.entity.TableType;
import ru.kmikhails.accountcare.exception.DataBaseException;
import ru.kmikhails.accountcare.repository.AbstractCrudRepository;
import ru.kmikhails.accountcare.repository.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TableTypeRepository extends AbstractCrudRepository<TableType> {
    private static final Logger LOG = LogManager.getLogger(TableTypeRepository.class);
    private static final String ADD_QUERY = "INSERT INTO table_types (table_type) VALUES (?)";
    private static final String FIND_ALL_QUERY = "SELECT * FROM table_types";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM table_types WHERE table_type_id = ?";
    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM table_types WHERE table_type = ?";


    public TableTypeRepository(DataSource dataSource) {
        super(dataSource, ADD_QUERY, FIND_BY_ID_QUERY, FIND_BY_NAME_QUERY);
    }

    @Override
    public Optional<TableType> save(TableType tableType) {
        try(final Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(saveQuery)) {
            try {
                connection.setAutoCommit(false);

                insert(statement, tableType);
                statement.executeUpdate();

                Optional<TableType> resultCompany =
                        findByParam(tableType.getTableType(), FIND_BY_NAME_QUERY, STRING_PARAM_SETTER);
                connection.commit();

                return resultCompany;
            } catch (SQLException exception) {
                connection.rollback();
            }
        } catch (SQLException e) {
            LOG.error(e);
            throw new DataBaseException(e);
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public List<TableType> findAll() {
        return findAll(FIND_ALL_QUERY);
    }

    @Override
    public List<TableType> findAllByTableType(String tableType, String year) {
        return null;
    }

    @Override
    public void update(TableType tableType) {

    }

    @Override
    protected TableType mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return TableType.builder()
                .withId(resultSet.getLong("table_type_id"))
                .withTableType(resultSet.getString("table_type"))
                .build();
    }

    @Override
    protected void insert(PreparedStatement statement, TableType tableType) throws SQLException {
        statement.setString(1, tableType.getTableType());
    }
}
