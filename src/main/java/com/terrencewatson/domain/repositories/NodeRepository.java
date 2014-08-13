package com.terrencewatson.domain.repositories;

import com.terrencewatson.domain.Node;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Created by twatson on 8/11/14.
 */

public interface NodeRepository extends GraphRepository<Node>{
    //Node findByNodeId(String id);
    Node findById(String id);
    Result<Node> findByType(String type);
    Node findByObjectID(String objectID);

    Node deleteByObjectID(String objectID);
}
