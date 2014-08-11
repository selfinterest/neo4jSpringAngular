package com.terrencewatson.domain.repositories;

import com.terrencewatson.domain.Node;
import org.springframework.data.neo4j.repository.GraphRepository;

import java.util.Collection;

/**
 * Created by twatson on 8/11/14.
 */
interface NodeRepository extends GraphRepository<Node>{
    Node findByNodeId(Long id);
    Collection<Node> findByNodeType(String type);
}
