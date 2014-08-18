package com.terrencewatson.domain.relationships;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.terrencewatson.domain.Node;
import com.terrencewatson.domain.repositories.NodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    /*private final Class<? extends AbstractArc> theClass;*/

    String displayName;
    String weight;

    @GraphId
    Long id;



    public AbstractArc(){
        //this.theClass = this.getClass();
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


    @StartNode private Node object1;
    @EndNode private Node object2;

    /*public Node getObjectID1() {
        return objectID1;
    }

    public Node getObjectID2() {
        return objectID2;
    }*/

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

        //Convert the ObjectIDs to actual Nodes

        //arc.objectID1 = arc.nodeRepository.findByObjectID((String)arc.objectID1);
        return arc;
    }

    public static AbstractArc getArcFromJsonString(String s, String relationshipType, String ObjectID1, String ObjectID2, NodeRepository nodeRepository) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        Class<AbstractArc> relationshipClass = AbstractArc.generateRelationshipClassFromString(relationshipType);

        AbstractArc arc = mapper.readValue(s, relationshipClass);
        Node node1 = nodeRepository.findByObjectID(ObjectID1);
        Node node2 = nodeRepository.findByObjectID(ObjectID2);
        if(node1 == null || node2 == null ){
            System.out.println("Why is one of these nodes null?");
        }
        arc.object1 = node1;
        arc.object2 = node2;
        return arc;
    }

    public static AbstractArc getArcFromJsonString(String s, NodeRepository nodeRepository) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        Map<String, Object> data = mapper.readValue(s, Map.class);
        String type = (String) data.get("type");
        String objectID1 = (String) data.get("objectID1");
        String objectID2 = (String) data.get("objectID2");

        return AbstractArc.getArcFromJsonString(s, type, objectID1, objectID2, nodeRepository);
    }
}
