package trackerserver;

import fi.iki.elonen.NanoHTTPD;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

class ClientHttpServer extends NanoHTTPD {
    private static final Logger LOG = Logger.getLogger(ClientHttpServer.class.getName());
    private Database mDatabase;

    ClientHttpServer(int port, Database database) {
        super(port);

        mDatabase = database;
    }

    @Override
    public Response serve(IHTTPSession session) {
        LOG.info("Received request from " + session.getRemoteIpAddress());

        try {
            URI requestURI = new URI(session.getUri());
            String trackerId = requestURI.getPath();

            LOG.info("Received position request for tracker ID " + trackerId);
        } catch (URISyntaxException e) {
            LOG.warning("Failed to parse URI: " + e.getMessage());
        }

        return newFixedLengthResponse(Response.Status.NOT_IMPLEMENTED, "text/plain", "go away bls");
    }
}
