package com.terrencewatson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.terrencewatson.domain.Node;
import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.core.GraphDatabase;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import com.terrencewatson.domain.repositories.NodeRepository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

/**
 * Created by twatson on 8/11/14.
 */

@Controller
@RequestMapping("/api/node")
public class NodeController {
    /*@Autowired
    NodeRepository nodeRepository;*/



    //@Autowired Neo4jOperations template;

    //@Autowired RestTemplate template;

    @Autowired
    private NodeRepository nodeRepository;

    @Autowired
    private SpringRestGraphDatabase graphDatabaseService;

    @Autowired
    private PlatformTransactionManager neo4jTransactionManager;

    @Autowired
    private GraphDatabase graphDatabase;

    private Neo4jOperations template;

    public NodeController(){}

        @Autowired
    public NodeController(NodeRepository nodeRepository, GraphDatabase graphDatabase, PlatformTransactionManager neo4jTransactionManager){
        this.nodeRepository = nodeRepository;
        this.template = new Neo4jTemplate(graphDatabase, neo4jTransactionManager);
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Result<Node> findAll(){

        Result<Node> nodes = template.findAll(Node.class);
        //Result<Node> nodes = nodeRepository.findAll();
        return nodes;

    }

    @RequestMapping(value="/-/byType/{nodeType}", produces = "application/json")
    @ResponseBody
    @Transactional
    public Collection<Node> showByNodeType(@PathVariable String nodeType){

        Collection<Node> nodes = nodeRepository.findByType(nodeType);
        return nodes;


    }

    @RequestMapping(method = RequestMethod.POST, produces="application/json", consumes="application/json")
    @ResponseBody
    @Transactional
    public Node create(@RequestBody Node node) throws JsonProcessingException {

        try {
            nodeRepository.save(node);
            return node;
        } catch (Exception e){
            throw new InternalServerErrorException();
        }


    }

    @RequestMapping(method = RequestMethod.GET, value="/{objectID}", produces="application/json")
    @ResponseBody
    @Transactional
    public Node getByObjectID(@PathVariable String objectID) {
        Node node = nodeRepository.findByObjectID(objectID);
        if(node == null) throw new ResourceNotFoundException();
        return node;
    }

    @RequestMapping(method = RequestMethod.PUT, value="/{objectID}", produces="application/json", consumes="application/json")
    @ResponseBody
    @Transactional
    public Node updateByObjectID(@PathVariable String objectID, @RequestBody Node newNode) {

        Node node = nodeRepository.findByObjectID(objectID);

        try {
            if(node != null){
                node.setNodeType(newNode.getType());
                node.setDisplayName(newNode.getDisplayName());
                node.setLabel(newNode.getLabel());
                node.setOrder(newNode.getOrder());
                nodeRepository.save(node);
                return node;
            } else {
                throw new ResourceNotFoundException();
            }
        } catch (ResourceNotFoundException e){
            throw new ResourceNotFoundException();
        } catch (Exception e){
            throw new InternalServerErrorException();
        }

    }

    @RequestMapping(method = RequestMethod.DELETE, value="/{objectID}", produces="application/json")
    @ResponseBody
    @Transactional
    public Node deleteByObjectID(@PathVariable String objectID ) {
        Node node = nodeRepository.findByObjectID(objectID);

        if(node != null){
            nodeRepository.delete(node);
            return node;
        } else {
            throw new ResourceNotFoundException();
        }

    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public class ResourceNotFoundException extends RuntimeException{

    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    private class InternalServerErrorException extends RuntimeException {

    }
}
