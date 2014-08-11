package com.terrencewatson.domain;

import org.springframework.data.annotation.Persistent;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

/**
 * Created by twatson on 8/11/14.
 */

@NodeEntity
@Persistent
public class Node {
    @GraphId
    private Long id;

    @Indexed(unique=true) Long nodeId;


    private String displayName;

    private String nodeType;

    private String label;

    private int order;
}
