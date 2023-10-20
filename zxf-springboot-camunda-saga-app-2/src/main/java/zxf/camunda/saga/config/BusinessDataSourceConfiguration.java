package zxf.camunda.saga.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, JdbcTemplateAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class})
public class BusinessDataSourceConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.business")
    public DataSourceProperties businessDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean("businessDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.business.hikari")
    public HikariDataSource businessDataSource() {
        return businessDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Primary
    @Bean("businessTransactionManager")
    public PlatformTransactionManager businessTransactionManager() {
        return new JdbcTransactionManager(businessDataSource());
    }

    @Primary
    @Bean("businessJdbcTemplate")
    public NamedParameterJdbcTemplate businessJdbcTemplate() {
        return new NamedParameterJdbcTemplate(businessDataSource());
    }
}
