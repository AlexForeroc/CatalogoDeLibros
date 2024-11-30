package com.alexfc.catalogodelibros.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class ConsumoAPI {

    public ConsumoAPI() {
    }

    public String obtenerDatosLibros(String url) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> response = null;

        try {
            response = client.send(request, BodyHandlers.ofString());
        } catch (InterruptedException | IOException var6) {
            Exception e = var6;
            throw new RuntimeException(e);
        }

        String json = (String)response.body();
        return json;
    }

}