package ru.kmikhails.accountcare.service.impl;

import ru.kmikhails.accountcare.entity.TableType;
import ru.kmikhails.accountcare.exception.AccountException;
import ru.kmikhails.accountcare.repository.CrudRepository;
import ru.kmikhails.accountcare.service.Service;

import java.util.List;

public class TableTypeService implements Service<TableType> {

    private final CrudRepository<TableType> tableTypeRepository;

    public TableTypeService(CrudRepository<TableType> tableTypeRepository) {
        this.tableTypeRepository = tableTypeRepository;
    }

    @Override
    public void save(TableType entity) {

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public TableType findById(Long id) {
        return null;
    }

    @Override
    public List<TableType> findAll() {
        return tableTypeRepository.findAll();
    }

    @Override
    public void update(TableType tableType) {

    }

    @Override
    public TableType findByName(String name) {
        return tableTypeRepository.findByName(name).orElseThrow(
                () -> new AccountException(String.format("Не могу найти тип таблицы с именем [%s]", name)));
    }
}
