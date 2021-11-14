package ru.kmikhails.accountcare.service.impl;

import ru.kmikhails.accountcare.entity.InspectionOrganization;
import ru.kmikhails.accountcare.repository.CrudRepository;
import ru.kmikhails.accountcare.service.Service;

import java.util.List;

public class InspectionOrganizationService implements Service<InspectionOrganization> {

    private final CrudRepository<InspectionOrganization> inspectionOrganizationRepository;

    public InspectionOrganizationService(CrudRepository<InspectionOrganization> inspectionOrganizationRepository) {
        this.inspectionOrganizationRepository = inspectionOrganizationRepository;
    }

    @Override
    public void save(InspectionOrganization entity) {

    }

    @Override
    public void deleteById(Long id) {

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

    }

    @Override
    public InspectionOrganization findByName(String name) {
        return null;
    }
}
