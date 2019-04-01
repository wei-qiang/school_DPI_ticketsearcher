package klm;


public class Main {

  public static void main(String[] args) {
    System.out.println("klm online");
    TravelAgencyApplicationGateway travelAgencyApplicationGateway = new TravelAgencyApplicationGateway(
        new KlmApplication());
  }
}
