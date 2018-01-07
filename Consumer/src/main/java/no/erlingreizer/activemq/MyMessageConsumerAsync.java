package no.erlingreizer.activemq;

import org.apache.qpid.jms.JmsConnectionFactory;

import javax.jms.*;

public class MyMessageConsumerAsync implements MessageListener {

    public MyMessageConsumerAsync() {

        boolean useTransaction = false;

        String user = env("ACTIVEMQ_USER", "admin");
        String password = env("ACTIVEMQ_PASSWORD", "password");
        String host = env("ACTIVEMQ_HOST", "localhost");
        int port = Integer.parseInt(env("ACTIVEMQ_PORT", "5672"));

        String connectionURI = "amqp://" + host + ":" + port;
        ConnectionFactory connectionFactory = new JmsConnectionFactory(connectionURI);

        try (Connection connection = connectionFactory.createConnection();
             Session session = connection.createSession(useTransaction, Session.AUTO_ACKNOWLEDGE)) {

            connection.start();

            Destination destination = session.createQueue("TEST.QUEUE");
            MessageConsumer consumer = session.createConsumer(destination);

            consumer.setMessageListener(this);

            //if the sleep is outside the try it will not work
            //because connection etc will be closed
            while(true){
                Thread.sleep(1000);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void main(String args[]) {
        new MyMessageConsumerAsync();
    }

    private static String env(String key, String defaultValue) {
        String rc = System.getenv(key);
        if (rc == null)
            return defaultValue;
        return rc;
    }

    @Override
    public void onMessage(Message message) {
        System.out.println("-------------------------------------");
        System.out.println("msg: " + message);
        if (message instanceof TextMessage) {
            try {
                System.out.println("melding mottatt: " + ((TextMessage) message).getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
