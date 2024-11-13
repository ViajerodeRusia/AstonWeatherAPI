package com.weather.jakarta.hello;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@WebServlet("/weather")
public class WeatherServlet extends HttpServlet {
    private static final String API_KEY = "f7d55e27d0bbc1c23cde47e9d815a639";
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String city = request.getParameter("city");
        if(city == null || city.isEmpty()) {
            city = "moscow";
        }
        String apiUrlWithParams = String.format("%s?q=%s&appid=%s", API_URL, city, API_KEY);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(apiUrlWithParams))
                .build();

        try {
            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(httpResponse.body());
            out.flush();
        } catch ( InterruptedException e) {
            e.printStackTrace();
        }
    }
}
