package se.aptitud.simpleconfig

import com.mongodb.casbah.Imports._
import com.mongodb.util.JSON
import com.mongodb.{DBCollection, DBCursor}

class ConfigWriter {
  val mongoClient = MongoClient("mongo", 27017)
  val configDb: MongoDB = mongoClient.getDB("config")


  def update( component:String, version:Double, environment:String, jsonContent:String ) : String = {
    val collection: DBCollection = configDb.getCollection(component)

    val builder = MongoDBObject.newBuilder
    builder += "environment" -> environment
    builder += "version" -> version
    builder += "active" -> "true"
    builder += "path" -> component.concat("/").concat("v").concat(version.toString).concat("/").concat(environment)
    val pattern = builder.result

    val find: DBCursor = collection.find(pattern)
    if(find.hasNext){
      val next: DBObject = find.next()
      next.put("replaced", java.time.Instant.now().toEpochMilli)
      next.put("active", "false")
      val result: WriteResult = collection.update(pattern, next)
      System.out.println("UPDATED -> " + result.toString)
    }

    val updated:AnyRef =  JSON.parse(jsonContent)
    val toStore:DBObject=  updated.asInstanceOf[DBObject]
    toStore.put("environment",environment)
    toStore.put("version",version)
    toStore.put("path", component.concat("/").concat("v").concat(version.toString).concat("/").concat(environment))
    toStore.put("active", "true")
    collection.insert(toStore)
    toStore.toString

  }
}
