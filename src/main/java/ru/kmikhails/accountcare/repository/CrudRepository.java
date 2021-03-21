package ru.kmikhails.accountcare.repository;

import ru.kmikhails.accountcare.entity.Account;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<E> {

	Optional<E> save(E entity);

	void deleteById(Long id);

	Optional<E> findById(Long id);

	List<E> findAll();

	List<E> findAllByTableType(String tableType);

	void update(E e);

	Optional<E> findByName(String name);
}
