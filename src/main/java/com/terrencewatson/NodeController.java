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

    private NodeRepository nodeRepository;

    @Autowired
    private SpringRestGraphDatabase graphDatabaseService;

    @Autowired
    private PlatformTransactionManager neo4jTransactionManager;


    /*private Neo4jTemplate neo4jTemplate(){
        return new Neo4jTemplate(graphDatabaseService, neo4jTransactionManager);
    }*/

    @Autowired
    public NodeController(NodeRepository nodeRepository){
        this.nodeRepository = nodeRepository;
    }


    public NodeController(){

    }

    /*@RequestMapping(value="/{id}")
    @ResponseBody
    public ResponseEntity<String> show(Model model, @PathVariable String id){
        Node node = nodeRepository.findById(id);
        if(node != null){
            model.addAttribute("node", node);
            return new ResponseEntity<String>(node.toString(), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("Not found", HttpStatus.NOT_FOUND);
        }
    }*/

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> findAll(){
      Result<Node> nodes = nodeRepository.findAll();
      ObjectMapper mapper = new ObjectMapper();
        try {
            return new ResponseEntity<String>(mapper.writeValueAsString(nodes), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<String>("Not ok!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public ResponseEntity<String> create(@RequestBody String nodeJson) throws JsonProcessingException {
        Node node = Node.getNodeFromJsonString(nodeJson);

        //nodeRepository.save(node);
        //ObjectMapper mapper = new ObjectMapper();
        template.save(node);
        return new ResponseEntity<String>(node.toJson(), HttpStatus.OK);
        //return new ResponseEntity<String>("OK", HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value="/{id}")
    @ResponseBody
    @Transactional
    public ResponseEntity<String> getById(@PathVariable String id) {
        Node node = nodeRepository.findByObjectID(id);

        //Node node = (Node) template.getNode(1);
        //Node node = nodeRepository.findById(id);
        if(node != null){
           return new ResponseEntity<String>(node.toJson(), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("NOT OK", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value="/{id}")
    @ResponseBody
    @Transactional
    public ResponseEntity<String> updateById(@PathVariable String id, String nodeJson) {

        Node node = nodeRepository.findByObjectID(id);
        Node newNode = Node.getNodeFromJsonString(nodeJson);
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
}
