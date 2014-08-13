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
        this.testNode.setDisplayName("Some node");
        this.testNode.setObjectID("abcd");
        this.testCollection.add(testNode);
        //mockMvc = MockMvcBuilders.standaloneSetup(new NodeController()).build();
        when(nodeRepository.findByType("course")).thenReturn(testCollection);
        when(nodeRepository.findById("abcd")).thenReturn(testNode);
        when(nodeRepository.findById("efgh")).thenReturn(null);
        when(nodeRepository.deleteById("abcd")).thenReturn(testNode);
        nodeController = new NodeController(nodeRepository);
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testShowByType() throws Exception {
        String expectedJsonResponse = "[{\"objectID\":\"abcd\",\"displayName\":\"Some node\",\"type\":\"test\",\"label\":null,\"order\":0,\"id\":null}]";

        model.addAttribute("nodeType", "course");
        ResponseEntity<String> response = nodeController.showByNodeType("course");
        //System.out.println(response.getBody());
        assertTrue(response.getBody().contains(expectedJsonResponse));

    }

    @Test
    public void testCreate() throws JsonProcessingException {
        ResponseEntity<String> response = nodeController.create("{\"displayName\":\"Test node\",\"type\":\"course\",\"label\":null,\"order\":0,\"id\":null}");
        assertTrue(response.getBody().contains("Test node"));
        assertTrue(response.toString().contains("200 OK"));
    }

    @Test
    public void testGetById(){
        ResponseEntity<String> response = nodeController.getById("abcd");
        assertTrue(response.getBody().contains("abcd"));

        response = nodeController.getById("efgh");
        assertTrue(response.toString().contains("404"));
    }

    @Test
    public void testUpdateById(){
        Node newNode = new Node();
        newNode.setDisplayName("I am a new node");
        newNode.setNodeType("course");
        ResponseEntity<String> response = nodeController.updateById("abcd", newNode.toJson());
        assertTrue(response.getBody().contains("I am a new node"));
        assertTrue(response.getBody().contains("abcd"));
        response = nodeController.updateById("efgh", newNode.toJson());

        assertTrue(response.toString().contains("404"));
    }

    @Test
    public void testDeleteById(){
        ResponseEntity<String> response = nodeController.deleteById("abcd");
    }

}
