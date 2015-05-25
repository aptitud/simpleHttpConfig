package se.aptitud.simpleconfig

import com.mongodb.casbah.Imports._
import com.mongodb.util.JSON
import com.mongodb.{DBCollection, DBCursor}

class ConfigWriter {
  val mongoClient = MongoClient("mongo", 27017)
  val configDb: MongoDB = mongoClient.getDB("config")


  def update( component:String, version:Double, environment:String, jsonContent:String ) : String = {
    val collection: DBCollection = configDb.getCollection(component)
    val path: JSFunction = component.concat("/").concat("v").concat(version.toString).concat("/").concat(environment)

    val builder = MongoDBObject.newBuilder
    builder += "environment" -> environment
    builder += "version" -> version
    builder += "active" -> "true"
    builder += "path" -> path
    val pattern = builder.result

    val find: DBCursor = collection.find(pattern)
    if(find.hasNext){
      val first: DBObject = find.next()
      first.put("replaced", java.time.Instant.now().toEpochMilli)
      first.put("active", "false")
      val result: WriteResult = collection.update(pattern, first)
      System.out.println("UPDATED -> " + result.toString)
    }

    val updated:AnyRef =  JSON.parse(jsonContent)
    val newItem:DBObject=  updated.asInstanceOf[DBObject]
    newItem.put("environment",environment)
    newItem.put("version",version)
    newItem.put("path", path)
    newItem.put("active", "true")
    collection.insert(newItem)
    newItem.toString

  }
}
