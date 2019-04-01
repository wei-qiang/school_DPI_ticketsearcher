package model;

public class RequestReply<REQUEST, REPLY> {

  private REQUEST request;
  private REPLY reply;

  public RequestReply(REQUEST request, REPLY reply) {
    setRequest(request);
    setReply(reply);
  }

  public REQUEST getRequest() {
    return request;
  }

  private void setRequest(REQUEST request) {
    this.request = request;
  }

  public REPLY getReply() {
    return reply;
  }

  public void setReply(REPLY reply) {
    this.reply = reply;
  }

  @Override
  public String toString() {
    return request.toString() + "  --->  " + ((reply != null) ? reply.toString()
        : "waiting for reply...");
  }

}
