package dev.swellington.literalura.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ConsumeAPI {

    private final static String BASE_URL = "https://gutendex.com";

    public static String makeRequest(String endpoint, String[][] params) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();

        StringBuilder url = new StringBuilder(BASE_URL + endpoint);
        if (params != null) {
            url.append("?").append(Arrays.stream(params).map(
                    f -> f[0].trim() + "=" + f[1].trim().replaceAll(" ", "+")
            ).collect(Collectors.joining("&")));
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url.toString()))
                .GET()
                .build();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return response.body();
        }
        throw new RuntimeException("Error: " + response.statusCode());
    }
}
