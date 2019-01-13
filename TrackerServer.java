class TrackerServer {
    public static void main(String[] args) {
        try {
            Database db = new Database("tracker_server.db", false);

            Network net = new Network(46000);
            net.start();

            Thread.sleep(60000);
        } catch (Exception e) {
            System.err.print("Caught exception: ");
            System.err.println(e.getMessage());
        }  
    }
}