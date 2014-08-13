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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.neo4j.conversion.Handler;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.conversion.ResultConverter;
import org.springframework.data.neo4j.mapping.MappingPolicy;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;
import org.springframework.ui.Model;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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
        Result<Node> nodes;

        this.testNode = new Node();
        this.testNode.setNodeType("test");
        this.testNode.setDisplayName("Some node");
        this.testNode.setObjectID("abcd");
        this.testCollection.add(testNode);
        //mockMvc = MockMvcBuilders.standaloneSetup(new NodeController()).build();
        when(nodeRepository.findByType("course")).thenReturn(testCollection);
        when(nodeRepository.findByType("blah")).thenReturn(new ArrayList());
        when(nodeRepository.findByObjectID("abcd")).thenReturn(testNode);
        when(nodeRepository.findByObjectID("efgh")).thenReturn(null);
        when(nodeRepository.deleteByObjectID("abcd")).thenReturn(testNode);
        nodeController = new NodeController(nodeRepository);
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testShowByType() throws Exception {
        Collection<Node> response = new ArrayList<Node>();
        response = nodeController.showByNodeType("course");
        assertTrue(response.contains(testNode));
        response = nodeController.showByNodeType("blah");
        assertTrue(response.isEmpty());

    }

    @Test
    public void testCreate() throws JsonProcessingException {
        Node node = nodeController.create(this.testNode);
        assertTrue(node.getDisplayName().contains("Some node"));

    }


    @Test
    public void testGetByObjectID(){
        Node node  = nodeController.getByObjectID("abcd");
        assertTrue(node.getObjectID().contains("abcd"));

        try {
            node = nodeController.getByObjectID("efgh");
        } catch (Exception e){
            assertTrue(e instanceof NodeController.ResourceNotFoundException);
        }


    }

    @Test
    public void testUpdateByObjectID(){
        Node newNode = new Node();
        newNode.setDisplayName("I am a new node");
        newNode.setNodeType("course");
        Node response = nodeController.updateByObjectID("abcd", newNode);
        assertTrue(response.getDisplayName().contains("I am a new node"));
        assertTrue(response.getObjectID().contains("abcd"));

        try {
            response = nodeController.updateByObjectID("efgh", newNode);
        } catch (Exception e){
            assertTrue(e instanceof NodeController.ResourceNotFoundException);
        }



    }

    @Test
    public void testDeleteById(){
        Node node = nodeController.deleteByObjectID("abcd");
        assertTrue(node.getDisplayName().contains("Some node"));
    }

}
