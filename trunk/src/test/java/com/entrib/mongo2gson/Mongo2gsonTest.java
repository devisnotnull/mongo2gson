package com.entrib.mongo2gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for BasicDBObject App.
 *
 * @author Atul Dambalkar atul@entrib.com
 */
public class Mongo2gsonTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public Mongo2gsonTest(String testName) {
        super(testName);
    }

    private static String simpleJsonObject =
            "{\"menu\": " +
                "{\n" +
            "  \"id\": \"file\",\n" +
            "  \"value\": \"File\",\n" +
                "}" +
            "}";

    private static String nestedJsonObject =
            "{\"menu\": " +
                "{\n" +
                "  \"id\": \"file\",\n" +
                "  \"value\": \"File\",\n" +
                "  \"popup\":\n" +
                "      {\"value\": \"New\", " +
                      "\"onclick\": \"CreateNewDoc()\"},\n" +
                    "  }\n" +
            "}}";

    private static String nestedJsonObjectWithArray = "{\"menu\": {\n" +
            "  \"id\": \"file\",\n" +
            "  \"value\": \"File\",\n" +
            "  \"popup\": {\n" +
            "    \"menuitem\": [\n" +
            "      {\"value\": \"New\", \"onclick\": \"CreateNewDoc()\"},\n" +
            "      {\"value\": \"Open\", \"onclick\": \"OpenDoc()\"},\n" +
            "      {\"value\": \"Close\", \"onclick\": \"CloseDoc()\"}\n" +
            "    ]\n" +
            "  }\n" +
            "}}";

    private static String multiNestedJsonObject = "{\"widget\": {\n" +
            "    \"debug\": \"on\",\n" +
            "    \"window\": {\n" +
            "        \"title\": \"Sample Konfabulator Widget\",\n" +
            "        \"name\": \"main_window\",\n" +
            "        \"width\": 500,\n" +
            "        \"test\": true,\n" +
            "        \"height\": 500\n" +
            "    },\n" +
            "    \"image\": { \n" +
            "        \"src\": \"Images/Sun.png\",\n" +
            "        \"name\": \"sun1\",\n" +
            "        \"hOffset\": 250,\n" +
            "        \"vOffset\": 250,\n" +
            "        \"alignment\": \"center\"\n" +
            "    },\n" +
            "    \"text\": {\n" +
            "        \"data\": \"Click Here\",\n" +
            "        \"size\": 36,\n" +
            "        \"style\": \"bold\",\n" +
            "        \"name\": \"text1\",\n" +
            "        \"hOffset\": 250,\n" +
            "        \"vOffset\": 100,\n" +
            "        \"alignment\": \"center\",\n" +
            "        \"onMouseUp\": \"sun1.opacity = (sun1.opacity / 100) * 90;\"\n" +
            "    }\n" +
            "}}    ";

    /**
     * Rigourous Test :-)
     */
    public void testSimpleBasicDBObject() {
        BasicDBObject basicDBObject =
                (BasicDBObject)JSON.parse(simpleJsonObject);

        JsonObject jsonObject = Mongo2gson.getAsJsonObject(basicDBObject);
        assertEquals(jsonObject.getAsJsonObject("menu").get("id").getAsString(), "file");
    }

    public void testNestedBasicDBObject() {
        BasicDBObject basicDBObject =
                (BasicDBObject)JSON.parse(nestedJsonObject);

        JsonObject jsonObject = Mongo2gson.getAsJsonObject(basicDBObject);
        JsonObject popup = jsonObject.getAsJsonObject("menu").get("popup").getAsJsonObject();
        assertEquals(popup.get("value").getAsString(), "New");
    }

    public void testNestedBasicDBObjectWithArray() {
        BasicDBObject basicDBObject =
                (BasicDBObject)JSON.parse(nestedJsonObjectWithArray);

        JsonObject jsonObject = Mongo2gson.getAsJsonObject(basicDBObject);
        JsonArray menuitems =
                (JsonArray)jsonObject.getAsJsonObject("menu").get("popup").getAsJsonObject().get("menuitem").getAsJsonArray();
        assertEquals(menuitems.get(0).getAsJsonObject().get("value").getAsString(), "New");
        assertEquals(menuitems.get(1).getAsJsonObject().get("value").getAsString(), "Open");
    }

    public void testMultiNestedBasicDBObejct() {
        BasicDBObject basicDBObject =
                (BasicDBObject)JSON.parse(multiNestedJsonObject);

        JsonObject jsonObject = Mongo2gson.getAsJsonObject(basicDBObject);
        assertEquals(jsonObject.get("widget").getAsJsonObject().get("text").getAsJsonObject().get("size").getAsInt(), 36);

    }

}
