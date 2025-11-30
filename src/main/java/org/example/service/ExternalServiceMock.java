package org.example.service;

import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class ExternalServiceMock {

    public static void reset(WireMockServer wmServer) {
        wmServer.resetAll();
    }

    public static void stubAuthOk(WireMockServer wmServer) {
        wmServer.stubFor(post(urlEqualTo("/auth"))
            .willReturn(aResponse().withStatus(200).withBody("{}")));
    }

    public static void stubAuthFail(WireMockServer wmServer) {
        wmServer.stubFor(post(urlEqualTo("/auth"))
            .willReturn(aResponse().withStatus(401).withBody("{}")));
    }

    public static void stubDoActionOk(WireMockServer wmServer) {
        wmServer.stubFor(post(urlEqualTo("/doAction"))
                .willReturn(aResponse().withStatus(200).withBody("{}")));
    }

    public static void stubDoActionFail(WireMockServer wmServer) {
        wmServer.stubFor(post(urlEqualTo("/doAction"))
                .willReturn(aResponse().withStatus(401).withBody("{}")));
    }
}
