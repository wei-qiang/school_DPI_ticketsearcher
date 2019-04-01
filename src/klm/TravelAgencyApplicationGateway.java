package klm;

import static jms.Queues.bookingConfirmation;
import static jms.Queues.flightResponse;
import static jms.Queues.klmBooking;
import static jms.Queues.klmTicket;
import static jms.Queues.ryanairBooking;
import static jms.Queues.ryanairTicket;

import com.google.gson.Gson;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import jms.MessageReceiverGateway;
import jms.MessageSenderGateway;
import model.BookingReply;
import model.BookingRequest;
import model.TicketReply;
import model.TicketRequest;

public class TravelAgencyApplicationGateway {

  private KlmApplication klmApplication;

  private MessageReceiverGateway ticketReceiver;
  private MessageSenderGateway ticketSender;
  private MessageReceiverGateway bookingReceiver;
  private MessageSenderGateway bookingSender;
  private Gson gson;

  public TravelAgencyApplicationGateway(KlmApplication klmApplication) {
    this.klmApplication = klmApplication;

    this.ticketReceiver = new MessageReceiverGateway(klmTicket);
    this.ticketSender = new MessageSenderGateway(flightResponse);
    this.bookingReceiver = new MessageReceiverGateway(klmBooking);
    this.bookingSender = new MessageSenderGateway(bookingConfirmation);
    this.gson = new Gson();

    onBookingRequestArrived();
    onTicketRequestArrived();
  }

  public String sendTicketReply(TicketReply reply, String correlationId, int aggregationId) {
    return ticketSender.sendMessage(gson.toJson(reply), correlationId, aggregationId);
  }

  public String sendBookingReply(BookingReply reply, String correlationId, int aggregationId) {
    return bookingSender.sendMessage(gson.toJson(reply), correlationId, aggregationId);
  }

  private void onTicketRequestArrived() {
    ticketReceiver.setListener(message -> {
      TextMessage textMessage = (TextMessage) message;
      try {
        TicketRequest ticketRequest = gson.fromJson(textMessage.getText(), TicketRequest.class);
        System.out.println("received ticketrequest: " + ticketRequest.toString());
        klmApplication.onTicketRequestArrived(ticketRequest, message.getJMSCorrelationID(),
            message.getIntProperty("aggregationId"));
      } catch (JMSException e) {
        e.printStackTrace();
      }
    });
  }

  private void onBookingRequestArrived() {
    bookingReceiver.setListener(message -> {
      TextMessage textMessage = (TextMessage) message;
      try {
        BookingRequest bookingRequest = gson.fromJson(textMessage.getText(), BookingRequest.class);
        System.out.println("received bookingrequest: " + bookingRequest.toString());
        klmApplication.onBookingRequestArrived(bookingRequest, message.getJMSCorrelationID());
      } catch (JMSException e) {
        e.printStackTrace();
      }
    });
  }
}
