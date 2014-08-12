package com.terrencewatson.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.terrencewatson.NodeController;
import com.terrencewatson.domain.Node;
import com.terrencewatson.domain.repositories.NodeRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;
import org.springframework.ui.Model;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by twatson on 8/12/14.
 */

@RunWith(MockitoJUnitRunner.class)
public class StandaloneNodeControllerTest  {
    private MockMvc mockMvc;

    /*@Autowired
    NodeRepository nodeRepository;*/

    @Mock
    private NodeRepository nodeRepository;



    @Mock
    private Model model;

    private Node testNode = new Node();
    private Collection<Node> testCollection = new ArrayList();

    private NodeController nodeController;// = new NodeController();

    @Before
    public void setUp(){

        this.testCollection = new ArrayList();
        this.testNode = new Node();
        this.testNode.setNodeType("test");
        this.testCollection.add(testNode);
        //mockMvc = MockMvcBuilders.standaloneSetup(new NodeController()).build();
        when(nodeRepository.findByNodeType("course")).thenReturn(testCollection);

        nodeController = new NodeController(nodeRepository);
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testShowByType() throws Exception {
        model.addAttribute("nodeType", "course");
        ResponseEntity<String> response = nodeController.showByNodeType("course");
        assertTrue(response.getBody().contains("[{\"displayName\":null,\"nodeType\":\"test\",\"label\":null,\"order\":0,\"id\":null}]"));

    }

    @Test
    public void testCreate() throws JsonProcessingException {
        ResponseEntity<String> response = nodeController.create("{\"displayName\":\"Test node\",\"nodeType\":\"course\",\"label\":null,\"order\":0,\"id\":null}");
        assertTrue(response.getBody().contains("Test node"));
        assertTrue(response.toString().contains("200 OK"));
    }

    @Test
    public void testUpdateById(){
        ResponseEntity<String> response = nodeController.updateById("abcd");
    }
}
