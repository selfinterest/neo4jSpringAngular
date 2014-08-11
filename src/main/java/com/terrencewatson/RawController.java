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
import org.neo4j.rest.graphdb.util.QueryResult;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @Transactional
    public String post(@RequestBody String query){

        QueryResult<Map<String, Object>> result;
        RestCypherQueryEngine engine = new RestCypherQueryEngine(graphDatabaseService.getRestAPI());  //As per @link http://stackoverflow.com/questions/21132234/null-exception-error-when-executing-cypher-query-from-java-code-neo4j
        QueryBody queryBody = QueryBody.getQueryBodyFromString(query);
        Map<String, Object> params = null;

        result = engine.query(queryBody.getQuery(), params);

        String rows = "";
        for(Map<String, Object> row: result){
            for(Map.Entry<String, Object> column: row.entrySet()) {
                rows += column.getKey() + ": " + column.getValue() + "; ";
            }
            rows += "\n";
        }
        return rows;


    }

}
