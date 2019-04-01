package travelagency;

import java.util.List;
import model.BookingReply;
import model.BookingRequest;
import model.TicketReply;
import model.TicketRequest;
import travelagency.strategy.ContinentFilterStrategy;

public class TravelAgencyApplicationSingleton {

  private static TravelAgencyApplicationSingleton INSTANCE;

  private TicketSearcherApplicationGateway ticketSearcherApplicationGateway;
  private AirlineApplicationGateway airlineApplicationGateway;

  private TravelAgencyApplicationSingleton() {
    ticketSearcherApplicationGateway = new TicketSearcherApplicationGateway();
    airlineApplicationGateway = new AirlineApplicationGateway(new ContinentFilterStrategy());
  }

  public static TravelAgencyApplicationSingleton getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new TravelAgencyApplicationSingleton();
    }
    return INSTANCE;
  }

  public void sendTicketRequestToAirlines(TicketRequest request, String correlationId) {
    int aggregationId = AggregatorSingleton.getInstance().getAggregatorId();
    AggregatorSingleton.getInstance().registerRequest(aggregationId,
        airlineApplicationGateway.sendTicketRequest(request, correlationId, aggregationId));
  }

  public void sendBookingRequestToAirline(BookingRequest request, String correlationId) {
    airlineApplicationGateway.sendBookingRequestMessageRouter(request, correlationId, 0);
  }

  public void sendBookingReply(BookingReply reply, String correlationId) {
    ticketSearcherApplicationGateway.sendBookingReply(reply, correlationId, 0);
  }

  public void sendTicketReplies(List<TicketReply> ticketReplies, String correlationId) {
    ticketSearcherApplicationGateway.sendTicketReply(ticketReplies, correlationId, 0);
  }

  public void onTicketRequestArrived(TicketRequest request, String correlationId) {
    sendTicketRequestToAirlines(request, correlationId);
  }

  public void onTicketReplyArrived(TicketReply reply, String correlationId, int aggregationId) {
    if (AggregatorSingleton.getInstance().addReply(aggregationId, reply)) {
      sendTicketReplies(AggregatorSingleton.getInstance().getReplies(aggregationId), correlationId);
    }
  }

  public void onBookingRequestArrived(BookingRequest request, String correlationId) {
    sendBookingRequestToAirline(request, correlationId);
  }

  public void onBookingReplyArrived(BookingReply reply, String correlationId) {
    sendBookingReply(reply, correlationId);
  }


}
