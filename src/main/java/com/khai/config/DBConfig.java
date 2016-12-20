package com.khai.config;

import org.hibernate.ejb.HibernatePersistence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;

import static com.khai.config.constants.Constants.PROP_DATABASE_DRIVER;
import static com.khai.config.constants.Constants.PROP_DATABASE_PASSWORD;
import static com.khai.config.constants.Constants.PROP_DATABASE_URL;
import static com.khai.config.constants.Constants.PROP_DATABASE_USERNAME;
import static com.khai.config.constants.Constants.PROP_ENTITYMANAGER_PACKAGES_TO_SCAN;
import static com.khai.config.constants.Constants.PROP_HIBERNATE_DIALECT;
import static com.khai.config.constants.Constants.PROP_HIBERNATE_HBM2DDL_AUTO;
import static com.khai.config.constants.Constants.PROP_HIBERNATE_SHOW_SQL;

/**
 * Spring beans containing app configuration.
 */
@Configuration
@EnableTransactionManagement
@ComponentScan("com.khai.db")
@PropertySource("classpath:application.properties")
@EnableJpaRepositories("com.khai.db.repository")
public class DBConfig {

    @Resource
    private Environment env;

    /**
     * Data source bean. Contains DB configuration.
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getRequiredProperty(PROP_DATABASE_DRIVER));
        dataSource.setUrl(env.getRequiredProperty(PROP_DATABASE_URL));
        dataSource.setUsername(env.getRequiredProperty(PROP_DATABASE_USERNAME));
        dataSource.setPassword(env.getRequiredProperty(PROP_DATABASE_PASSWORD));
        return dataSource;
    }

    /**
     * Entity manager factory bean. Contains entity manager factory configuration.
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistence.class);
        entityManagerFactoryBean.setPackagesToScan(env.getRequiredProperty(PROP_ENTITYMANAGER_PACKAGES_TO_SCAN));
        entityManagerFactoryBean.setJpaProperties(getHibernateProperties());
        return entityManagerFactoryBean;
    }

    /**
     * Transaction manager bean. Contains transaction manager configuration.
     */
    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.put(PROP_HIBERNATE_DIALECT,
                env.getRequiredProperty(PROP_HIBERNATE_DIALECT));
        properties.put(PROP_HIBERNATE_SHOW_SQL,
                env.getRequiredProperty(PROP_HIBERNATE_SHOW_SQL));
        properties.put(PROP_HIBERNATE_HBM2DDL_AUTO,
                env.getRequiredProperty(PROP_HIBERNATE_HBM2DDL_AUTO));
        return properties;
    }

}