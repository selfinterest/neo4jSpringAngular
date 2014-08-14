package com.terrencewatson.domain;

import com.terrencewatson.domain.relationships.Contains;
import com.terrencewatson.domain.repositories.NodeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.GraphDatabase;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.data.neo4j.template.Neo4jOperations;

/**
 * Created by twatson on 8/14/14.
 */

@RunWith(MockitoJUnitRunner.class)
public class StandaloneRelationshipTest {



    private Neo4jOperations template;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        this.template = Mockito.mock(Neo4jTemplate.class);
    }

    @Test
    public void testMakingRelationshipWithTemplate(){
        Node nodeA = new Node();
        nodeA.setDisplayName("Node A");
        nodeA.setObjectID("A");
        Node nodeB = new Node();
        nodeB.setDisplayName("Node B");
        nodeB.setObjectID("B");

        template.createRelationshipBetween(nodeA, nodeB, Contains.class, "CONTAINS", false);

    }
}
