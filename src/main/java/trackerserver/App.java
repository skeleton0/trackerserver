package trackerserver;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

class App {
    private static final Logger LOG = Logger.getLogger(App.class.getSimpleName());

    public static void main(String[] args) {
        CountDownLatch shutdownLatch = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread(shutdownLatch::countDown));

        Database db = null;
        TrackerHttpServer trackerServer = null;
        ClientHttpServer clientServer = null;

        try {
            db = new Database("tracker_server.db", false);

            trackerServer = new TrackerHttpServer(46000, db);
            trackerServer.makeSecure(trackerServer.makeSSLSocketFactory("/keystore.jks", "".toCharArray()), null);
            trackerServer.start();

            clientServer = new ClientHttpServer(47000, db);
            clientServer.makeSecure(trackerServer.makeSSLSocketFactory("/keystore.jks", "".toCharArray()), null);
            clientServer.start();
        } catch (Exception e) {
            LOG.info("Caught exception during initialisation: " + e.getMessage());
            shutdownLatch.countDown();
        }

        try {
            shutdownLatch.await();
        } catch (InterruptedException e) {
            LOG.info("Was interrupted while waiting on shutdown latch.");
            LOG.info("Received the following excuse for interruption: " + e.getMessage());
        }

        LOG.info("Waiting for existing connections to finish...");
        if (trackerServer != null) trackerServer.stop();
        if (clientServer != null) clientServer.stop();

        LOG.info("Shutting down.");
    }
}
