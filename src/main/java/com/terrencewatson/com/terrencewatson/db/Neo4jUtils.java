package com.terrencewatson.com.terrencewatson.db;

import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.stereotype.Component;

/**
 * Created by twatson on 8/7/14.
 */

public interface Neo4jUtils {
    public GraphDatabaseService getGraphDb();
}
