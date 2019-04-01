package jms;

import java.util.Properties;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class MessageSenderGateway {
  private Connection connection; // to connect to the ActiveMQ
  private Session session; // session for creating messages, producers and

  private Destination sendDestination; // reference to a queue/topic destination
  private MessageProducer producer; // for sending messages

  public MessageSenderGateway(String destination) {
    try {
      Properties props = new Properties();
      props.setProperty(Context.INITIAL_CONTEXT_FACTORY,
          "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
      props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");

      // connect to the Destination called “myFirstChannel”
      // queue or topic: “queue.myFirstDestination” or “topic.myFirstDestination”
      props.put(("queue." + destination), destination);

      Context jndiContext = new InitialContext(props);
      ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext
          .lookup("ConnectionFactory");
      connection = connectionFactory.createConnection();
      session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

      // connect to the sender destination
      sendDestination = (Destination) jndiContext.lookup(destination);
      producer = session.createProducer(sendDestination);
    } catch (NamingException | JMSException e) {
      e.printStackTrace();
    }
  }

  public String sendMessage(String message, String correlationId, int aggregationId) {
    try {
      // create a object message
      Message msg = session.createTextMessage(message);
      if (!correlationId.equals("")) {
        msg.setJMSCorrelationID(correlationId);
      }
      if(aggregationId != 0){
        msg.setIntProperty("aggregationId", aggregationId);
      }
      // send the message
      producer.send(msg);
      return msg.getJMSMessageID();
    } catch (JMSException e) {
      e.printStackTrace();
    }
    return null;
  }
}
