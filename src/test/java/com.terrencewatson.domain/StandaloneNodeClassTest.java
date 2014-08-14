package com.terrencewatson.domain;

import com.terrencewatson.NodeController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.support.Neo4jTemplate;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by twatson on 8/14/14.
 */

@RunWith(MockitoJUnitRunner.class)
public class StandaloneNodeClassTest {
    @Before
    public void setUp(){

        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testNodeConstruction(){
        Node node = new Node();
        assertTrue(node != null);
    }

    @Test
    public void testNodeSetters(){
        Node node = new Node();
        node.setDisplayName("Some node");
        assertTrue(node.getDisplayName().equals("Some node"));

        node.setLabel("Some label");
        assertTrue(node.getLabel().equals("Some label"));


        node.setQualifier("qualifier");
        assertTrue(node.getQualifier().equals("qualifier"));
    }

    @Test
    public void testNodeUpdate(){
        Node node = new Node();
        node.setObjectID("abcd");
        node.setLabel("I am a node");
        node.setDisplayName("I am this node's display name.");
        Node newNode = new Node();
        newNode.setObjectID("efgh");
        newNode.setLabel("I am a different label").setDisplayName("I am a different node.");
        node.update(newNode);
        assertTrue(node.getObjectID().equals("abcd"));
        assertTrue(node.getLabel().equals("I am a different label"));

    }
}
