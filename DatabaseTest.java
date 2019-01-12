import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import java.sql.SQLException;
import java.io.IOException;

public class DatabaseTest {
    static Database mDatabase;

    @BeforeClass
    public static void setUpClass() throws SQLException, IOException {
        mDatabase = new Database("tracker_server_test.db", true);
    }

    @Test
    public void testInsertLocationUpdate() throws SQLException {
        mDatabase.insertLocationUpdate(new LocationUpdate("1337", "2018-01-12T19:18", 0, 0));

        LocationUpdate latestLoc = mDatabase.getLatestLocationForTracker("1337");

        assertNotNull("Returned LocationUpdate object is null", latestLoc);
    }

    @Test
    public void testGetLatestLocationForTracker() throws SQLException {
        mDatabase.insertLocationUpdate(new LocationUpdate("0", "2018-01-12T19:18", 0, 0));
        mDatabase.insertLocationUpdate(new LocationUpdate("1", "2018-01-12T19:18", 0, 0));
        mDatabase.insertLocationUpdate(new LocationUpdate("0", "2018-01-13T19:18", 0, 0));
        mDatabase.insertLocationUpdate(new LocationUpdate("0", "2018-01-14T19:18", 0, 0));
        mDatabase.insertLocationUpdate(new LocationUpdate("46", "2018-01-12T19:18", 0, 0));

        LocationUpdate latestLoc = mDatabase.getLatestLocationForTracker("0");

        assertNotNull("Returned LocationUpdate object is null", latestLoc);
        assertEquals("Returned LocationUpdate object is not the latest row", latestLoc.mTimestamp, "2018-01-14T19:18");
    }
}