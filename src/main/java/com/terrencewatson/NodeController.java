package com.terrencewatson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.terrencewatson.domain.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;
import org.springframework.data.neo4j.support.Neo4jTemplate;
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
    @Autowired
    NodeRepository nodeRepository;

    @Autowired
    private SpringRestGraphDatabase graphDatabaseService;

    @Autowired
    private PlatformTransactionManager neo4jTransactionManager;


    private Neo4jTemplate neo4jTemplate(){
        return new Neo4jTemplate(graphDatabaseService, neo4jTransactionManager);
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

    @RequestMapping(value="/-/byType/{nodeType}")
    @ResponseBody
    @Transactional
    public ResponseEntity<String> showByNodeType(Model model, @PathVariable String nodeType){
        Collection<Node> nodes = nodeRepository.findByNodeType(nodeType);
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
    public ResponseEntity<String> create(@RequestBody String nodeJson){
        Node node = Node.getNodeFromJsonString(nodeJson);
        System.out.print(nodeJson);

        nodeRepository.save(node);
        return new ResponseEntity<String>("OK", HttpStatus.OK);
    }
}
