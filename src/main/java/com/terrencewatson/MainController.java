package com.terrencewatson;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by twatson on 8/6/14.
 */

@Controller
@RequestMapping("/api")
@ComponentScan
public class MainController {




    /*@RequestMapping(method = RequestMethod.GET)
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

    }*/



}
