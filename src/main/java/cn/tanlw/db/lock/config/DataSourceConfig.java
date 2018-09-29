package cn.tanlw.db.lock.config;

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
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(environment.getProperty("datasource.driverClassName"));
        hikariConfig.setJdbcUrl(environment.getProperty("datasource.url"));
        hikariConfig.setUsername(environment.getProperty("datasource.username"));
        hikariConfig.setPassword(environment.getProperty("datasource.password"));
        hikariConfig.setMinimumIdle(Integer.parseInt(environment.getProperty("datasource.minimumIdle")));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(environment.getProperty("datasource.maximumPoolSize")));
        hikariConfig.setConnectionTimeout(Long.parseLong(environment.getProperty("datasource.connectionTimeout")));
        hikariConfig.setIdleTimeout(Long.parseLong(environment.getProperty("datasource.idleTimeout")));
        hikariConfig.setMaxLifetime(Long.parseLong(environment.getProperty("datasource.maxLifetime")));
        hikariConfig.addDataSourceProperty("cachePrepStmts", environment.getProperty("datasource.cachePrepStmts"));
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", environment.getProperty("datasource.prepStmtCacheSize"));
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", environment.getProperty("datasource.prepStmtCacheSqlLimit"));
        hikariConfig.setAutoCommit(false);
        return new HikariDataSource(hikariConfig);
    }

    private DataSource getSingleDataSource() {
        String dataSourceUrl = environment.getProperty("datasource.url");
        String driverClassName = environment.getProperty("datasource.driverClassName");
        DriverManagerDataSource dataSource = new DriverManagerDataSource(dataSourceUrl);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUsername(environment.getProperty("datasource.username"));
        dataSource.setPassword(environment.getProperty("datasource.password"));
        return dataSource;
    }

    @Bean(name = "dataSource")
    public DataSource dataSource() {
        String env = environment.getProperty("spring.profiles.active");
        return env != null && env.equals("dev") ? this.getSingleDataSource() : this.getHikariDataSource();
    }
}
