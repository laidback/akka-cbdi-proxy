/**
 * Copyright (c) 2017 - 2017 Lukas Ciszewski <Lukas.Ciszewski@Gmail.com>
 */

/** Domain Object */
case class Hotel(name: String)

/** Internal Hotel Request */
case class HotelRequest(name: String)

/** External Request */
sealed trait ExternalHotelRequest extends ExternalRequest
case class ExternalHotelRequestA(name: String) extends ExternalHotelRequest
case class ExternalHotelRequestB(name: String) extends ExternalHotelRequest

/** External Responses */
sealed trait ExternalHotelResponse extends ExternalResponse
case class ExternalHotelResponseA(name: String) extends ExternalHotelResponse
case class ExternalHotelResponseB(name: String) extends ExternalHotelResponse

/** Internal Response */
case class HotelResponse(hotels: List[Hotel])
