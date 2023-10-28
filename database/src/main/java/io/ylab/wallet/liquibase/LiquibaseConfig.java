package io.ylab.wallet.liquibase;

import liquibase.integration.spring.SpringLiquibase;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class LiquibaseConfig {

    @Value("${liquibase.schema}")
    private String liquibaseSchema;
    @Value("${liquibase.changelog}")
    private String changelog;

    @Bean
    public DataSource dataSource(@Value("${db.url}") String url,
                                @Value("${db.username}") String username,
                                @Value("${db.password}") String password) {
        var dataSource = new PGSimpleDataSource();
        dataSource.setUrl(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        var liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setLiquibaseSchema(liquibaseSchema);
        liquibase.setChangeLog(changelog);
        return liquibase;
    }
}
