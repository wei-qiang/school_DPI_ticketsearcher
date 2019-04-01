package travelagency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import model.TicketReply;

public class AggregatorSingleton {

  private static AggregatorSingleton INSTANCE;

  private int aggregatorIdCount;

  private HashMap<Integer, Integer> idTotalCountMap;
  private HashMap<Integer, Integer> idCountMap;

  private List<HashMap<Integer, TicketReply>> ticketReplies;

  private AggregatorSingleton() {
    idTotalCountMap = new HashMap<>();
    idCountMap = new HashMap<>();
    ticketReplies = new ArrayList<>();
    this.aggregatorIdCount = 0;
  }

  public static AggregatorSingleton getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new AggregatorSingleton();
    }
    return INSTANCE;
  }

  public int getAggregatorId() {
    ++aggregatorIdCount;
    return aggregatorIdCount;
  }

  public void registerRequest(int aggregatorId, int count) {
    idTotalCountMap.put(aggregatorId, count);
    idCountMap.put(aggregatorId, 0);
  }

  public boolean addReply(int aggregatorId, TicketReply reply) {
    //update count
    int currentCount = idCountMap.get(aggregatorId);
    idCountMap.replace(aggregatorId, ++currentCount);

    //add reply to list
    HashMap<Integer, TicketReply> hashMap = new HashMap<>();
    hashMap.put(aggregatorId, reply);
    ticketReplies.add(hashMap);

    //check if count is equal to totalcount
    return idCountMap.get(aggregatorId).equals(idTotalCountMap.get(aggregatorId));
  }

  public List<TicketReply> getReplies(int aggregatorId) {
    idTotalCountMap.remove(aggregatorId);
    idCountMap.remove(aggregatorId);
    List<TicketReply> replies = new ArrayList<>();

    //iterate through list and if the id's are the same it will delete it from the list and add it to the responselist
    Iterator iterator = ticketReplies.iterator();
    while (iterator.hasNext()) {
      HashMap hashMap = (HashMap) iterator.next();
      if (hashMap.get(aggregatorId) != null) {
        replies.add((TicketReply) hashMap.get(aggregatorId));
        iterator.remove();
      }
    }
    return replies;
  }
}
