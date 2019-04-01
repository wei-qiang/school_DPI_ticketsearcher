package travelagency.strategy;

import static jms.Queues.klmTicket;
import static jms.Queues.ryanairTicket;

import java.util.ArrayList;
import java.util.List;
import model.TicketRequest;

public class ContinentFilterStrategy implements Strategyable {

  @Override
  public List<String> FilterInput(TicketRequest request) {
    List<String> filterOutput = new ArrayList<>();
    if (locateContinentofCity(request.getLocationFrom()).equals("europe")) {
      if (locateContinentofCity(request.getLocationTo()).equals("europe")) {
        filterOutput.add(ryanairTicket);
        filterOutput.add(klmTicket);
      } else if (locateContinentofCity(request.getLocationTo()).equals("asia")) {
        filterOutput.add(klmTicket);
      }
    } else if (locateContinentofCity(request.getLocationFrom()).equals("asia")
        && locateContinentofCity(request.getLocationTo()).equals("europe")) {
      filterOutput.add(klmTicket);
    }
    return filterOutput;
  }

  private String locateContinentofCity(String city) {
    //ToDo use an api to determine the continent of a city and return it
    switch (city.toLowerCase()) {
      case "amsterdam":
        return "europe";
      case "paris":
        return "europe";
      case "tokyo":
        return "asia";
      case "shanghai":
        return "asia";
      case "vancouver":
        return "north america";
      case "new york":
        return "north america";
      default:
        return "";
    }

  }
}
