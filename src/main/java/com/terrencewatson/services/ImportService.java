package com.terrencewatson.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.terrencewatson.domain.Node;
import com.terrencewatson.domain.relationships.AbstractArc;
import com.terrencewatson.domain.repositories.NodeRepository;
import org.neo4j.graphdb.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.web.JSONResource;
import us.monoid.web.Resty;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import static us.monoid.web.Resty.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by twatson on 8/15/14.
 */

@Service
public class ImportService {

    private Neo4jOperations template;
    private NodeRepository nodeRepository;


    Resty resty = new Resty();

    public ImportService(){

    }

    @Autowired
    public ImportService(NodeRepository nodeRepository, Neo4jOperations template){
        this.nodeRepository = nodeRepository;
        this.template = template;
    }




    public ArrayList<AbstractArc> extractArcs(List<JsonNode> arcs) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<AbstractArc> result = new ArrayList<AbstractArc>();
        for(JsonNode n : arcs){
            System.out.println(n.toString());
            String type = n.get("type").asText();
            Class<AbstractArc> relationshipClass = AbstractArc.generateRelationshipClassFromString(type);
            if(relationshipClass != null){
                String nodeJsonText = n.asText();
                AbstractArc relationship = mapper.readValue(nodeJsonText, relationshipClass);
            } else {
                throw new Exception("Unknown class");
            }


        }


        return result;
    }

    public ArrayList<Node> extractNodes(List<JsonNode> nodeNode){
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Node> result = new ArrayList<Node>();
        Object[] nodeArray = nodeNode.toArray();
        for(Object n : nodeArray){

            System.out.println(n.toString());
            //String nodeJsonText = n.asText();
            //Node node = Node.getNodeFromJsonString(nodeJsonText);
            //result.add(node);
        }

        return result;
    }


    private Set<Node> nodeListFromMap(Map<String, Object> map){
        Set<Node> result = null;

        return result;
    }

    public ArrayList<Node> processNodesFromJsonTree(JsonNode jsonTree){
        ArrayList<Node> result = new ArrayList<Node>();

        for(int x = 0; x < jsonTree.size(); x++){
            JsonNode jsonNode = jsonTree.get(x);
            Node node = Node.getNodeFromJsonString(jsonNode.toString());
            template.save(node);
            //result.add(node);
        }

        return result;
    }

    public ArrayList<AbstractArc> processArcsFromJsonTree(JsonNode jsonTree) throws IOException {
        ArrayList<AbstractArc> result = new ArrayList<AbstractArc>();

        for(int x = 0; x < jsonTree.size(); x++){
            JsonNode jsonNode = jsonTree.get(x);
            String type = "contains";
            //System.out.println(jsonNode.path("type"));
            //String type = jsonNode.get("type").toString();

            //Class<AbstractArc> relationshipClass = AbstractArc.generateRelationshipClassFromString(type);
            AbstractArc arc = AbstractArc.getArcFromJsonString(jsonNode.toString(), nodeRepository);

            try {
                template.save(arc);
            } catch (NullPointerException e){
                System.out.println("Why is arc null?");
            }

            //result.add(arc);
        }
        return result;

    }

    public ImportedResult fromUrl(String url) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        URL urlObject = new URL(url);
        JsonNode data = mapper.readTree(urlObject);

        JsonNode nodes = data.findPath("nodes");
        JsonNode arcs = data.findPath("arcs");


        ImportedResult result = new ImportedResult(this.processNodesFromJsonTree(nodes), this.processArcsFromJsonTree(arcs));
        return result;

    }

    public static class ImportedResult {

        @Autowired
        protected PlatformTransactionManager neo4jTransactionManager;


        private ArrayList<Node> nodes;
        private ArrayList<AbstractArc> arcs;


        @Autowired
        Neo4jOperations template;

        public ArrayList<Node> getNodes() {
            return nodes;
        }

        public void setNodes(ArrayList<Node> nodes) {
            this.nodes = nodes;
        }

        public ArrayList<AbstractArc> getArcs() {
            return arcs;
        }

        public void setArcs(ArrayList<AbstractArc> arcs) {
            this.arcs = arcs;
        }

        public ImportedResult(ArrayList<Node> nodes, ArrayList<AbstractArc> abstractArcs){
            this.nodes = nodes;
            this.arcs = abstractArcs;
        }

        @Transactional
        public void saveNodes(Neo4jOperations template) throws HeuristicRollbackException, HeuristicMixedException, RollbackException, SystemException {
            Transaction tx = template.getGraphDatabase().beginTx();
            for(Node node : this.nodes) {
                Node newNode = template.save(node);
                
                //System.out.println(node.getDisplayName());
            }
            tx.close();
        }

        @Transactional
        public void saveArcs(Neo4jOperations template){
            for(AbstractArc arc : this.arcs){
                if(arc != null){

                    template.save(arc);
                }

            }
        }
        public void save(Neo4jTemplate template, NodeRepository nodeRepository){
            //save nodes
            try {
                saveNodes(template);
            } catch (HeuristicRollbackException e) {
                e.printStackTrace();
            } catch (HeuristicMixedException e) {
                e.printStackTrace();
            } catch (RollbackException e) {
                e.printStackTrace();
            } catch (SystemException e) {
                e.printStackTrace();
            }
            saveArcs(template);
            /*for(Node node : this.nodes) {
                Node newNode = template.save(node);
                //System.out.println(node.getDisplayName());
            }*/


        }

    }


}
