package jms;

public final class Queues {

  //travelagency - airline queues
  public static final String ryanairTicket = "RyanairTRQueue";

  public static final String klmTicket = "KLMTRQueue";

  public static final String ryanairBooking = "RyanairBRQueue";

  public static final String klmBooking = "KLMBRQueue";

  public static final String bookingConfirmation = "BookingConfirmationQueue";

  public static final String flightResponse = "FlightQueue";

  //ticketsearcher - travelagency
  public static final String bookingReplyQueue = "BookingReplyQueue";

  public static final String bookingRequestQueue = "BookingRequestQueue";

  public static final String ticketRequestQueue = "TicketRequestQueue";

  public static final String ticketReplyQueue = "TicketReplyQueue";
}
