package com.terrencewatson.config;

import com.terrencewatson.NodeController;
import com.terrencewatson.domain.Node;
import com.terrencewatson.domain.repositories.NodeRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by twatson on 8/12/14.
 */

@RunWith(MockitoJUnitRunner.class)
public class StandaloneNodeControllerTest {
    private MockMvc mockMvc;

    /*@Autowired
    NodeRepository nodeRepository;*/

    @Mock
    private NodeRepository nodeRepository;

    private Node testNode = new Node();
    private Collection<Node> testCollection = new ArrayList();

    @Before
    public void setUp(){
        this.testCollection = new ArrayList();
        this.testNode = new Node();
        this.testNode.setNodeType("test");
        this.testCollection.add(testNode);
        //mockMvc = MockMvcBuilders.standaloneSetup(new NodeController()).build();
        when(nodeRepository.findByNodeType("course")).thenReturn(testCollection);
    }

    @Test
    public void testFindByType() throws Exception {
        Collection<Node> nodes = nodeRepository.findByNodeType("course");
        if(nodes != null){
            assertTrue(nodes.contains(this.testNode));
        } else {
            throw new Exception("BLAH");
        }
    }
}
