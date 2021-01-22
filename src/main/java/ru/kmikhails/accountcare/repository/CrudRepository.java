package ru.kmikhails.accountcare.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<E> {

	int save(E entity);

	void deleteById(Long id);

	Optional<E> findById(Long id);

	List<E> findAll();

	void update(Long id);
}
