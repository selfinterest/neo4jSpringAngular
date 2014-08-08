package com.terrencewatson;

import com.terrencewatson.com.terrencewatson.db.Db;
import com.terrencewatson.com.terrencewatson.db.QueryBody;
import org.neo4j.cypher.javacompat.ExecutionResult;
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
    private Db db = Db.get();

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public String post(@RequestBody String query){
        /*try {
            QueryBody queryBody = db.handleQueryBody(query);
            return queryBody.execute().dumpToString();

            //return queryBody.toString();
        } catch (IOException e) {
            return e.toString();
        }*/
        ExecutionResult result;
        try (Transaction tx = db.graphDb().beginTx()){
            QueryBody queryBody = db.handleQueryBody(query);
            result = db.executionEngine().execute(queryBody.getQuery());
            tx.success();
            return result.dumpToString();
        } catch (Error e){
            return e.toString();
        } catch (IOException e) {
            return e.toString();
        }
    }

}
