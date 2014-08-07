package com.terrencewatson.com.terrencewatson.db;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.core.GraphDatabase;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by twatson on 8/7/14.
 */


public class Neo4jUtilsImpl implements Neo4jUtils {



    static Logger log = Logger.getLogger(Neo4jUtilsImpl.class.getName());
    static Neo4jUtilsImpl instance = null;
    static String DB_PATH = getDatabaseDirectory();

    static GraphDatabaseService graphDb;


    private static String getDatabaseDirectory(){
        File file1 = new File(System.getProperty("user.dir"));
        File file2 = new File(file1, "/tmp/db");
        log.info("Opening database at "+file2.getPath());
        return file2.getPath();
    }

    static {
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
        registerShutdownHook(graphDb);
    }

    @Bean
    public static GraphDatabaseService graphDb(){
        return graphDb;
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
                graphDb.shutdown();
            }
        } );
    }


    public GraphDatabaseService getGraphDb(){
        return graphDb;
    }


}
