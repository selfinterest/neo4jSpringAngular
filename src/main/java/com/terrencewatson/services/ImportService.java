package com.terrencewatson.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.terrencewatson.domain.Node;
import com.terrencewatson.domain.relationships.AbstractArc;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.web.JSONResource;
import us.monoid.web.Resty;
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

    Resty resty = new Resty();

    public ImportService(){

    }

    /*private JSONObject openUrl(String url) throws IOException, JSONException {
        return resty.json(url).object();
    }

    private ArrayList<Node> extractNodes(JSONObject json) throws Exception {
        ArrayList<Node> nodes = new ArrayList<Node>();
        JSONArray jsonArray = (JSONArray) json.get("nodes");
        int jsonArrayLength = jsonArray.length();
        for(int x = 0; x < jsonArrayLength - 1; x++){
            JSONObject object = jsonArray.getJSONObject(x);
            String objectJsonString = object.toString();
            Node node = Node.getNodeFromJsonString(objectJsonString);
            nodes.add(node);
        }

        return nodes;

    }

    private ArrayList extractArcs(JSONObject json) throws Exception {
        ArrayList<AbstractArc> arcs = new ArrayList<AbstractArc>();
        JSONArray jsonArray = (JSONArray) json.get("arcs");
        int jsonArrayLength = jsonArray.length();
        for(int x = 0; x < jsonArrayLength - 1; x++){
            JSONObject object = jsonArray.getJSONObject(x);
            Object<AbstractArc> arc

            String objectJsonString = object.toString();

        }
        ArrayList<AbstractArc> arcs = (ArrayList<AbstractArc>) json.get("arcs");
        return arcs;
    }

    public ArrayList<Node> fromUrl(String url) throws IOException {
        ArrayList nodes = null;
        ArrayList arcs = null;

        try {
            JSONObject json = openUrl(url);
            nodes = extractNodes(json);
            arcs = extractArcs(json);
            return nodes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/

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
            result.add(node);
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
            AbstractArc arc = AbstractArc.getArcFromJsonString(jsonNode.toString(), type);
            result.add(arc);
        }
        return result;

    }

    public ImportedResult fromUrl(String url) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        URL urlObject = new URL(url);
        JsonNode data = mapper.readTree(urlObject);

        JsonNode nodes = data.findPath("nodes");
        processNodesFromJsonTree(nodes);

        //System.out.println(nodes.get(0).get("type"));
        ImportedResult result = new ImportedResult();
        return result;
        //Map<String, Object> data = mapper.readValue(urlObject, Map.class);

        //ArrayList nodes = (ArrayList) data.get("nodes");


        //JsonNode data = mapper.readTree(urlObject);
        //List<JsonNode> nodeNode = data.findValues("nodes");
        //List<JsonNode> arcNode = data.findValues("arcs");
        /*try {
            ArrayList<Node> nodes = extractNodes(nodeNode);
            ArrayList<AbstractArc> arcs = extractArcs(arcNode);
            return new ImportedResult(nodes, arcs);
        } catch (Exception e){
            System.out.print(e);
            throw(e);
        }*/

        //JsonNode nodeNode = data.path("nodes");
        //JsonNode arcNode = data.path("arcs");

    }

    public static class ImportedResult {

        private ArrayList<Node> nodes;
        private ArrayList<AbstractArc> arcs;


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

        public ImportedResult(){

        }

    }


}
