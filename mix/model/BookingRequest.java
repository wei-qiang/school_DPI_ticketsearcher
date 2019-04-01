package model;

public class BookingRequest {

  private int userId;
  private int flightId;
  private String airline;

  public BookingRequest(int userId, int flightId, String airline) {
    this.userId = userId;
    this.flightId = flightId;
    this.airline = airline;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
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
    return "Booking flight: " + flightId +
        " by airline: " + airline +
        " for user: " + userId;
  }
}
