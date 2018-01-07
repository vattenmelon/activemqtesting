package no.erlingreizer.activemq;

import org.apache.qpid.jms.JmsConnectionFactory;

import javax.jms.*;

public class MyMessageProducer {

    public MyMessageProducer() {

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
            try (MessageProducer producer = session.createProducer(destination)) {

                connection.start();
                Message message = session.createTextMessage("this is a test");
                producer.send(message);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String args[]) {
        new MyMessageProducer();
    }

    private static String env(String key, String defaultValue) {
        String rc = System.getenv(key);
        if (rc == null)
            return defaultValue;
        return rc;
    }
}
