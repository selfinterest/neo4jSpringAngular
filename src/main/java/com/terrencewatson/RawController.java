package com.terrencewatson;


import com.terrencewatson.com.terrencewatson.db.QueryBody;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.impl.util.StringLogger;
import org.neo4j.rest.graphdb.query.RestCypherQueryEngine;
import org.neo4j.rest.graphdb.util.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;

//import org.springframework.data.neo4j.annotation.QueryResult;

import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.management.Query;
import java.io.IOException;
import java.util.Map;

/**
 * Created by twatson on 8/7/14.
 */

@Controller
@RequestMapping("/api/raw")
@ComponentScan
public class RawController {

    @Autowired
    private SpringRestGraphDatabase graphDatabaseService;


    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public String post(@RequestBody String query){
        //ExecutionResult result;
        //QueryResult res
        org.neo4j.rest.graphdb.util.QueryResult<Map<String, Object>> result;
        try (Transaction tx = graphDatabaseService.beginTx()){
            RestCypherQueryEngine engine = new RestCypherQueryEngine(graphDatabaseService.getRestAPI());
            //ExecutionEngine engine = new ExecutionEngine( this.graphDatabaseService, StringLogger.SYSTEM );
            QueryBody queryBody = QueryBody.getQueryBodyFromString(query);
            Map<String, Object> params = null;

            result = engine.query(queryBody.getQuery(), params);
            //result = (ExecutionResult) engine.query(queryBody.getQuery(), params);
            //result = engine.execute(queryBody.getQuery());
            //QueryResult<Map<String, Object>>
            tx.success();
            String rows = "";
            for(Map<String, Object> row: result){
                for(Map.Entry<String, Object> column: row.entrySet()) {
                    rows += column.getKey() + ": " + column.getValue() + "; ";
                }
                rows += "\n";
            }
            return rows;
            //return graphDatabaseService.toString();
            //return queryBody.getQuery();

        } catch (Error e) {
            return e.toString();
        }
    }

}
