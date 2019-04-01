package travelagency;

import com.google.gson.Gson;
import java.util.List;
import jms.MessageSenderGateway;
import model.TicketRequest;
import travelagency.strategy.Strategyable;

public class AirlineRecipientList {

  private Strategyable filterStrategy;
  private Gson gson;

  public AirlineRecipientList(Strategyable strategy) {
    filterStrategy = strategy;
    gson = new Gson();
  }

  public int sendToAirline(TicketRequest request, String correlationId, int aggregationId) {
    List<String> airlineQueues = filterStrategy.FilterInput(request);

    for (String queue : airlineQueues
    ) {
      MessageSenderGateway gateway = new MessageSenderGateway(queue);
      gateway.sendMessage(gson.toJson(request), correlationId, aggregationId);
    }
    return airlineQueues.size();
  }
}

