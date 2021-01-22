package ru.kmikhails.accountcare.validator;

public interface Validator<E> {

	void validate(E entity);
}
