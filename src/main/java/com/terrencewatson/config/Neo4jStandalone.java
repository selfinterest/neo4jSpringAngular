package com.terrencewatson.config;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.rest.graphdb.RestGraphDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;
import org.springframework.stereotype.Component;

/**
 * Created by twatson on 8/11/14.
 */

@Configuration
@EnableNeo4jRepositories(basePackages = "org.springframework.data.neo4j.repository")
@Import(AppConfig.class)
public class Neo4jStandalone extends Neo4jConfiguration {

    @Bean
    public SpringRestGraphDatabase graphDatabaseService(){
        return new SpringRestGraphDatabase("http://localhost:7474/db/data");
    }
}
