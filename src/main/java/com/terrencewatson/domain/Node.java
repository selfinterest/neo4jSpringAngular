package com.terrencewatson.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.data.neo4j.template.Neo4jOperations;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by twatson on 8/11/14.
 */


@NodeEntity
public class Node {

    @Indexed(unique=true)
    String objectID;

    String displayName;

    String type;

    String label;

    int order;

    int time;

    String qualifier;

    @GraphId
    Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public String getObjectID() {
        return objectID;
    }

    public Node setObjectID(String objectID ) {
        this.objectID = objectID;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Node setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getType() {
        return type;
    }

    public Node setType(String type) {
        this.type = type;
        return this;
    }



    public String getLabel() {
        return label;
    }

    public Node setLabel(String label) {
        this.label = label;
        return this;
    }

    public int getOrder() {
        return order;
    }

    public Node setOrder(int order) {
        this.order = order;
        return this;
    }

    public int getTime(){
        return time;
    }

    public Node setTime(int time){
        this.time = time;
        return this;
    }

    public String getQualifier() {
        return qualifier;
    }

    public Node setQualifier(String qualifier) {
        this.qualifier = qualifier;
        return this;
    }



    public static Node getNodeFromJsonString(String jsonString){
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            return mapper.readValue(jsonString, Node.class);
        } catch (IOException e) {
            return null;
        }
    }

    public static String getJsonStringFromNode(Node node) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(node);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

    }

    public String toJson(){
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {

            e.printStackTrace();
            return null;
        }
    }


    public Node update(Node newNode) {
        Class nodeClass = this.getClass();
        Method[] allMethods = nodeClass.getDeclaredMethods();
        List<Method> setters = new ArrayList<Method>();
        for(Method method : allMethods){
            if(method.getName().startsWith("set") && !method.getName().equals("setObjectID")) {
                String getterName = method.getName().replace("set", "get");
                try {
                    Method getterMethod = nodeClass.getDeclaredMethod(getterName);
                    method.invoke(this, getterMethod.invoke(newNode));
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }


        return this;
    }
}
