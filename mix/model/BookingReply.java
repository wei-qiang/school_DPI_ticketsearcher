package model;

public class BookingReply {

  private String confirmation;

  public BookingReply(String confirmation) {
    this.confirmation = confirmation;
  }

  public String getConfirmation() {
    return confirmation;
  }

  public void setConfirmation(String confirmation) {
    this.confirmation = confirmation;
  }

  @Override
  public String toString() {
    return "Booking: " +
        "confirmation= " + confirmation;
  }
}
