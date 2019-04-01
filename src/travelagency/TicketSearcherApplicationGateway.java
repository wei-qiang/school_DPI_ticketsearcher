package travelagency;

import static jms.Queues.bookingReplyQueue;
import static jms.Queues.bookingRequestQueue;
import static jms.Queues.ticketReplyQueue;
import static jms.Queues.ticketRequestQueue;

import com.google.gson.Gson;
import java.util.List;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import jms.MessageReceiverGateway;
import jms.MessageSenderGateway;
import model.BookingReply;
import model.BookingRequest;
import model.TicketReply;
import model.TicketRequest;
import ticketsearcher.TravelAgencyApplicationGateway;

public class TicketSearcherApplicationGateway {

  private MessageReceiverGateway ticketReceiver;
  private MessageSenderGateway ticketSender;
  private MessageReceiverGateway bookingReceiver;
  private MessageSenderGateway bookingSender;
  private Gson gson;

  public TicketSearcherApplicationGateway() {
    this.ticketReceiver = new MessageReceiverGateway(ticketRequestQueue);
    this.ticketSender = new MessageSenderGateway(ticketReplyQueue);
    this.bookingReceiver = new MessageReceiverGateway(bookingRequestQueue);
    this.bookingSender = new MessageSenderGateway(bookingReplyQueue);
    this.gson = new Gson();

    onTicketRequestArrived();
    onBookingRequestArrived();
  }

  public String sendTicketReply(List<TicketReply> replies, String correlationId,
      int aggregationId) {
    return ticketSender.sendMessage(gson.toJson(replies), correlationId, aggregationId);
  }

  public String sendBookingReply(BookingReply reply, String correlationId, int aggregationId) {
    return bookingSender.sendMessage(gson.toJson(reply), correlationId, aggregationId);
  }

  public void onTicketRequestArrived() {
    ticketReceiver.setListener(new MessageListener() {
      @Override
      public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
          TicketRequest ticketRequest = gson.fromJson(textMessage.getText(), TicketRequest.class);
          TravelAgencyApplicationSingleton.getInstance()
              .onTicketRequestArrived(ticketRequest, message.getJMSMessageID());
          System.out.println("received ticket request: " + ticketRequest.toString());
        } catch (JMSException e) {
          e.printStackTrace();
        }
      }
    });
  }

  public void onBookingRequestArrived() {
    bookingReceiver.setListener(new MessageListener() {
      @Override
      public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
          BookingRequest bookingRequest = gson
              .fromJson(textMessage.getText(), BookingRequest.class);
          System.out.println("received ticket request: " + bookingRequest.toString());
          TravelAgencyApplicationSingleton.getInstance()
              .onBookingRequestArrived(bookingRequest, message.getJMSMessageID());
        } catch (JMSException e) {
          e.printStackTrace();
        }
      }
    });
  }
}
