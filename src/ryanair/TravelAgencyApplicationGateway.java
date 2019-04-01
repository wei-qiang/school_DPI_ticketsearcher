package ryanair;

import static jms.Queues.bookingConfirmation;
import static jms.Queues.flightResponse;
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

  private RyanairApplication ryanairApplication;

  private MessageReceiverGateway ticketReceiver;
  private MessageSenderGateway ticketSender;
  private MessageReceiverGateway bookingReceiver;
  private MessageSenderGateway bookingSender;
  private Gson gson;

  public TravelAgencyApplicationGateway(RyanairApplication ryanairApplication) {
    this.ryanairApplication = ryanairApplication;

    this.ticketReceiver = new MessageReceiverGateway(ryanairTicket);
    this.ticketSender = new MessageSenderGateway(flightResponse);
    this.bookingReceiver = new MessageReceiverGateway(ryanairBooking);
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
        ryanairApplication.onTicketRequestArrived(ticketRequest, message.getJMSCorrelationID(),
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
        ryanairApplication.onBookingRequestArrived(bookingRequest, message.getJMSCorrelationID());
      } catch (JMSException e) {
        e.printStackTrace();
      }
    });
  }
}
