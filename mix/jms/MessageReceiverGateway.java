package jms;

import java.util.Properties;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


public class MessageReceiverGateway {

  Connection connection; // to connect to the JMS
  Session session; // session for creating consumers

  Destination receiveDestination; //reference to a queue/topic destination
  MessageConsumer consumer = null; // for receiving messages

  public MessageReceiverGateway(String destination) {
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

      // connect to the receiver destination
      receiveDestination = (Destination) jndiContext.lookup(destination);
      consumer = session.createConsumer(receiveDestination);

      connection.start(); // this is needed to start receiving messages

    } catch (NamingException | JMSException e) {
      e.printStackTrace();
    }
  }

  public void setListener(MessageListener messageListener) {
    try {
      consumer.setMessageListener(messageListener);
    } catch (JMSException e) {
      e.printStackTrace();
    }
  }
}
