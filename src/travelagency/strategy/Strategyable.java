package travelagency.strategy;

import java.util.List;
import model.TicketRequest;

public interface Strategyable {

  List<String> FilterInput(TicketRequest request);
}
