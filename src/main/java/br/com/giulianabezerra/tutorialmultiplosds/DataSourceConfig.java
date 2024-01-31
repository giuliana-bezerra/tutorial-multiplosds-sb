package br.com.giulianabezerra.tutorialmultiplosds;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.init.DataSourceScriptDatabaseInitializer;
import org.springframework.boot.sql.init.DatabaseInitializationMode;
import org.springframework.boot.sql.init.DatabaseInitializationSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.simple.JdbcClient;

@Configuration
public class DataSourceConfig {

  @Primary
  @ConfigurationProperties(prefix = "app.datasource.posts")
  @Bean
  DataSourceProperties postsDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Primary
  @Bean
  DataSource postsDataSource(DataSourceProperties properties) {
    return properties.initializeDataSourceBuilder().build();
  }

  @Primary
  @Bean
  JdbcClient postsJdbcClient(DataSource dataSource) {
    return JdbcClient.create(dataSource);
  }

  @Bean
  DataSourceScriptDatabaseInitializer postsDataSourceScriptDatabaseInitializer(
      @Qualifier("postsDataSource") DataSource dataSource) {
    var settings = new DatabaseInitializationSettings();
    settings.setSchemaLocations(List.of("classpath:posts-schema.sql"));
    settings.setMode(DatabaseInitializationMode.ALWAYS);
    return new DataSourceScriptDatabaseInitializer(dataSource, settings);
  }

  @ConfigurationProperties(prefix = "app.datasource.comments")
  @Bean
  DataSourceProperties commentsDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  DataSource commentsDataSource(
      @Qualifier("commentsDataSourceProperties") DataSourceProperties properties) {
    return properties.initializeDataSourceBuilder().build();
  }

  @Bean
  JdbcClient commentsJdbcClient(
      @Qualifier("commentsDataSource") DataSource dataSource) {
    return JdbcClient.create(dataSource);
  }

  @Bean
  DataSourceScriptDatabaseInitializer commentsDataSourceScriptDatabaseInitializer(
      @Qualifier("commentsDataSource") DataSource dataSource) {
    var settings = new DatabaseInitializationSettings();
    settings.setSchemaLocations(List.of("classpath:comments-schema.sql"));
    settings.setMode(DatabaseInitializationMode.ALWAYS);
    return new DataSourceScriptDatabaseInitializer(dataSource, settings);
  }
}
