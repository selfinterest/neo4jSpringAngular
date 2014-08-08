package com.terrencewatson.com.terrencewatson.db;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.apache.log4j.Logger;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

/**
 * Created by twatson on 8/7/14.
 */
@Service
public class Db implements InitializingBean {

    @Autowired
    private GraphDatabaseService graphDatabaseService;

    private static Db instance;
    private GraphDatabaseService graphDb;
    private ExecutionEngine engine;
    static Logger log = Logger.getLogger(Neo4jUtilsImpl.class.getName());

    private static String getDatabaseDirectory(){
        File file1 = new File(System.getProperty("user.dir"));
        File file2 = new File(file1, "/tmp/db");
        System.out.println(file2.getPath());
        //log.info("Opening database at "+file2.getPath());
        return file2.getPath();
    }

    //TODO: eliminate this, rely only on Spring to instantiate graph database
    public Db(){
        this.graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(getDatabaseDirectory());
        this.engine = new ExecutionEngine( this.graphDb );
        registerShutdownHook(this.graphDb);
    }



    public GraphDatabaseService graphDb(){
        return this.graphDb;
    }

    public ExecutionEngine executionEngine() { return this.engine; }

    public QueryBody handleQueryBody(String query) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            QueryBody queryBody = mapper.readValue(query, QueryBody.class);
            return queryBody;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        instance = this;
    }

    private static void registerShutdownHook( final GraphDatabaseService graphDb )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                Db.get().graphDb().shutdown();
            }
        } );
    }

    public static Db get(){
        return instance;
    }
}
