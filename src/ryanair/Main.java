package ryanair;

public class Main{

  public static void main(String[] args) {
    System.out.println("ryanair online");
    TravelAgencyApplicationGateway travelAgencyApplicationGateway = new TravelAgencyApplicationGateway(
        new RyanairApplication());
  }
}
