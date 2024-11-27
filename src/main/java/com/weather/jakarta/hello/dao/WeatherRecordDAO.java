package com.weather.jakarta.hello.dao;

import com.weather.jakarta.hello.entity.WeatherRecord;
import com.weather.jakarta.hello.config.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WeatherRecordDAO {

    public void saveWeatherRecord(WeatherRecord record) {
        String sql = "INSERT INTO weather_records (city_id, temperature, humidity, pressure, description) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, record.getCityId());
            stmt.setDouble(2, record.getTemperature());
            stmt.setInt(3, record.getHumidity());
            stmt.setInt(4, record.getPressure());
            stmt.setString(5, record.getDescription());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving weather record", e);
        }
    }

    // Example of a transaction involving multiple operations
    public void saveWeatherRecordWithCity(WeatherRecord record, String cityName) {
        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // First insert or get city
                int cityId;
                try (PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO cities (name) VALUES (?) ON CONFLICT (name) DO UPDATE SET name = EXCLUDED.name RETURNING id")) {
                    stmt.setString(1, cityName);
                    ResultSet rs = stmt.executeQuery();
                    rs.next();
                    cityId = rs.getInt(1);
                }

                // Then save weather record
                try (PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO weather_records (city_id, temperature, humidity, pressure, description) VALUES (?, ?, ?, ?, ?)")) {
                    stmt.setInt(1, cityId);
                    stmt.setDouble(2, record.getTemperature());
                    stmt.setInt(3, record.getHumidity());
                    stmt.setInt(4, record.getPressure());
                    stmt.setString(5, record.getDescription());
                    stmt.executeUpdate();
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error in transaction", e);
        }
    }
}
