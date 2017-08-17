/**
 * Copyright (c) 2017 - 2017 Lukas Ciszewski <Lukas.Ciszewski@Gmail.com>
 */

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{ HttpMethods, HttpRequest, HttpResponse }
import spray.json.DefaultJsonProtocol

import scala.util.{ Failure, Success, Try }
import scala.language.implicitConversions

/**
 * Implicit conversions and marshalling helpers for [[Hotel]] instances.
 *
 * {{{
 *   import HotelProtocol._
 *   val lh: List[Hotel] = ExternalRequest
 * }}}
 */
object HotelProtocol extends SprayJsonSupport with DefaultJsonProtocol {

  /**
   * Converts a HotelRequest to its external hotel request counterpart
   *
   * @param r the HotelRequest to be converted
   * @return a list of ExternalHotelRequest objects
   */
  implicit def toExternalHotelRequestList(r: HotelRequest): List[ExternalHotelRequest] =
    List(
      ExternalHotelRequestA(r.name),
      ExternalHotelRequestB(r.name)
    )

  /**
   * Converts an ExternalHotelRequest to the request level (HttpRequest, ExternalHotelRequest) pair.
   *
   * @param r the ExternalHotelRequest to be converted
   * @return a request level representation of the request
   */
  implicit def toHttpRequest(r: ExternalHotelRequest): (HttpRequest, ExternalHotelRequest) = {
    r match {
      case r: ExternalHotelRequestA => (HttpRequest(HttpMethods.GET, "http://localhost:8081/hotels"), r)
      case r: ExternalHotelRequestB => (HttpRequest(HttpMethods.GET, "http://localhost:8082/hotels"), r)
    }
  }

  /**
   * Converts a httpClient Response to the appropriate List of Hotels
   *
   * @param r a tuple of a try of a HttpResponse and the ExternalRequest behind that
   * @return a List of Hotels extracted from the HttpResponse
   */
  implicit def toHotelList(r: (Try[HttpResponse], ExternalRequest)): List[Hotel] = {
    r match {
      case (Failure(f), ctx) => List(Hotel("failure"))
      case (Success(s), ctx) => {
        ctx match {
          case ctx: ExternalHotelRequestA => List(Hotel("Ext A"))
          case ctx: ExternalHotelRequestB => List(Hotel("Ext B"))
          case _ => List(Hotel("no such request"))
        }
      }
    }
  }

  /**
   * Converts a List of Hotels into a HotelResponse
   *
   * @param l a List of Hotels
   * @return a HotelResponse
   */
  implicit def toHotelResponse(l: List[Hotel]): HotelResponse = HotelResponse(l)

  /**
   * Marshalling from and to Json for the HotelResponse
   *
   * @return a JsonFormat[HotelResponse]
   */
  implicit def hotelFormat = jsonFormat1(Hotel)
  implicit def hotelResponseFormat = jsonFormat1(HotelResponse)
}
