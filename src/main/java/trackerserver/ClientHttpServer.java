package trackerserver;

import fi.iki.elonen.NanoHTTPD;

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

        String trackerId = session.getUri().replace("/", "");
        LOG.info("Received position request for tracker ID " + trackerId);

        return newFixedLengthResponse(Response.Status.NOT_IMPLEMENTED, "text/plain", "go away bls");
    }
}
