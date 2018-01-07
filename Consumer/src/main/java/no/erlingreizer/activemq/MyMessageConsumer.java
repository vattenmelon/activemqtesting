package no.erlingreizer.activemq;

import org.apache.qpid.jms.JmsConnectionFactory;

import javax.jms.*;

public class MyMessageConsumer {

    public MyMessageConsumer() {

        boolean useTransaction = false;

        String user = env("ACTIVEMQ_USER", "admin");
        String password = env("ACTIVEMQ_PASSWORD", "password");
        String host = env("ACTIVEMQ_HOST", "localhost");
        int port = Integer.parseInt(env("ACTIVEMQ_PORT", "5672"));

        String connectionURI = "amqp://" + host + ":" + port;
        ConnectionFactory connectionFactory = new JmsConnectionFactory(connectionURI);

        try (Connection connection = connectionFactory.createConnection();
             Session session = connection.createSession(useTransaction, Session.AUTO_ACKNOWLEDGE)) {

            Destination destination = session.createQueue("TEST.QUEUE");
            try (MessageConsumer consumer = session.createConsumer(destination)) {

                connection.start();
                Message message = consumer.receive(1000);
                System.out.println("melding mottatt: " + ((TextMessage)message).getText());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String args[]) {
        new MyMessageConsumer();
    }

    private static String env(String key, String defaultValue) {
        String rc = System.getenv(key);
        if (rc == null)
            return defaultValue;
        return rc;
    }
}
