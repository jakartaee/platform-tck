package org.jboss.cdi.tck.tests.context.jms;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;

import org.jboss.cdi.tck.util.SimpleLogger;
public class AbstractMessageListener implements MessageListener {

    public static AtomicInteger processedMessages = new AtomicInteger(0);
    private static final SimpleLogger simpleLogger = new SimpleLogger(AbstractMessageListener.class);
    private static AtomicBoolean initialized = new AtomicBoolean();

    @Inject
    private LoggerService loggerService;

    @Override
    public void onMessage(Message message) {

        if (message instanceof TextMessage) {
            try {
                loggerService.log(((TextMessage) message).getText());
            } catch (JMSException e) {
                simpleLogger.log(e);
            } finally {
                processedMessages.incrementAndGet();
            }
        } else {
            throw new IllegalArgumentException("Unsupported message type");
        }
    }

    public static void resetProcessedMessages() {
        processedMessages.set(0);
    }

    public static boolean isInitialized() {
        return initialized.get();
    }

    @PostConstruct
    public void postConstruct() {
        initialized.set(true);
    }



}
