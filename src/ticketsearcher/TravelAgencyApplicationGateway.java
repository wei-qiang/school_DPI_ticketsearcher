package ticketsearcher;

import static jms.Queues.bookingReplyQueue;
import static jms.Queues.bookingRequestQueue;
import static jms.Queues.ticketReplyQueue;
import static jms.Queues.ticketRequestQueue;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

public class TravelAgencyApplicationGateway {

  private TicketSearcherApplication ticketSearcherApplication;

  private MessageReceiverGateway ticketReceiver;
  private MessageSenderGateway ticketSender;
  private MessageReceiverGateway bookingReceiver;
  private MessageSenderGateway bookingSender;
  private Gson gson;

  public TravelAgencyApplicationGateway(TicketSearcherApplication ticketSearcherApplication) {
    this.ticketSearcherApplication = ticketSearcherApplication;

    this.ticketReceiver = new MessageReceiverGateway(ticketReplyQueue);
    this.ticketSender = new MessageSenderGateway(ticketRequestQueue);
    this.bookingReceiver = new MessageReceiverGateway(bookingReplyQueue);
    this.bookingSender = new MessageSenderGateway(bookingRequestQueue);
    this.gson = new Gson();

    onBookingReplyArrived();
    onTicketRepliesArrived();
  }

  public String sendTicketRequest(TicketRequest request, String correlationId, int aggregationId) {
    return ticketSender.sendMessage(gson.toJson(request), correlationId, aggregationId);
  }

  public String sendBookingRequest(BookingRequest request, String correlationId,
      int aggregationId) {
    return bookingSender.sendMessage(gson.toJson(request), correlationId, aggregationId);
  }

  private void onTicketRepliesArrived() {
    ticketReceiver.setListener(new MessageListener() {
      @Override
      public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
          List<TicketReply> ticketReplies = gson
              .fromJson(textMessage.getText(), new TypeToken<List<TicketReply>>() {
              }.getType());
          for (TicketReply reply : ticketReplies
          ) {
            System.out.println("received ticketreply: " + reply.toString());
          }
          ticketSearcherApplication
              .onTicketRepliesArrived(ticketReplies, message.getJMSCorrelationID());
        } catch (JMSException e) {
          e.printStackTrace();
        }
      }
    });
  }

  private void onBookingReplyArrived() {
    bookingReceiver.setListener(new MessageListener() {
      @Override
      public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
          BookingReply bookingReply = gson.fromJson(textMessage.getText(), BookingReply.class);
          System.out.println("received booking reply: " + bookingReply.toString());
          ticketSearcherApplication
              .onBookingReplyArrived(bookingReply, message.getJMSCorrelationID());
        } catch (JMSException e) {
          e.printStackTrace();
        }
      }
    });
  }
}
