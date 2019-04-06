package trackerserver;

import fi.iki.elonen.NanoHTTPD;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

class TrackerHttpServer extends NanoHTTPD {
    private static final Logger LOG = Logger.getLogger(TrackerHttpServer.class.getName());
    private Database mDb;

    TrackerHttpServer(int port, Database db) {
        super(port);

        mDb = db;
    }

    @Override
    public Response serve(IHTTPSession session) {
        LOG.info("Received connection from " + session.getRemoteHostName() + " (" + session.getRemoteIpAddress() + ")");

        Response badRequest = newFixedLengthResponse(Response.Status.BAD_REQUEST, null, null);

        if (session.getMethod() != Method.POST) {
            LOG.warning("Received HTTP method other than POST.");
            return badRequest;
        }

        try {
            Map<String, String> files = new HashMap<String, String>();
            session.parseBody(files);
            Database.LocationUpdate updateData = Database.LocationUpdate.fromString(files.get("postData"));
            mDb.insertLocationUpdate(updateData);
        } catch (Exception e) {
            LOG.warning("Parsing post data threw exception: " + e.getMessage());
            return badRequest;
        }

        return newFixedLengthResponse(Response.Status.OK, null, null);
    }
}
