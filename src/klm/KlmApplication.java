package klm;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import model.BookingReply;
import model.BookingRequest;
import model.TicketReply;
import model.TicketRequest;

public class KlmApplication {

  private TravelAgencyApplicationGateway travelAgencyApplicationGateway;

  public KlmApplication() {
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
    TicketReply reply = new TicketReply(100.00, new Date(), new Date(), 1, "klm");

    //INSERT WAIT TIME
    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    sendTicketReply(reply, correlationId, aggregationId);

  }

  public void onBookingRequestArrived(BookingRequest request, String correlationId) {
    //ToDo book flight
    sendBookingReply(new BookingReply("flight has been booked"), correlationId);
  }
}
