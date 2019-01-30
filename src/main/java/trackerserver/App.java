package trackerserver;

import java.util.logging.Logger;

class App {
    private static final Logger LOG = Logger.getLogger(App.class.getSimpleName());

    public static void main(String[] args) {
        Database database = null;
        Network network = null;

        try {
            database = new Database("tracker_server.db", false);

            network = new Network(46000);
            network.start();
        } catch (Exception e) {
            System.err.print("Caught exception during initialisation of database and network: ");
            System.err.println(e.getMessage());
        }

        while (true) {
            String update = network.takeNextUpdate();

            LOG.info("Received update: " + update);
            LOG.info("Parsing update to LocationUpdate...");

            try {
                database.insertLocationUpdate(LocationUpdate.fromString(update));
            } catch (Exception e) {
                LOG.warning(e.getMessage());
            }
        }
    }
}
