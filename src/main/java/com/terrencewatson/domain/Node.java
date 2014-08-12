package com.terrencewatson.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

import java.io.IOException;

/**
 * Created by twatson on 8/11/14.
 */

@NodeEntity
@Persistent
public class Node {


    @
    private String nodeId;

    private String displayName;

    private String nodeType;

    private String label;

    private int order;


    @GraphId
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    /*public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }*/

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
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

}
