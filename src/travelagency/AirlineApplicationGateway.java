package travelagency;

import static jms.Queues.bookingConfirmation;
import static jms.Queues.flightResponse;
import static jms.Queues.klmBooking;
import static jms.Queues.ryanairBooking;

import com.google.gson.Gson;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import jms.MessageReceiverGateway;
import jms.MessageSenderGateway;
import model.BookingReply;
import model.BookingRequest;
import model.TicketReply;
import model.TicketRequest;
import travelagency.strategy.Strategyable;

public class AirlineApplicationGateway {

  private MessageReceiverGateway BookingReceiver;
  private MessageReceiverGateway FlightReceiver;

  private AirlineRecipientList airlineRecipientList;

  private Gson gson;

  public AirlineApplicationGateway(Strategyable recipientListStrategy) {
    this.BookingReceiver = new MessageReceiverGateway(bookingConfirmation);
    this.FlightReceiver = new MessageReceiverGateway(flightResponse);
    this.airlineRecipientList = new AirlineRecipientList(recipientListStrategy);
    this.gson = new Gson();

    onBookingReplyArrived();
    onTicketReplyArrived();
  }

  public int sendTicketRequest(TicketRequest request, String correlationId, int aggregationId) {
    return airlineRecipientList.sendToAirline(request, correlationId, aggregationId);
  }

  public String sendBookingRequestMessageRouter(BookingRequest request, String correlationId,
      int aggregationId) {
    switch (request.getAirline().toLowerCase()) {
      case "ryanair":
        return new MessageSenderGateway(ryanairBooking)
            .sendMessage(gson.toJson(request), correlationId, aggregationId);
      case "klm":
        return new MessageSenderGateway(klmBooking)
            .sendMessage(gson.toJson(request), correlationId, aggregationId);
      default:
        return "";
    }
  }

  private void onBookingReplyArrived() {
    BookingReceiver.setListener(message -> {
      TextMessage textMessage = (TextMessage) message;
      try {
        BookingReply bookingReply = gson.fromJson(textMessage.getText(), BookingReply.class);
        System.out.println("received bookingreply: " + bookingReply.toString());
        TravelAgencyApplicationSingleton.getInstance()
            .onBookingReplyArrived(bookingReply, message.getJMSCorrelationID());
      } catch (JMSException e) {
        e.printStackTrace();
      }
    });
  }

  private void onTicketReplyArrived() {
    FlightReceiver.setListener(message -> {
      TextMessage textMessage = (TextMessage) message;
      try {
        TicketReply ticketReply = gson.fromJson(textMessage.getText(), TicketReply.class);
        System.out.println("received ticketreply: " + ticketReply.toString());
        TravelAgencyApplicationSingleton.getInstance()
            .onTicketReplyArrived(ticketReply, textMessage.getJMSCorrelationID(),
                textMessage.getIntProperty("aggregationId"));
      } catch (JMSException e) {
        e.printStackTrace();
      }
    });
  }
}
