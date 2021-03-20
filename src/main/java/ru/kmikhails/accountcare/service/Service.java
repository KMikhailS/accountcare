package ru.kmikhails.accountcare.service;

import java.util.List;

public interface Service<E> {

    void save(E entity);

    void deleteById(Long id);

    E findById(Long id);

    List<E> findAll();

    void update(E e);
}
