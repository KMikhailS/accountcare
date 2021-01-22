package ru.kmikhails.accountcare.exception;

public class AccountException extends RuntimeException {

	public AccountException() {
		super();
	}

	public AccountException(String message) {
		super(message);
	}

	public AccountException(Exception cause) {
		super(cause);
	}

	public AccountException(String message, Exception cause) {
		super(message, cause);
	}
}
