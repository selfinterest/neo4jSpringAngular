package com.terrencewatson;

import com.terrencewatson.com.terrencewatson.db.Db;
import com.terrencewatson.com.terrencewatson.db.Neo4jUtils;
import com.terrencewatson.com.terrencewatson.db.Neo4jUtilsImpl;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by twatson on 8/6/14.
 */

@Controller
@RequestMapping("/api")
@ComponentScan
public class MainController {

    @Autowired
    private Db db = Db.get();


    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String get(){
        String rows = "";
        int numberOfRows = 0;
        try (Transaction tx = db.graphDb().beginTx()) {
            //Transaction tx = db.graphDb().beginTx();
            tx.success();
            ExecutionResult result = db.executionEngine().execute( "start n=node(*) where n.name = 'my node' return n, n.name" );
            for (Map<String, Object> row: result){
                numberOfRows++;
                for (Map.Entry<String, Object> column : row.entrySet()){
                    rows += column.getKey() + ": " + column.getValue() + "; ";
                }

                rows += "\n";
            }

        } catch (Error e){
            return e.toString();
        }
        if(numberOfRows > 0){
            return rows;
        } else {
            return "Nothing found!";
        }

    }



}
