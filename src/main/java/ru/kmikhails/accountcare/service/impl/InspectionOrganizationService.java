package ru.kmikhails.accountcare.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.kmikhails.accountcare.entity.InspectionOrganization;
import ru.kmikhails.accountcare.exception.AccountException;
import ru.kmikhails.accountcare.repository.CrudRepository;
import ru.kmikhails.accountcare.service.Service;

import java.util.List;

public class InspectionOrganizationService implements Service<InspectionOrganization> {
    private static final Logger LOG = LogManager.getLogger(InspectionOrganizationService.class);

    private final CrudRepository<InspectionOrganization> inspectionOrganizationRepository;

    public InspectionOrganizationService(CrudRepository<InspectionOrganization> inspectionOrganizationRepository) {
        this.inspectionOrganizationRepository = inspectionOrganizationRepository;
    }

    @Override
    public void save(InspectionOrganization inspectionOrganization) {
        inspectionOrganizationRepository.save(inspectionOrganization);
        LOG.info(String.format("Сохранёна поверяющая организация с именем [%s], id [%d]",
                inspectionOrganization.getInspectionOrganization(), inspectionOrganization.getId()));
    }

    @Override
    public void deleteById(Long id) {
        inspectionOrganizationRepository.deleteById(id);
        LOG.info(String.format("Удалёна поверяющая организация с id [%d]", id));
    }

    @Override
    public InspectionOrganization findById(Long id) {
        return null;
    }

    @Override
    public List<InspectionOrganization> findAll() {
        return inspectionOrganizationRepository.findAll();
    }

    @Override
    public void update(InspectionOrganization inspectionOrganization) {
        inspectionOrganizationRepository.update(inspectionOrganization);
        LOG.info(String.format("Изменёна поверяющая организация с именем [%s], id [%d]",
                inspectionOrganization.getInspectionOrganization(), inspectionOrganization.getId()));
    }

    @Override
    public InspectionOrganization findByName(String name) {
        return inspectionOrganizationRepository.findByName(name).orElseThrow(
                () -> new AccountException(String.format("Не могу найти поверяющую организацию с именем [%s]", name)));
    }
}
