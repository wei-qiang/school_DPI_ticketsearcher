package ryanair;

import java.util.Date;
import model.BookingReply;
import model.BookingRequest;
import model.TicketReply;
import model.TicketRequest;

public class RyanairApplication {

  private TravelAgencyApplicationGateway travelAgencyApplicationGateway;

  public RyanairApplication() {
    this.travelAgencyApplicationGateway = new TravelAgencyApplicationGateway(this);
  }

  public void sendTicketReply(TicketReply reply, String correlationId, int aggregationId) {
    travelAgencyApplicationGateway.sendTicketReply(reply, correlationId, aggregationId);
  }

  public void sendBookingReply(BookingReply reply, String correlationId) {
    travelAgencyApplicationGateway.sendBookingReply(reply, correlationId, 0);
  }

  public void onTicketRequestArrived(TicketRequest request, String correlationId,
      int aggregationId) {
    //ToDo look for available flights for request
    TicketReply reply = new TicketReply(100.00, new Date(), new Date(), 1, "ryanair");
    sendTicketReply(reply, correlationId, aggregationId);
  }

  public void onBookingRequestArrived(BookingRequest request, String correlationId) {
    //ToDo book flight
    sendBookingReply(new BookingReply("flight has been booked"), correlationId);
  }
}
