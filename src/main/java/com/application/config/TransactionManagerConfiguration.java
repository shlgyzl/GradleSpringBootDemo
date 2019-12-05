package com.application.config;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;


@Configuration
@EnableTransactionManagement
public class TransactionManagerConfiguration {

    @Bean(name = "dataSourceTransactionManager")
    public PlatformTransactionManager dataSourceTransactionManager(@Qualifier("druidDataSource") DataSource druidDataSource) {
        return new DataSourceTransactionManager(druidDataSource);
    }

    //@Bean
    public PlatformTransactionManager hibernateTransactionManager(SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
    }

    //@Bean
    public LocalSessionFactoryBean sessionFactory(@Qualifier("druidDataSource") DataSource druidDataSource) {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setConfigLocation(new ClassPathResource("classpath:config/hibernate/hibernate.cfg.xml"));
        sessionFactoryBean.setDataSource(druidDataSource);
        sessionFactoryBean.setPackagesToScan("com.application.domain");
        return sessionFactoryBean;
    }

    @Bean(name = "transactionManager")
    @Primary
    public PlatformTransactionManager jpaTransactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("druidDataSource") DataSource druidDataSource) {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.application.domain");
        factory.setDataSource(druidDataSource);
        return factory;
    }
}
