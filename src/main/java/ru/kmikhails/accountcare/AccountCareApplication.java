package ru.kmikhails.accountcare;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.kmikhails.accountcare.config.JDBCConfig;

public class AccountCareApplication {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(JDBCConfig.class);

		context.close();
	}
}
