package com.purchaseSystem.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

@Configuration
public class DBConfiguration {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private Environment env;

	@Profile("dev")
	@Bean
	public DataSource verifincationDevDataSource() {
		LOGGER.info("DB connection for Development");
		return createDataSource();
	}

	private DataSource createDataSource() {

		System.out.println("================== Data Base Config ====================");
		System.out.println(env.getProperty("spring.datasource.username"));
		System.out.println(env.getProperty("spring.datasource.password"));
		System.out.println(env.getProperty("spring.datasource.url"));
		System.out.println(env.getProperty("spring.datasource.driver-class-name"));

		return DataSourceBuilder.create()
				.username(env.getProperty("spring.datasource.username"))
				.password(env.getProperty("spring.datasource.password"))
				.url(env.getProperty("spring.datasource.url"))
				.driverClassName(env.getProperty("spring.datasource.driver-class-name"))
				.build();
	}

}
