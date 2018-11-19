package com.ecoservices.app.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

@Configuration
@PropertySource("classpath:application.${spring.profiles.active}.properties")
public class MongoDataSourceConfig {

    @Value("${spring.data.mongodb.host}")
    public String host;

    @Value("${spring.data.mongodb.port}")
    public String port;

    @Value("${spring.data.mongodb.username}")
    public String username;

    @Value("${spring.data.mongodb.password}")
    public String password;

    @Value("${spring.data.mongodb.database}")
    public String database;

    @Bean
    public MongoDbFactory mongoDbFactory(){
        MongoCredential credential = MongoCredential.createCredential(username, getDatabaseName(), password.toCharArray());
        ServerAddress serverAddress = new ServerAddress(host, Integer.parseInt(port));
        MongoClient mongoClient = new MongoClient(serverAddress,Arrays.asList(credential));
        return new SimpleMongoDbFactory(mongoClient,getDatabaseName());
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception { return new MongoTemplate(mongoDbFactory()); }

    protected String getDatabaseName() {
        return database;
    }
}
