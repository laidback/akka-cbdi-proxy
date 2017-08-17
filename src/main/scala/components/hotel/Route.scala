/**
 * Copyright (c) 2017 - 2017 Lukas Ciszewski <Lukas.Ciszewski@Gmail.com>
 */

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl._

/** Hotel Route component */
trait HotelRouteComponent extends RouteComponent {
  /** Require a [[HotelServiceComponent]] to be available in the client code */
  this: HotelServiceComponent =>

  /** Declare a member which is provided to the client */
  val hotelRoute: HotelRoute

  /** Internal [[HotelRoute]] interface trait of the component */
  trait HotelRoute {

    /** Abstract member of the [[HotelRoute]] */
    val route: Route
  }
}

/** Hotel Route component implementation */
trait HotelRouteComponentImpl extends HotelRouteComponent {
  /** Require a [[HotelServiceComponent]] to be available in the clinet code */
  this: HotelServiceComponent =>

  /** Instantiate a specific [[HotelRoute]] implementation */
  override val hotelRoute: HotelRoute = new HotelRouteImpl

  /** Implementation of the [[HotelRoute]] interface trait */
  private class HotelRouteImpl extends HotelRoute {
    import HotelProtocol._

    val route = {
      path("hotels") {
        get {
          parameters('name.as[String] ? "default").as(HotelRequest.apply) { req =>
            val graph = Source
              .single(req)
              .via(hotelService.getHotels)
              .toMat(Sink.head)(Keep.right)

            complete(graph.run())
          }
        }
      }
    }
  }
}
