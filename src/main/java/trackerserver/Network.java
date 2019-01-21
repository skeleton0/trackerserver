package trackerserver;

import fi.iki.elonen.NanoHTTPD;
import java.util.logging.Logger;

class Network extends NanoHTTPD {
    private static final Logger LOG = Logger.getLogger(Network.class.getName());

    Network(int port) {
        super(port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        LOG.info("Received connection from " + session.getRemoteHostName() + " (" + session.getRemoteIpAddress() + ")");

        return super.serve(session);
    }
}
