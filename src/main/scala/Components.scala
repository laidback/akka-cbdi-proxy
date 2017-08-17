/**
 * Copyright (c) 2017 - 2017 Lukas Ciszewski <Lukas.Ciszewski@Gmail.com>
 */

import akka.stream.scaladsl.Flow

/** Jerry Components */
sealed trait Component
trait ModelComponent extends Component
trait ServiceComponent extends Component
trait RouteComponent extends Component

/** Basic application component */
trait AppComponent extends Component
  with ModelComponent
  with ServiceComponent
  with RouteComponent

/** Http Request and Response */
trait InternalRequest
trait InternalResponse
trait ExternalRequest
trait ExternalResponse

/** Default generic proxy flow */
object DefaultFlow {
  def out1[T, M](implicit f: T => List[M]) = Flow[T].mapConcat[M](f)
  def out2[T, M](implicit m: T => M) = Flow[T].map[M](i => i)
  def in2[T, M](implicit f: T => List[M]) = Flow[T].mapConcat[M](f)
  def in1[T, M](implicit m: List[T] => M) = Flow[T].fold(List[T]())(_ :+ _).map[M](i => i)
}
