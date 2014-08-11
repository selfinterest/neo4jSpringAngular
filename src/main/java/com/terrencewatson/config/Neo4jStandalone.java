package com.terrencewatson.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by twatson on 8/11/14.
 */

@Configuration
@EnableNeo4jRepositories(basePackages = "com.terrencewatson.domain")
//@EnableNeo4jRepositories(basePackages = "org.springframework.data.neo4j.repository")
@Import(AppConfig.class)
@ImportResource("classpath:META-INF/spring/spring-data-context.xml")
@EnableTransactionManagement
@Component
public class Neo4jStandalone extends Neo4jConfiguration {

    Neo4jStandalone(){
        setBasePackage("com.terrencewatson.domain");
    }
    //@Autowired
    //TransactionManager transactionManager;

    @Bean
    public SpringRestGraphDatabase graphDatabaseService(){
        return new SpringRestGraphDatabase("http://localhost:7474/db/data");
    }



    /*@Bean
    public TransactionManager transactionManager(){
        return new TransactionManager() {
            @Override
            public int getNumberOfOpenTransactions() {
                return 0;
            }

            @Override
            public int getPeakNumberOfConcurrentTransactions() {
                return 0;
            }

            @Override
            public int getNumberOfOpenedTransactions() {
                return 0;
            }

            @Override
            public long getNumberOfCommittedTransactions() {
                return 0;
            }

            @Override
            public long getNumberOfRolledBackTransactions() {
                return 0;
            }

            @Override
            public long getLastCommittedTxId() {
                return 0;
            }
        }
    }*/


    @Bean
    public Neo4jTemplate neo4jTemplate(SpringRestGraphDatabase graphDatabaseService) throws Exception {
        return new Neo4jTemplate(graphDatabaseService, neo4jTransactionManager());
    }

    @Bean
    public Neo4jTemplate getNeo4jTemplate() throws Exception {
        return new Neo4jTemplate(this.graphDatabaseService(), this.neo4jTransactionManager());
    }



}
