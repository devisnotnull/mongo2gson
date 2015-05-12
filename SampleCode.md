Sample code on how to use the APIs

# Introduction #

This page contains the sample code that shows how to use the APIs.


# Details #

### Converting BasicDBObject into JsonObject ###
```

import com.entrib.mongo2gson.Mongo2gson;

...

    // Construct a Mongo BasicDBObject 
    // The BasicDBObject can come from Mongo Java Driver also
    DBObject obj = new BasicDBObject();
    obj.put( "foo1", "bar1" );
    obj.put( "foo2", "bar2" );
    
    Mongo2gson mongo2gson = new Mongo2gson();
    // Now convert the BasicDBObject into JsonObejct
    JsonObject jsonObject = mongo2gson.getAsJsonObject(obj);
```

### Converting BasicDBList into JsonArray ###
```

import com.entrib.mongo2gson.Mongo2gson;

...

    // Construct a Mongo BasicDBList
    DBObject obj1 = new BasicDBObject();
    obj1.put( "foo1", "bar1" );
    DBObject obj2 = new BasicDBObject();
    obj2.put( "foo2", "bar2" );

    DBObject array= new BasicDBList();
    array.add(obj1);
    array.add(obj2);

    Mongo2gson mongo2gson = new Mongo2gson();
    // Convert the object using getAsJsonArray API
    JsonArray jsonArray = 
            mongo2gson.getAsJsonArray(array);

```


### Formatting the Date object that's part of Mongo obejcts ###
While converting Mongo objects to Gson objects, if a Mongo object contains an embedded Date object, then this Date gets converted as JsonElement with underlying data type as String. This string can be now formatted the way application needs.

```

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.entrib.mongo2gson.Mongo2gson;

...

    // Construct a Mongo BasicDBObject 
    // The BasicDBObject can come from Mongo Java Driver also
    DBObject obj = new BasicDBObject();
    obj.put( "foo1", "bar1" );
    obj.put( "foo2", "bar2" );
    obj.put( "foo3", new Date());
    
    DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

    Mongo2gson mongo2gson = new Mongo2gson(dateFormat);
    // Now convert the BasicDBObject into JsonObject
    JsonObject jsonObject = mongo2gson.getAsJsonObject(obj);
```