package com.example

import org.specs2.mutable.Specification
import se.aptitud.simpleconfig.RestService
import spray.testkit.Specs2RouteTest
import spray.http._
import StatusCodes._

class MyServiceSpec extends Specification with Specs2RouteTest with RestService {
  def actorRefFactory = system
  
  "MyService" should {
    "leave GET requests to other paths unhandled" in {
      Get("/kermit") ~> myRoute ~> check {
        handled must beFalse
      }
    }

    "return a config path for GET requests to the /config path" in {
      Get("/config/mycomponent/v1.0/test") ~> myRoute ~> check {
        responseAs[String] must contain("mycomponent/v1.0/test")
      }
    }

    "return a config path for GET requests to the /config path" in {
      Put("/config/mycomponent/v1.0/test"," { \"json\":\"value\" }") ~> myRoute ~> check {
        responseAs[String] must contain("\"active\" : \"true\"")
      }
    }

    "return a MethodNotAllowed error for PUT requests to the root path" in {
      Put() ~> sealRoute(myRoute) ~> check {
        status === MethodNotAllowed
        responseAs[String] === "HTTP method not allowed, supported methods: GET"
      }
    }
  }
}
