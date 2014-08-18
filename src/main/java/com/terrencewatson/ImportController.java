package com.terrencewatson;

import com.terrencewatson.domain.repositories.NodeRepository;
import com.terrencewatson.services.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Created by twatson on 8/15/14.
 */

@Controller
@RequestMapping("/import")
public class ImportController {

    @Autowired
    ImportService importService;

    private Neo4jTemplate template;


    private NodeRepository nodeRepository;


    public ImportController(){

    }

    @Autowired
    public ImportController(Neo4jTemplate template, NodeRepository nodeRepository){
        this.template = template;
        this.nodeRepository = nodeRepository;
    }

    @RequestMapping("/")
    @Transactional
    public void importFromUrl(@RequestParam String url) throws Exception {
        ImportService.ImportedResult result = importService.fromUrl(url);
        //result.save(this.template, this.nodeRepository);

    }
}
