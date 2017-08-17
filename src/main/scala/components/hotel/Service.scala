/**
 * Copyright (c) 2017 - 2017 Lukas Ciszewski <Lukas.Ciszewski@Gmail.com>
 */

import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ HttpRequest, HttpResponse }
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{ BidiFlow, Flow }
import com.typesafe.config.Config

import scala.util.Try

/** components.hotel.Hotel Service component interface */
trait HotelServiceComponent extends ServiceComponent {

  /** Declares implicit dependencies needed to attach to the actor system */
  implicit val config: Config
  implicit val system: ActorSystem
  implicit val materializer: ActorMaterializer

  /** Declare the dependency on a certain type of httpClient */
  implicit def httpClient: Flow[(HttpRequest, ExternalRequest), (Try[HttpResponse], ExternalRequest), NotUsed]

  /** Declare a member which is provided to the client */
  val hotelService: HotelService

  /** Internal [[HotelService]] interface trait of the component */
  trait HotelService {

    /** Abstract method creating components.hotel.HotelResponse based on a given components.hotel.HotelRequest */
    def getHotels: Flow[HotelRequest, HotelResponse, NotUsed]
  }
}

/** components.hotel.Hotel Service component implementation */
trait HotelServiceComponentImpl extends HotelServiceComponent {

  /** Instantiate a specific [[HotelService]] implementation */
  override val hotelService: HotelService = new HotelServiceImpl

  /** Implementation of the [[HotelService]] interface trait */
  private class HotelServiceImpl extends HotelService {
    import HotelProtocol._

    /** Wiring the default proxy flow with the domain objects. */
    val out1 = DefaultFlow.out1[HotelRequest, ExternalHotelRequest]
    val out2 = DefaultFlow.out2[ExternalHotelRequest, (HttpRequest, ExternalHotelRequest)]
    val in2 = DefaultFlow.in2[(Try[HttpResponse], ExternalRequest), Hotel]
    val in1 = DefaultFlow.in1[Hotel, HotelResponse]

    val internal = BidiFlow.fromFlows(out1, in1)
    val external = BidiFlow.fromFlows(out2, in2)

    /** Implementation of the [[HotelService.getHotels]] method */
    lazy val getHotels = internal atop external join httpClient
  }
}
