package com.terrencewatson.query;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by twatson on 8/7/14.
 */
@Component
public class QueryBody {


    private String query;

    private ExecutionResult result = null;


    public void setQuery(String query){
        this.query = query;
    }

    public String getQuery(){
        return this.query;
    }

    public static QueryBody getQueryBodyFromString(String query){
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            return mapper.readValue(query, QueryBody.class);
        } catch (IOException e) {
            return null;
        }
    }





}
