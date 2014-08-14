package com.terrencewatson.domain.relationships;

import com.terrencewatson.domain.Node;
import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

/**
 * Created by twatson on 8/14/14.
 */

@RelationshipEntity
public abstract class AbstractArc {

    private final Class<? extends AbstractArc> theClass;
    String displayName;
    String weight;

    @GraphId
    Long id;

    public AbstractArc(){
        this.theClass = this.getClass();
    }
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @StartNode private Node objectID1;
    @EndNode private Node objectID2;
}
