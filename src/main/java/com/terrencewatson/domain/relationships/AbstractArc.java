package com.terrencewatson.domain.relationships;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.terrencewatson.domain.Node;
import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;
import us.monoid.json.JSONObject;

import java.io.IOException;
import java.util.Map;

/**
 * Created by twatson on 8/14/14.
 */

@RelationshipEntity
public abstract class AbstractArc {

    private final Class<? extends AbstractArc> theClass;
    String displayName;
    String weight;

    @GraphId
    Long id;

    public AbstractArc(){
        this.theClass = this.getClass();
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @StartNode private Node objectID1;
    @EndNode private Node objectID2;

    public static Class generateRelationshipClassFromString(String relationshipType){
        String className = relationshipType.toLowerCase();
        className = className.substring(0, 1).toUpperCase() + className.substring(1);
        className = "com.terrencewatson.domain.relationships." + className;
        try {
            Class<AbstractArc> RelationshipClass = (Class<AbstractArc>) Class.forName(className);
            return RelationshipClass;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static AbstractArc getArcFromJsonString(String s, String relationshipType) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        Class<AbstractArc> relationshipClass = AbstractArc.generateRelationshipClassFromString(relationshipType);
        AbstractArc arc = mapper.readValue(s, relationshipClass);
        return arc;
    }
}
