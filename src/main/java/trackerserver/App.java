package trackerserver;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

class App {
    private static final Logger LOG = Logger.getLogger(App.class.getSimpleName());

    public static void main(String[] args) {
        LOG.info("Starting up...");

        CountDownLatch shutdownLatch = new CountDownLatch(1);

        Runtime.getRuntime().addShutdownHook(new Thread(shutdownLatch::countDown));

        try {
            shutdownLatch.await();
        } catch (InterruptedException e) {
            LOG.info("Was interrupted while waiting on shutdown latch.");
            LOG.info("Received the following excuse for interruption: " + e.getMessage());
        }

        LOG.info("Shutting down...");
    }
}
