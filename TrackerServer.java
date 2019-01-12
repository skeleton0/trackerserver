class TrackerServer {
    public static void main(String[] args) {

        try {
            Database db = new Database("tracker_server.db", false);
        } catch (Exception e) {
            System.err.print("Caught exception: ");
            System.err.println(e.getMessage());
        }
    }
}