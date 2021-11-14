package ru.kmikhails.accountcare.repository.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.kmikhails.accountcare.entity.Company;
import ru.kmikhails.accountcare.exception.DataBaseException;
import ru.kmikhails.accountcare.repository.AbstractCrudRepository;
import ru.kmikhails.accountcare.repository.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CompanyRepository extends AbstractCrudRepository<Company> {
    private static final Logger LOG = LogManager.getLogger(CompanyRepository.class);
    private static final String ADD_QUERY = "INSERT INTO companies (company) VALUES (?)";
    private static final String FIND_ALL_QUERY = "SELECT * FROM companies";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM companies WHERE company_id = ?";
    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM companies WHERE company = ?";


    public CompanyRepository(DataSource dataSource) {
        super(dataSource, ADD_QUERY, FIND_BY_ID_QUERY, FIND_BY_NAME_QUERY);
    }

    @Override
    public Optional<Company> save(Company company) {
        try(final Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(saveQuery)) {
            try {
                connection.setAutoCommit(false);

                insert(statement, company);
                statement.executeUpdate();

                Optional<Company> resultCompany = findByParam(company.getCompany(), FIND_BY_NAME_QUERY, STRING_PARAM_SETTER);
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
    public List<Company> findAll() {
        return findAll(FIND_ALL_QUERY);
    }

    @Override
    public List<Company> findAllByTableType(String tableType) {
        return null;
    }

    @Override
    public void update(Company company) {

    }

    @Override
    protected Company mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return Company.builder()
                .withId(resultSet.getLong("company_id"))
                .withCompany(resultSet.getString("company"))
                .build();
    }

    @Override
    protected void insert(PreparedStatement statement, Company company) throws SQLException {
        statement.setString(1, company.getCompany());
    }
}
