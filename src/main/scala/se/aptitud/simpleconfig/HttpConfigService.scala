package se.aptitud.simpleconfig

import akka.actor.Actor
import spray.http.HttpHeaders.`Content-Type`
import spray.http.MediaTypes._
import spray.routing._

class HttpConfigService extends Actor with RestService {

  def actorRefFactory = context

  def receive = runRoute(myRoute)
}


// this trait defines our service behavior independently from the service actor
trait RestService extends HttpService {
  val configReader: ConfigReader = new ConfigReader()
  val configwriter: ConfigWriter = new ConfigWriter()

  val myRoute =
    path("") {
      get {
        respondWithMediaType(`application/json`) { // XML is marshalled to `text/xml` by default, so we simply override here
          complete {
            "{ \n\t hello:world \n}"
          }
        }
      }
    } ~
    path("config" / """\w+""".r / "v" ~ DoubleNumber / """\w+""".r ) { (component, version, environment) =>
      get {
        respondWithMediaType(`application/json`) {
          complete {
            configReader.read(component, version, environment)
          }
        }
      } ~
      put {
        respondWithHeader(`Content-Type`(`application/json`)) {
          entity(as[String]) { data =>
            complete(configwriter.update(component, version, environment, data.toString))
          }
        }
      }
    }
}