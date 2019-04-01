package ticketsearcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import model.BookingReply;
import model.BookingRequest;
import model.RequestReply;
import model.TicketReply;
import model.TicketRequest;

public class TicketSearcherApplication {

  private TravelAgencyApplicationGateway travelAgencyApplicationGateway;
  private Controller controller;
  private List<RequestReply> requestReplies;

  private HashMap<String, TicketRequest> ticketRequestHashMap;
  private HashMap<String, BookingRequest> bookingRequestHashMap;

  public TicketSearcherApplication(Controller controller) {
    this.travelAgencyApplicationGateway = new TravelAgencyApplicationGateway(this);
    this.controller = controller;

    requestReplies = new ArrayList<>();
    ticketRequestHashMap = new HashMap<>();
    bookingRequestHashMap = new HashMap<>();
  }

  public String sendTicketRequest(TicketRequest request) {
    requestReplies.add(new RequestReply<TicketRequest, TicketReply>(request, null));
    return travelAgencyApplicationGateway.sendTicketRequest(request, "", 0);
  }

  public String sendBookingRequest(RequestReply ticketRequestReply, BookingRequest bookingRequest) {
    replaceTicketRequestRepliesWithBookingRequest(ticketRequestReply, bookingRequest);
    return travelAgencyApplicationGateway.sendBookingRequest(bookingRequest, "", 0);
  }

  private void replaceTicketRequestRepliesWithBookingRequest(RequestReply ticketRequestReplies,
      BookingRequest bookingRequest) {
    boolean indexToInsertBookingFound = false;
    for (int i = -1; i < requestReplies.size() - 1; i++) {
      if (indexToInsertBookingFound) {
        if (requestReplies.get(i + 1).getRequest().equals(ticketRequestReplies.getRequest())) {
          requestReplies.remove(i + 1);
        }
      } else {
        if (requestReplies.get(i + 1).getRequest().equals(ticketRequestReplies.getRequest())) {
          indexToInsertBookingFound = true;
          requestReplies.set(i + 1, new RequestReply<>(bookingRequest, null));
        }
      }
    }
  }

  public void onTicketRepliesArrived(List<TicketReply> ticketReplies, String correlationId) {
    int indexRequestReply = requestReplies
        .indexOf(getRequestReply(ticketRequestHashMap.get(correlationId)));

    for (int i = 0; i < ticketReplies.size(); i++) {
      if (i != 0) {
        requestReplies.add(indexRequestReply,
            new RequestReply<>(ticketRequestHashMap.get(correlationId), ticketReplies.get(i)));
      } else {
        requestReplies.set(indexRequestReply,
            new RequestReply<>(ticketRequestHashMap.get(correlationId), ticketReplies.get(i)));
      }
    }

    controller.onRepliesArrived(requestReplies);
  }

  public void onBookingReplyArrived(BookingReply bookingReply, String correlationId) {
    //Todo
    int indexRequestReply = requestReplies
        .indexOf(getRequestReply(bookingRequestHashMap.get(correlationId)));

    requestReplies.set(indexRequestReply,
        new RequestReply<>(bookingRequestHashMap.get(correlationId), bookingReply));

    controller.onRepliesArrived(requestReplies);
  }

  /**
   * This method returns the RequestReply line that belongs to the request from requestReplyList
   * (JList). You can call this method when an reply arrives in order to add this reply to the right
   * request in requestReplyList.
   */
  private RequestReply<TicketRequest, TicketReply> getRequestReply(TicketRequest request) {

    for (int i = 0; i < requestReplies.size(); i++) {
      RequestReply<TicketRequest, TicketReply> rr = requestReplies.get(i);
      if (rr.getRequest() == request) {
        return rr;
      }
    }

    return null;
  }

  private RequestReply<BookingRequest, BookingReply> getRequestReply(BookingRequest request) {

    for (int i = 0; i < requestReplies.size(); i++) {
      RequestReply<BookingRequest, BookingReply> rr = requestReplies.get(i);
      if (rr.getRequest() == request) {
        return rr;
      }
    }
    return null;
  }

  public HashMap<String, TicketRequest> getTicketRequestHashMap() {
    return ticketRequestHashMap;
  }

  public void setTicketRequestHashMap(
      HashMap<String, TicketRequest> ticketRequestHashMap) {
    this.ticketRequestHashMap = ticketRequestHashMap;
  }

  public HashMap<String, BookingRequest> getBookingRequestHashMap() {
    return bookingRequestHashMap;
  }

  public void setBookingRequestHashMap(
      HashMap<String, BookingRequest> bookingRequestHashMap) {
    this.bookingRequestHashMap = bookingRequestHashMap;
  }

  public List<RequestReply> getRequestReplies() {
    return requestReplies;
  }


}
