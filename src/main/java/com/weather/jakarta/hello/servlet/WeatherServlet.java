package com.weather.jakarta.hello.servlet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.jakarta.hello.dao.WeatherRecordDAO;
import com.weather.jakarta.hello.entity.WeatherRecord;
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
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final WeatherRecordDAO weatherRecordDAO = new WeatherRecordDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String city = request.getParameter("city");
        if(city == null || city.isEmpty()) {
            city = "moscow";
        }

        String apiUrlWithParams = String.format("%s?q=%s&appid=%s", API_URL, city, API_KEY);

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrlWithParams))
                    .build();

            HttpResponse<String> httpResponse = client.send(httpRequest,
                    HttpResponse.BodyHandlers.ofString());

            // Сохраняем в БД
            WeatherRecord record = parseWeatherData(httpResponse.body());
            weatherRecordDAO.saveWeatherRecordWithCity(record, city);

            // Возвращаем ответ
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(httpResponse.body());
            out.flush();
        } catch (InterruptedException e) {
            throw new ServletException("Error fetching weather data", e);
        }
    }
    private WeatherRecord parseWeatherData(String jsonResponse) throws IOException {
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        JsonNode mainNode = rootNode.path("main");
        JsonNode weatherNode = rootNode.path("weather").get(0);

        int cityId = rootNode.path("id").asInt(); // ID города из ответа API
        double temperature = mainNode.path("temp").asDouble(); // Температура
        int humidity = mainNode.path("humidity").asInt(); // Влажность
        int pressure = mainNode.path("pressure").asInt(); // Давление
        String description = weatherNode.path("description").asText(); // Описание погоды

        // Создание объекта WeatherRecord
        return new WeatherRecord(cityId, temperature, humidity, pressure, description);
    }
}
