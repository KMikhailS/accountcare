package ru.kmikhails.accountcare.repository.impl;

import ru.kmikhails.accountcare.entity.InspectionOrganization;
import ru.kmikhails.accountcare.exception.DataBaseException;
import ru.kmikhails.accountcare.repository.AbstractCrudRepository;
import ru.kmikhails.accountcare.repository.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class InspectionOrganizationRepository extends AbstractCrudRepository<InspectionOrganization> {

    private static final String ADD_QUERY = "INSERT INTO inspection_organizations (inspection_organization) VALUES (?)";
    private static final String FIND_ALL_QUERY = "SELECT * FROM inspection_organizations";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM inspection_organizations WHERE inspection_organization_id = ?";
    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM inspection_organizations WHERE inspection_organization = ?";


    public InspectionOrganizationRepository(DataSource dataSource) {
        super(dataSource, ADD_QUERY, FIND_BY_ID_QUERY);
    }

    @Override
    public Optional<InspectionOrganization> save(InspectionOrganization inspectionOrganization) {
        try(final Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(saveQuery)) {
            try {
                connection.setAutoCommit(false);

                insert(statement, inspectionOrganization);
                statement.executeUpdate();

                Optional<InspectionOrganization> resultCompany =
                        findByParam(inspectionOrganization.getInspectionOrganization(), FIND_BY_NAME_QUERY, STRING_PARAM_SETTER);
                connection.commit();

                return resultCompany;
            } catch (SQLException exception) {
                connection.rollback();
            }
        } catch (SQLException e) {
//            LOG.error(e);
            throw new DataBaseException(e);
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public List<InspectionOrganization> findAll() {
        return findAll(FIND_ALL_QUERY);
    }

    @Override
    public List<InspectionOrganization> findAllByTableType(String tableType) {
        return null;
    }

    @Override
    public void update(InspectionOrganization inspectionOrganization) {

    }

    @Override
    protected InspectionOrganization mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return InspectionOrganization.builder()
                .withId(resultSet.getLong("inspection_organization_id"))
                .withInspectionOrganization(resultSet.getString("inspection_organization"))
                .build();
    }

    @Override
    protected void insert(PreparedStatement statement, InspectionOrganization inspectionOrganization) throws SQLException {
        statement.setString(1, inspectionOrganization.getInspectionOrganization());
    }
}
