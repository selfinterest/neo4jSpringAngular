package com.terrencewatson;

import com.terrencewatson.com.terrencewatson.db.Db;
import com.terrencewatson.com.terrencewatson.db.QueryBody;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.management.Query;
import java.io.IOException;

/**
 * Created by twatson on 8/7/14.
 */

@Controller
@RequestMapping("/api/raw")
public class RawController {

    @Autowired
    private GraphDatabaseService graphDatabaseService;


    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public String post(@RequestBody String query){

        ExecutionResult result;
        try (Transaction tx = graphDatabaseService.beginTx()){
            ExecutionEngine engine = new ExecutionEngine( this.graphDatabaseService );
            QueryBody queryBody = QueryBody.getQueryBodyFromString(query);
            result = engine.execute(queryBody.getQuery());
            tx.success();
            return result.dumpToString();
        } catch (Error e) {
            return e.toString();
        }
    }

}
