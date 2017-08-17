/**
 * Copyright (c) 2017 - 2017 Lukas Ciszewski <Lukas.Ciszewski@Gmail.com>
 */

trait HotelComponent extends AppComponent
  with HotelServiceComponent
  with HotelRouteComponent

trait HotelComponentImpl extends HotelComponent
  with HotelServiceComponentImpl
  with HotelRouteComponentImpl
