package cn.tanlw.db.lastInsertId.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * @create 2018/8/27
 */
@Configuration

public class DataSourceConfig {

    private final Environment environment;

    @Autowired
    public DataSourceConfig(Environment environment) {
        this.environment = environment;
    }

    private DataSource getHikariDataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(environment.getProperty("datasource.driverClassName"));
        config.setJdbcUrl(environment.getProperty("datasource.url"));
        config.setUsername(environment.getProperty("datasource.username"));
        config.setPassword(environment.getProperty("datasource.password"));
        config.setMinimumIdle(Integer.parseInt(environment.getProperty("datasource.minimumIdle")));
        config.setMaximumPoolSize(Integer.parseInt(environment.getProperty("datasource.maximumPoolSize")));
        config.setConnectionTimeout(Long.parseLong(environment.getProperty("datasource.connectionTimeout")));
        config.setIdleTimeout(Long.parseLong(environment.getProperty("datasource.idleTimeout")));
        config.setMaxLifetime(Long.parseLong(environment.getProperty("datasource.maxLifetime")));
        config.addDataSourceProperty("cachePrepStmts", environment.getProperty("datasource.cachePrepStmts"));
        config.addDataSourceProperty("prepStmtCacheSize", environment.getProperty("datasource.prepStmtCacheSize"));
        config.addDataSourceProperty("prepStmtCacheSqlLimit", environment.getProperty("datasource.prepStmtCacheSqlLimit"));
        return new HikariDataSource(config);
    }

    private DataSource getSingleDataSource() {
        String url = environment.getProperty("datasource.url");
        String driverClassName = environment.getProperty("datasource.driverClassName");
        DriverManagerDataSource dataSource = new DriverManagerDataSource(url);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUsername(environment.getProperty("datasource.username"));
        dataSource.setPassword(environment.getProperty("datasource.password"));
        return dataSource;
    }

    @Bean(name = "dataSource")
    public DataSource dataSource() {
        String env = environment.getProperty("spring.profiles.active");
        return env != null && env.equals("dev") ? this.getSingleDataSource() : this.getHikariDataSource();
//	return  this.getSingleDataSource();
    }
}
