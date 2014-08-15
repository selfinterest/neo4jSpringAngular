package com.terrencewatson.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.terrencewatson.domain.Node;
import com.terrencewatson.domain.relationships.AbstractArc;
import com.terrencewatson.domain.relationships.Contains;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.web.JSONResource;
import us.monoid.web.Resty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;
/**
 * Created by twatson on 8/15/14.
 */

@RunWith(MockitoJUnitRunner.class)
public class ImportServiceTest {

    //JSONObject jsonObject = new JSONObject();

    //JSONResource jsonResource = new JSONResource();
    String testUrl = "http://dev-stable.cxp.corp.web/contentservice/assets/easd01h/course_definitions/9781285846415.json";
    Resty resty;

    JsonNode jsonNode;
    JsonNode jsonNodeForArcs;
    ImportService importService = new ImportService();

    @Before
    public void setUp() throws Exception {
        ArrayList<Object> fakeResult = new ArrayList<Object>();
        resty = mock(Resty.class);
        jsonNode = mock(JsonNode.class);
        jsonNodeForArcs = mock(JsonNode.class);
        JSONResource jsonResource = mock(JSONResource.class);
        when(resty.json("http://dev-stable.cxp.corp.web/contentservice/assets/easd01h/course_definitions/9781285846415.json")).thenReturn(jsonResource);
        when(jsonResource.get("nodes")).thenReturn(fakeResult);

        when(jsonNode.size()).thenReturn(1);
        when(jsonNode.get(0)).thenReturn(jsonNode);
        when(jsonNode.toString()).thenReturn("{\"displayName\":\"Henley Chemistry Title\", \"type\":\"course\", \"objectID\":\"HEN.CHEM.COURSE.1\"}");
        when(jsonNodeForArcs.size()).thenReturn(1);
        when(jsonNodeForArcs.get(0)).thenReturn(jsonNodeForArcs);
        when(jsonNodeForArcs.toString()).thenReturn("{\"objectID1\":\"HEN.CHEM.COURSE.1\", \"objectID2\":\"HEN.CHEM.PART.1\", \"displayName\":\"course - part 1\", \"type\":\"CONTAINS\", \"weight\":\"1.0\"}");
        MockitoAnnotations.initMocks(this);
    }

    /*@Test
    public void testImportFromUrl() throws Exception {
        //System.out.println(importService);
        System.out.println(importService.fromUrl(testUrl));
    }

    @Test
    public void nodeListFromMap(){
        Map<String, Object> testMap = new HashMap<String, Object>();
        ArrayList testNodeArray = new ArrayList();

        //testNodeArray.add()
        ArrayList testArcArray = new ArrayList();
        testMap.put("nodes", testNodeArray);
        testMap.put("arcs", testArcArray);
    }*/

    @Test
    public void testProcessNode(){
        ArrayList<Node> nodes = importService.processNodesFromJsonTree(jsonNode);
        Node firstNode = nodes.get(0);
        assertTrue(firstNode.getObjectID().equals("HEN.CHEM.COURSE.1"));
    }

    @Test
    public void testProcessArc() throws IOException {
        ArrayList<AbstractArc> arcs = importService.processArcsFromJsonTree(jsonNodeForArcs);
        System.out.print(arcs.size());
        Contains contains = (Contains) arcs.get(0);
        System.out.print(contains.getDisplayName());
        assertTrue(contains.getDisplayName().contains("course"));
    }

}
