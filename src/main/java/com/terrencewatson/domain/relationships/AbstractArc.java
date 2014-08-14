package com.terrencewatson.domain.relationships;

import com.terrencewatson.domain.Node;
import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

/**
 * Created by twatson on 8/14/14.
 */

public abstract class AbstractArc {
    String displayName;
    String weight;

    @StartNode private Node objectID1;
    @EndNode private Node objectID2;
}
