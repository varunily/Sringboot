package io.endeavour.stocks.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

/**
 * Class not neecssary today, just written to highlight how things work behind the scenes
 */
@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class DatabaseConfig {

    @Autowired
    DataSource dataSource;

    /**
     * This method will be called at Application Startup and is used to create
     * a JDBC template object from the datasource, that has been defined by
     * the parameters set as spring.datasource in the application.properties file
     * @return
     */
    @Bean(value = "jdbcTemplate")// Singleton
    public JdbcTemplate getJdBCTemplate(){
        return new JdbcTemplate(dataSource);
    }


    @Bean(value = "namedParameterJdbcTemplate")
    public NamedParameterJdbcTemplate getNamedParamJDBCTempalte(){
        return new NamedParameterJdbcTemplate(dataSource);
    }
}

