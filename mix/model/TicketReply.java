package model;

import java.util.Date;

public class TicketReply {

  private double price;
  private Date departure;
  private Date arrival;
  private int flightId;
  private String airline;

  public TicketReply(double price, Date departure, Date arrival, int flightId, String airline) {
    this.price = price;
    this.departure = departure;
    this.arrival = arrival;
    this.flightId = flightId;
    this.airline = airline;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public Date getDeparture() {
    return departure;
  }

  public void setDeparture(Date departure) {
    this.departure = departure;
  }

  public Date getArrival() {
    return arrival;
  }

  public void setArrival(Date arrival) {
    this.arrival = arrival;
  }

  public int getFlightId() {
    return flightId;
  }

  public void setFlightId(int flightId) {
    this.flightId = flightId;
  }

  public String getAirline() {
    return airline;
  }

  public void setAirline(String airline) {
    this.airline = airline;
  }

  @Override
  public String toString() {
    return "Flight: " + flightId +
        " price=" + price +
        ", airline= " + airline;
  }
}
