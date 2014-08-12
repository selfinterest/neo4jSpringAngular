package com.terrencewatson.config;

import com.terrencewatson.domain.repositories.NodeRepository;
import org.mockito.Mock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by twatson on 8/12/14.
 */

@Configuration
@ComponentScan
public class TestContext {
    @Mock
    NodeRepository nodeRepository;

    @Bean
    public NodeRepository getNodeRepository(){
        return nodeRepository;
    }

}
