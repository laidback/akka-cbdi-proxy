/**
 * Copyright (c) 2017 - 2017 Lukas Ciszewski <Lukas.Ciszewski@Gmail.com>
 */

import akka.actor._
import akka.stream._
import akka.http.scaladsl._
import com.typesafe.config.ConfigFactory
import scala.concurrent.Future
import scala.io.StdIn

/** Main application */
object Main extends App with HotelComponentImpl {
  implicit val config = ConfigFactory.load()
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  implicit val httpClient = Http().superPool[ExternalRequest]()

  val host = config.getString("jerry.http.host")
  val port = config.getInt("jerry.http.port")

  /** Start http server */
  val bindingFuture: Future[Http.ServerBinding] =
    Http().bindAndHandle(hotelRoute.route, host, port)

  /** Run the server */
  StdIn.readLine(s"Running on: $host:$port%nHit ENTER to quit!%n")

  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())

}
