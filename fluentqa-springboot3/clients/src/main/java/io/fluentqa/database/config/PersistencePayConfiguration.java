package io.fluentqa.database.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.EntityManagerFactoryBuilderCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypes;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypesScanner;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
// https://github.com/ToQuery/example-spring-data-jpa-multiple-databases.git
/**
 *
 */
@Configuration
@EnableJpaRepositories(
        basePackages = PersistencePayConfiguration.DAO_PACKAGES,
        entityManagerFactoryRef = "payEntityManagerFactory",
        transactionManagerRef = "payTransactionManager"
)
//Create AS Template
//ADD Transaction Manager to Service
public class PersistencePayConfiguration {

    public static final String DAO_PACKAGES = "multiple.databases.modules.pay.dao";
    public static final String[] ENTITY_PACKAGES = {"multiple.databases.modules.pay.entity"};

    @Autowired
    private JpaProperties properties;

    @Bean
    @ConfigurationProperties(prefix = "spring.multiple-database.pay")
    public DataSource payDataSource() {
        return new HikariDataSource();
    }

    @Bean
    static PersistenceManagedTypes payPersistenceManagedTypes(ResourceLoader resourceLoader) {
        return new PersistenceManagedTypesScanner(resourceLoader).scan(ENTITY_PACKAGES);
    }

    @Bean
    public EntityManagerFactoryBuilder payEntityManagerFactoryBuilder(JpaVendorAdapter jpaVendorAdapter,
                                                                      ObjectProvider<PersistenceUnitManager> persistenceUnitManager,
                                                                      ObjectProvider<EntityManagerFactoryBuilderCustomizer> customizers) {
        EntityManagerFactoryBuilder builder = new EntityManagerFactoryBuilder(jpaVendorAdapter,
          this.properties.getProperties(), persistenceUnitManager.getIfAvailable());
        customizers.orderedStream().forEach((customizer) -> customizer.customize(builder));
        return builder;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean payEntityManagerFactory(@Qualifier("payDataSource") DataSource payDataSource,
                                                                          @Qualifier("payPersistenceManagedTypes") PersistenceManagedTypes payPersistenceManagedTypes,
                                                                          @Qualifier("payEntityManagerFactoryBuilder") EntityManagerFactoryBuilder payEntityManagerFactoryBuilder) {
        return payEntityManagerFactoryBuilder
                .dataSource(payDataSource)
                // .packages(PersistencepayConfiguration.ENTITY_PACKAGES)
                .persistenceUnit("payPersistenceUnit")
                .managedTypes(payPersistenceManagedTypes)
                .build();
    }


    @Bean
    public PlatformTransactionManager payTransactionManager(@Qualifier("payEntityManagerFactory")
                                                                LocalContainerEntityManagerFactoryBean
                                                                  payEntityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(payEntityManagerFactory.getObject());
        return transactionManager;
    }
}
