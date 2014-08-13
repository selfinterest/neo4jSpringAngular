package com.terrencewatson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.terrencewatson.domain.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.neo4j.conversion.Result;
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

    @Autowired Neo4jOperations template;

    //@Autowired RestTemplate template;

    @Autowired
    private NodeRepository nodeRepository;

    @Autowired
    private SpringRestGraphDatabase graphDatabaseService;

    @Autowired
    private PlatformTransactionManager neo4jTransactionManager;

    @Autowired
    public NodeController(NodeRepository nodeRepository){
        this.nodeRepository = nodeRepository;
    }


    public NodeController(){

    }


    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Result<Node> findAll(){
        Result<Node> nodes = nodeRepository.findAll();
        return nodes;

    }

    @RequestMapping(value="/-/byType/{nodeType}")
    @ResponseBody
    @Transactional
    public ResponseEntity<String> showByNodeType(@PathVariable String nodeType){

        Collection<Node> nodes = nodeRepository.findByType(nodeType);
        ObjectMapper mapper = new ObjectMapper();
        try {
            return new ResponseEntity<String>(mapper.writeValueAsString(nodes), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<String>("Not ok!", HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @RequestMapping(method = RequestMethod.POST, produces="application/json", consumes="application/json")
    @ResponseBody
    @Transactional
    public ResponseEntity<String> create(@RequestBody Node node) throws JsonProcessingException {

        nodeRepository.save(node);
        return new ResponseEntity<String>(node.toJson(), HttpStatus.OK);

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
    public ResponseEntity<String> updateByObjectID(@PathVariable String objectID, @RequestBody Node newNode) {

        Node node = nodeRepository.findByObjectID(objectID);

        if(node != null){
            node.setNodeType(newNode.getType());
            node.setDisplayName(newNode.getDisplayName());
            node.setLabel(newNode.getLabel());
            node.setOrder(newNode.getOrder());
            nodeRepository.save(node);
            return new ResponseEntity<String>(node.toJson(), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("NOT OK", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value="/{id}")
    @ResponseBody
    @Transactional
    public ResponseEntity<String> deleteById(@PathVariable String id ) {
        Node node = nodeRepository.findById(id);
        if(node != null){
            nodeRepository.deleteByObjectID(id);
            return new ResponseEntity<String>(node.toJson(), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("NOT OK", HttpStatus.NOT_FOUND);
        }

    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public class ResourceNotFoundException extends RuntimeException{

    }
}
