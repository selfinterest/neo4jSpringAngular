package com.terrencewatson.domain;

import org.springframework.data.neo4j.annotation.RelationshipEntity;

/**
 * Created by twatson on 8/14/14.
 */
@RelationshipEntity(type = "COVERS")
public class Covers extends AbstractArc {
}
