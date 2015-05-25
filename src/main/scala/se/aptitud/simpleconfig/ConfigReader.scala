package se.aptitud.simpleconfig

import com.mongodb.{DBCursor, DBCollection}
import com.mongodb.casbah.Imports._

class ConfigReader {
  val mongoClient = MongoClient("mongo", 27017)


  def read( component:String, version:Double, environment:String ) : String = {
    val path: JSFunction = component.concat("/").concat("v").concat(version.toString).concat("/").concat(environment)
    val builder = MongoDBObject.newBuilder
    builder += "environment" -> environment
    builder += "version" -> version
    builder += "active" -> "true"
    builder += "path" -> path
    val pattern = builder.result

    val configDb: MongoDB = mongoClient.getDB("config")
    val collection: DBCollection = configDb.getCollection(component)
    val find: DBCursor = collection.find(pattern)
    val first: DBObject = find.next()

    first.toString();

  }
}
