package trackerserver;

import fi.iki.elonen.NanoHTTPD;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

class Network extends NanoHTTPD {
    private static final Logger LOG = Logger.getLogger(Network.class.getName());
    private final ArrayBlockingQueue<String> mTrackerUpdates = new ArrayBlockingQueue<String>(10);

    Network(int port) {
        super(port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        LOG.info("Received connection from " + session.getRemoteHostName() + " (" + session.getRemoteIpAddress() + ")");

        Map<String, String> files = new HashMap<String, String>();

        try {
            session.parseBody(files);
        } catch (Exception e) {
            System.err.println("parseBody threw an Exception.");
            System.err.println(e.getMessage());
        }

        String value = files.get("postData");
        if (value != null) {
            while (true) {
                try {
                    mTrackerUpdates.put(value);
                    break;
                } catch (InterruptedException e) {
                    System.err.println(e.getMessage());
                }
            }
        }

        return super.serve(session);
    }

    String takeNextUpdate() {
        while (true) {
            try {
                return mTrackerUpdates.take();
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
