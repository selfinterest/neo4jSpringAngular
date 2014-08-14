package com.terrencewatson.domain.relationships;

import com.terrencewatson.domain.relationships.AbstractArc;
import org.springframework.data.neo4j.annotation.RelationshipEntity;

/**
 * Created by twatson on 8/14/14.
 */

@RelationshipEntity(type="TEACHES")
public class Teaches extends AbstractArc {
}
