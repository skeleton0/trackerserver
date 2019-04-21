package trackerserver;

import fi.iki.elonen.NanoHTTPD;
import jdk.nashorn.internal.parser.JSONParser;

import java.sql.SQLException;
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

        Database.LocationUpdate latestUpdate = null;

        try {
            latestUpdate = mDatabase.getLatestLocationForTracker(trackerId);
        } catch (SQLException e) {
            LOG.warning("Caught SQL exception: " + e.getMessage());
        }

        if (latestUpdate != null) {
            String jsonResponse = new StringBuilder().append("{\"t\":\"")
                                                     .append(latestUpdate.mTimestamp)
                                                     .append("\",\"la\":")
                                                     .append(latestUpdate.mLatitude)
                                                     .append(",\"lo\":")
                                                     .append(latestUpdate.mLongitude)
                                                     .append("}").toString();

            return newFixedLengthResponse(Response.Status.OK, "application/json", jsonResponse);
        }

        return newFixedLengthResponse(Response.Status.NOT_FOUND, null, null);
    }
}
