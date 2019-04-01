package model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TicketRequest {

  private int userId;
  private String locationFrom;
  private String locationTo;
  private Date date;

  public TicketRequest(int userId, String locationFrom, String locationTo, Date date) {
    this.userId = userId;
    this.locationFrom = locationFrom;
    this.locationTo = locationTo;
    this.date = date;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getLocationFrom() {
    return locationFrom;
  }

  public void setLocationFrom(String locationFrom) {
    this.locationFrom = locationFrom;
  }

  public String getLocationTo() {
    return locationTo;
  }

  public void setLocationTo(String locationTo) {
    this.locationTo = locationTo;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  @Override
  public String toString() {
    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

    return "Request from " +
        "user:" + userId +
        " for: " + locationFrom +
        " to: " + locationTo +
        " on " + format.format(date);
  }
}
