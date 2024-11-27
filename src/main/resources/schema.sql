CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cities (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    country_code VARCHAR(2),
    latitude DECIMAL(9,6),
    longitude DECIMAL(9,6)
);

CREATE TABLE weather_records (
    id SERIAL PRIMARY KEY,
    city_id INTEGER REFERENCES cities(id),
    temperature DECIMAL(5,2),
    humidity INTEGER,
    pressure INTEGER,
    description VARCHAR(200),
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_favorite_cities (
    user_id INTEGER REFERENCES users(id),
    city_id INTEGER REFERENCES cities(id),
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, city_id)
);