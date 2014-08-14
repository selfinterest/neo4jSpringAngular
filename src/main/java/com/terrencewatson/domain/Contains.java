package com.terrencewatson.domain;

import org.springframework.data.neo4j.annotation.RelationshipEntity;

/**
 * Created by twatson on 8/14/14.
 */

@RelationshipEntity(type="CONTAINS")
public class Contains extends AbstractArc{


}
