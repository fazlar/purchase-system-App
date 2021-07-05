package com.purchaseSystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class AppConfig implements WebMvcConfigurer {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private Environment env;


	@Bean
	@Primary
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource);
		em.setPackagesToScan("com.purchaseSystem");
		final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		em.setJpaProperties(additionalProperties());
		return em;
	}

	protected Properties additionalProperties() {

		final Properties hibernateProperties = new Properties();
		hibernateProperties.setProperty("hibernate.hbm2ddl.auto",env.getProperty("hibernate.hbm2ddl.auto"));
        hibernateProperties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
        hibernateProperties.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        hibernateProperties.setProperty("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
        hibernateProperties.setProperty("hibernate.proc.param_null_passing", env.getProperty("hibernate.proc.param_null_passing"));
        hibernateProperties.put("hibernate.temp.use_jdbc_metadata_defaults", env.getProperty("hibernate.temp.use_jdbc_metadata_defaults"));
        hibernateProperties.put("hibernate.schema_update.unique_constraint_strategy", env.getProperty("hibernate.schema_update.unique_constraint_strategy"));
        hibernateProperties.put("hibernate.temp.use_jdbc_metadata_defaults", env.getProperty("hibernate.temp.use_jdbc_metadata_defaults"));
		return hibernateProperties;
	}

}
