package com.terrencewatson;


import com.terrencewatson.query.QueryBody;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;

//import org.springframework.data.neo4j.annotation.QueryResult;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
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

    @Autowired
    private PlatformTransactionManager neo4jTransactionManager;

    private Neo4jTemplate neo4jTemplate(){
        return new Neo4jTemplate(graphDatabaseService, neo4jTransactionManager);
    }


    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public String post(@RequestBody String query){
        Neo4jTemplate template = neo4jTemplate();
        Result<Map<String, Object>> result;

        QueryBody queryBody = QueryBody.getQueryBodyFromString(query);
        result = template.query(queryBody.getQuery(), null);

        StringBuilder stringBuilder = new StringBuilder();

        for(Map<String, Object> row : result){
            for(Map.Entry<String, Object> column: row.entrySet()) {
                stringBuilder.append(column.getKey()).append(": ").append(column.getValue()).append("; ");
            }
            stringBuilder.append("\n");

        }
        return stringBuilder.toString();

    }

}
