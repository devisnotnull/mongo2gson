/*
   Copyright [2012] [Entrib Technologies]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package com.entrib.mongo2gson;

import com.google.gson.*;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * Utility class to convert Mongo Java objects into Google Json (Gson API) objects.
 * <p/>
 * This class has three APIs
 * <ul>
 *   <li>Convert given Mongo BasicDBList object to Google Gson JsonArray object</li>
 *   <li>Convert given Mongo BasicDBObject object to Google Gson JsonObject object</li>
 *   <li>Convert given primitive data object such as Long, Double, Boolean as well as String to Google Gson JsonPrimitive object</li>
 *   <li>Mongo ObjectId gets converted as string as returned by org.bson.types.ObjectId 
 *   <li>Date object gets converted into String currently, formatted as "MM-dd-yyyy-HH-mm-ss-SSS".  
 * </ul>
 *
 * The APIs are recursive and hence support nested objects.
 *  
 * @author Atul M Dambalkar (atul@entrib.com)
 */
public final class Mongo2gson {

    private static DateFormat defaultDateFormat =
            new SimpleDateFormat("MM-dd-yyyy-HH-mm-ss-SSS");

    private DateFormat dateFormat;

    public Mongo2gson() {
        this.dateFormat  = defaultDateFormat;
    }

    public Mongo2gson(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    
    /**
     * Convert the given mongo BasicDBList object to JsonArray.
     *
     * @param object BasicDBList
     * @return JsonArray
     */
    public JsonArray getAsJsonArray(DBObject object) {
        if (!(object instanceof BasicDBList)) {
            throw new IllegalArgumentException("Expected BasicDBList as argument type!");
        }
        BasicDBList list = (BasicDBList)object;
        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < list.size(); i++) {
            Object dbObject = list.get(i);
            if (dbObject instanceof BasicDBList) {
                jsonArray.add(getAsJsonArray((BasicDBList) dbObject));
            } else if (dbObject instanceof BasicDBObject) { // it's an object
                jsonArray.add(getAsJsonObject((BasicDBObject) dbObject));
            } else {   // it's a primitive type number or string 
                jsonArray.add(getAsJsonPrimitive(dbObject));
            }
        }
        return jsonArray;
    }

    /**
     * Convert the given mongo BasicDBObject to JsonObject.
     *
     * @param object BasicDBObject
     * @return JsonObject
     */
    public JsonObject getAsJsonObject(DBObject object) {
        if (!(object instanceof BasicDBObject)) {
            throw new IllegalArgumentException("Expected BasicDBObject as argument type!");
        }
        BasicDBObject dbObject = (BasicDBObject)object;        
        Set<String> keys = dbObject.keySet();
        Iterator<String> iterator = keys.iterator();
        JsonObject jsonObject = new JsonObject();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object innerObject = dbObject.get(key);
            if (innerObject instanceof BasicDBList) {
                jsonObject.add(key, getAsJsonArray((BasicDBList)innerObject));
            } else if (innerObject instanceof BasicDBObject) {
                jsonObject.add(key, getAsJsonObject((BasicDBObject)innerObject));
            } else {
                jsonObject.add(key, getAsJsonPrimitive(innerObject));
            }
        }
        return jsonObject;
    }

    /**
     * Convert the given object to Json primitive JsonElement based on the type.
     * 
     * @param value Object
     * @return JsonElement
     */
    public JsonElement getAsJsonPrimitive(Object value) {
        if (value instanceof String) {
            return new JsonPrimitive((String) value);
        } else if (value instanceof Character) {
            return new JsonPrimitive((Character) value);
        } else if (value instanceof Integer) {
            return new JsonPrimitive((Integer) value);
        } else if (value instanceof Long) {
            return new JsonPrimitive((Long) value);
        } else if (value instanceof Double) {
            return new JsonPrimitive((Double) value);
        } else if (value instanceof Boolean) {
            return new JsonPrimitive((Boolean) value);
        } else if (value instanceof ObjectId) {
        	return new JsonPrimitive(value.toString());
        } else if (value instanceof Date) {
        	return new JsonPrimitive(dateFormat.format((Date)value));
        }
        throw new IllegalArgumentException("Unsupported value type for: " + value);
    }
}
