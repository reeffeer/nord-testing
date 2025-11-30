package org.example.core.stub;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

public class WireMockServerManager {
    private WireMockServer server;

    public WireMockServer server() {
        return server;
    }

    public void start(int port) {
        server = new WireMockServer(WireMockConfiguration.options().port(port));
        server.start();
    }

    public void stop() {
        if (server != null) {
            server.stop();
        }
    }
}
