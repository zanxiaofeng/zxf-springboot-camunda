package zxf.camunda.saga.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, JdbcTemplateAutoConfiguration.class})
public class DataSourceConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.camunda")
    public DataSourceProperties camundaBpmDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("camundaBpmDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.camunda.hikari")
    public HikariDataSource camundaBpmDataSource() {
        return camundaBpmDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean("camundaBpmTransactionManager")
    public PlatformTransactionManager camundaBpmTransactionManager() {
        return new DataSourceTransactionManager(camundaBpmDataSource());
    }
}
