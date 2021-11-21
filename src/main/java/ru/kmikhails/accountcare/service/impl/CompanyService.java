package ru.kmikhails.accountcare.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.kmikhails.accountcare.entity.Company;
import ru.kmikhails.accountcare.exception.AccountException;
import ru.kmikhails.accountcare.repository.CrudRepository;
import ru.kmikhails.accountcare.service.Service;

import java.util.List;

public class CompanyService implements Service<Company> {
    private static final Logger LOG = LogManager.getLogger(CompanyService.class);

    private final CrudRepository<Company> companyRepository;

    public CompanyService(CrudRepository<Company> companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public void save(Company company) {
        companyRepository.save(company);
        LOG.info(String.format("Сохранёно предприятие с именем [%s], id [%d]",
                company.getCompany(), company.getId()));
    }

    @Override
    public void deleteById(Long id) {
        companyRepository.deleteById(id);
        LOG.info(String.format("Удалёно предприятие с id [%d]", id));
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
        companyRepository.update(company);
        LOG.info(String.format("Изменёно предприятие с именем [%s], id [%d]",
                company.getCompany(), company.getId()));
    }

    @Override
    public Company findByName(String name) {
        return companyRepository.findByName(name).orElseThrow(
                () -> new AccountException(String.format("Не могу найти предприятие с именем [%s]", name)));
    }
}
