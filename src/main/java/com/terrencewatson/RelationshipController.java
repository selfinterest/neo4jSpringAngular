package com.terrencewatson;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.terrencewatson.domain.Node;
import com.terrencewatson.domain.relationships.AbstractArc;
import com.terrencewatson.domain.relationships.Contains;
import com.terrencewatson.domain.repositories.NodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.GraphDatabase;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by twatson on 8/14/14.
 */

@Controller
@RequestMapping("/api/relationship")
public class RelationshipController {

    private Neo4jTemplate template;
    private NodeRepository nodeRepository;

    public RelationshipController(){}

    @Autowired
    public RelationshipController(Neo4jTemplate template, NodeRepository nodeRepository){
        this.template = template;
        this.nodeRepository = nodeRepository;
    }


    @RequestMapping(value="/{objectID1}/{relationshipType}/{objectID2}", produces = "application/json", consumes = "application/json", method = RequestMethod.PUT)
    @ResponseBody
    @Transactional
    public AbstractArc createRelationship(@PathVariable String objectID1, @PathVariable String relationshipType,  @PathVariable String objectID2, @RequestBody String relationshipJson){
        AbstractArc relationship = null;

        Node nodeA = nodeRepository.findByObjectID(objectID1);
        Node nodeB = nodeRepository.findByObjectID(objectID2);
        try {
            String className = relationshipType.toLowerCase();
            //Now capitalize the first letter
            className = className.substring(0, 1).toUpperCase() + className.substring(1);
            className = "com.terrencewatson.domain.relationships." + className;
            Class<AbstractArc> RelationshipClass = (Class<AbstractArc>) Class.forName(className);

            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            AbstractArc relationshipFromJson = mapper.readValue(relationshipJson, RelationshipClass);

            AbstractArc relationshipFromTemplate = template.createRelationshipBetween(nodeA, nodeB, RelationshipClass, relationshipType, false);

            relationshipFromTemplate.setDisplayName(relationshipFromJson.getDisplayName());
            relationshipFromTemplate.setWeight(relationshipFromJson.getWeight());

            template.save(relationshipFromTemplate);
            relationship = relationshipFromTemplate;
        } catch (ClassNotFoundException | JsonMappingException | JsonParseException e) {
            throw new BadRequestException();
        } catch (IOException e) {
            throw new InternalServerErrorException();
        }

        return relationship;
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public class ResourceNotFoundException extends RuntimeException{

    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    private class InternalServerErrorException extends RuntimeException {

    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public class BadRequestException extends RuntimeException {

    }
}


