package trackerserver;

import java.sql.DriverManager;
import java.sql.Connection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;
import java.text.ParseException;

class Database {
    Connection mConnection;
    Statement mStatement;

    Database(String databaseName, Boolean dropTable) throws IOException, SQLException {
        Files.createDirectories(Paths.get("data"));
        
        mConnection = DriverManager.getConnection("jdbc:sqlite:data/" + databaseName);

        mStatement = mConnection.createStatement();

        if (dropTable) {
            mStatement.executeUpdate("DROP TABLE IF EXISTS location_update");
        }

        mStatement.executeUpdate("CREATE TABLE IF NOT EXISTS location_update (tracker_id TEXT, timestamp TEXT, latitude REAL, longitude REAL)");
    }

    void insertLocationUpdate(LocationUpdate update) throws SQLException {
        mStatement.executeUpdate("INSERT INTO location_update VALUES ('" + update.mTrackerId + "', '" +
                                                                              update.mTimestamp + "', " +
                                                                              update.mLatitude + ", " +
                                                                              update.mLongitude + ")");
    }

    LocationUpdate getLatestLocationForTracker(String trackerId) throws SQLException {
        ResultSet rs = mStatement.executeQuery("SELECT * FROM location_update WHERE rowid = " +
                                               "(SELECT max(rowid) FROM location_update WHERE tracker_id = '" + trackerId + "')");

        LocationUpdate location = null;                                        

        if (rs.next()) {
            location = new LocationUpdate(rs.getString("tracker_id"), rs.getString("timestamp"), rs.getDouble("latitude"), rs.getDouble("longitude"));
        }

        return location;
    }

    static class LocationUpdate {
        String mTrackerId;
        String mTimestamp;
        double mLatitude;
        double mLongitude;

        LocationUpdate(String trackerId, String timestamp, double latitude, double longitude) {
            mTrackerId = trackerId;
            mTimestamp = timestamp;
            mLatitude = latitude;
            mLongitude = longitude;
        }

        static LocationUpdate fromString(String s) throws ParseException {
            if (s == null) throw new ParseException("Input string is null.", 0);

            String[] fields = s.split(",");

            if (fields.length != 6) throw new ParseException("Found " + fields.length + " comma separated fields. 6 are required.", 0);

            try {
                return new LocationUpdate(fields[0], fields[1], Double.parseDouble(fields[2]), Double.parseDouble(fields[3]));
            } catch (NumberFormatException e) {
                throw new ParseException("Geodetic coordinates failed to parse to Double", 0);
            }
        }
    }
}
