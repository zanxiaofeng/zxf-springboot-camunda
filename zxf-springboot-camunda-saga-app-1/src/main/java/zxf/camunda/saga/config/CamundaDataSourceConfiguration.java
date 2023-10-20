package zxf.camunda.saga.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, JdbcTemplateAutoConfiguration.class})
public class CamundaDataSourceConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.camunda")
    public DataSourceProperties camundaDataSourceProperties(){
        return new DataSourceProperties();
    }

    @Bean("camundaBpmDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.camunda.hikari")
    public HikariDataSource camundaBpmDataSource(){
        return camundaDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean("camundaBpmTransactionManager")
    public PlatformTransactionManager camundaBpmTransactionManager() {
        return new DataSourceTransactionManager(camundaBpmDataSource());
    }
}
