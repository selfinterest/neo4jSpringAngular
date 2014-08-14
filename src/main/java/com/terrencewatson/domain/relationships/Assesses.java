package com.terrencewatson.domain.relationships;

import org.springframework.data.neo4j.annotation.RelationshipEntity;

/**
 * Created by twatson on 8/14/14.
 */
@RelationshipEntity(type="ASSESSES")
public class Assesses extends AbstractArc {
}
