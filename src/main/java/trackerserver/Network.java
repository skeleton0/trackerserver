package trackerserver;

import fi.iki.elonen.NanoHTTPD;

class Network extends NanoHTTPD {
    Network(int port) {
        super(port);
    }
}
