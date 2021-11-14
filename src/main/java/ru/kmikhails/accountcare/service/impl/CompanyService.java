package ru.kmikhails.accountcare.service.impl;

import ru.kmikhails.accountcare.entity.Company;
import ru.kmikhails.accountcare.repository.CrudRepository;
import ru.kmikhails.accountcare.service.Service;

import java.util.List;

public class CompanyService implements Service<Company> {

    private final CrudRepository<Company> companyRepository;

    public CompanyService(CrudRepository<Company> companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public void save(Company entity) {

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Company findById(Long id) {
        return null;
    }

    @Override
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @Override
    public void update(Company company) {

    }

    @Override
    public Company findByName(String name) {
        return null;
    }
}
