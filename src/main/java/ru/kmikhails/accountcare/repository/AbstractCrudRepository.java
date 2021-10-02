package ru.kmikhails.accountcare.repository;

import ru.kmikhails.accountcare.entity.Account;
import ru.kmikhails.accountcare.exception.DataBaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public abstract class AbstractCrudRepository<E> implements CrudRepository<E> {

    protected static final BiConsumer<PreparedStatement, Long> LONG_PARAM_SETTER = (statement, value) -> {
        try {
            statement.setLong(1, value);
        } catch (SQLException e) {
            e.printStackTrace();
//			LOG.error(e);
        }
    };

    protected static final BiConsumer<PreparedStatement, String> STRING_PARAM_SETTER = (statement, string) -> {
        try {
            statement.setString(1, string);
        } catch (SQLException e) {
            e.printStackTrace();
//			LOG.error(e);
        }
    };

    protected final DataSource dataSource;
    protected final String saveQuery;
    protected final String findByIdQuery;
    protected final String findByNameQuery;

    protected AbstractCrudRepository(DataSource dataSource, String saveQuery, String findByIdQuery, String findByNameQuery) {
        this.dataSource = dataSource;
        this.saveQuery = saveQuery;
        this.findByIdQuery = findByIdQuery;
        this.findByNameQuery = findByNameQuery;
    }

    @Override
    public Optional<E> findById(Long id) {
        return findByParam(id, findByIdQuery, LONG_PARAM_SETTER);
    }

    protected <P> void deleteByParam(P param, String deleteByParam, BiConsumer<PreparedStatement, P> designatedParamSetter) {
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement statement = connection.prepareStatement(deleteByParam)) {

            designatedParamSetter.accept(statement, param);
            if (statement.executeUpdate() == 0) {
                throw new IllegalArgumentException("Entity with id " + param + " does not exist");
            }
        } catch (SQLException e) {
            e.printStackTrace();
//			LOG.error(e);
            throw new DataBaseException(e);
        }
    }

    protected <P> Optional<E> findByParam(P param, String findByParam, BiConsumer<PreparedStatement, P> designatedParamSetter) {
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement statement = connection.prepareStatement(findByParam)) {

            designatedParamSetter.accept(statement, param);

            try (final ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToEntity(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
//			LOG.error(e);
            throw new DataBaseException(e);
        }

        return Optional.empty();
    }

    protected <P> List<E> findAllByStringParam(P param, String findByParam, BiConsumer<PreparedStatement, P> designatedParamSetter) {
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement statement = connection.prepareStatement(findByParam)) {

            designatedParamSetter.accept(statement, param);

            try (final ResultSet resultSet = statement.executeQuery()) {
                List<E> entities = new ArrayList<>();
                while (resultSet.next()) {
                    entities.add(mapResultSetToEntity(resultSet));
                }
                return entities;
            }
        } catch (SQLException e) {
            e.printStackTrace();
//			LOG.error(e);
            throw new DataBaseException(e);
        }
    }

    public List<E> findAll(String findAllQuery) {
        try (final Connection connection = dataSource.getConnection();
             final PreparedStatement statement = connection.prepareStatement(findAllQuery)) {

            try (final ResultSet resultSet = statement.executeQuery()) {
                List<E> entities = new ArrayList<>();
                while (resultSet.next()) {
                    entities.add(mapResultSetToEntity(resultSet));
                }
                return entities;
            }
        } catch (SQLException e) {
            e.printStackTrace();
//			LOG.error(e);
            throw new DataBaseException(e);
        }
    }

    @Override
    public Optional<E> findByName(String name) {
        return findByParam(name, findByNameQuery, STRING_PARAM_SETTER);
    }

    @Override
    public Optional<E> findByAccountNumberAndDate(String number, LocalDate date) {
        return Optional.empty();
    }

    protected abstract E mapResultSetToEntity(ResultSet resultSet) throws SQLException;

    protected abstract void insert(PreparedStatement statement, E entity) throws SQLException;
}
