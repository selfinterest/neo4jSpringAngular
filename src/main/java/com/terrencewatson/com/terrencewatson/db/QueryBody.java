package com.terrencewatson.com.terrencewatson.db;

import org.apache.log4j.Logger;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by twatson on 8/7/14.
 */
@Component
public class QueryBody {

    @Autowired
    private Db db = Db.get();

    private String query;

    private ExecutionResult result = null;

    static Logger log = Logger.getLogger(Neo4jUtilsImpl.class.getName());
    public void setQuery(String query){
        this.query = query;
    }

    public String getQuery(){
        return this.query;
    }

    @Override
    public String toString(){
        if(this.result == null){
            this.execute();
        }

        return this.result.dumpToString();
    }

    public ExecutionResult execute(){
        try (Transaction ignored = db.graphDb().beginTx()){
            log.info(this.query);
            this.result = db.executionEngine().execute(this.query);
            return this.result;
        }
    }
}
