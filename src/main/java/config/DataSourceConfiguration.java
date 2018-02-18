package config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "mpi.datasource")
    public DataSource getMpiDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public NamedParameterJdbcTemplate getGitLeanNamedParameterJdbcTemplate(DataSource mpiDatasource) {
        return new NamedParameterJdbcTemplate(mpiDatasource);
    }

    @Bean
    public JdbcTemplate getGitLearnJdbcTemplate(DataSource mpiDatasource) {
        return new JdbcTemplate(mpiDatasource);
    }
}