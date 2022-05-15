CREATE DATABASE luft_db;
\c luft_db;
CREATE SCHEMA luft_sc;
SET SCHEMA 'luft_sc';
/*
    Person
*/
CREATE TABLE person (
    id INT GENERATED ALWAYS AS IDENTITY UNIQUE,
    first_name VARCHAR,
    last_name VARCHAR,
    username VARCHAR UNIQUE,
    email VARCHAR,
    phone VARCHAR,
    PRIMARY KEY (id)
);

/*
    Zones
*/
CREATE TABLE sensor_zone (
    id INT GENERATED ALWAYS AS IDENTITY UNIQUE,
    tag VARCHAR NOT NULL,
    PRIMARY KEY (id)
);

/*
    Sensor
*/
CREATE TABLE sensor (
    id INT GENERATED ALWAYS AS IDENTITY UNIQUE,
    token VARCHAR NOT NULL UNIQUE, 
    sensor_name VARCHAR NOT NULL,
    zone_id INT NULL REFERENCES sensor_zone(id),
    proprietor INT NULL REFERENCES person(id),
    PRIMARY KEY (id)
);

/*
    Temperature
*/
CREATE TABLE temperature (
    id INT GENERATED ALWAYS AS IDENTITY UNIQUE,
    temperature FLOAT NOT NULL,
    log_time TIMESTAMP NOT NULL DEFAULT NOW(),
    sensor_id INT NOT NULL REFERENCES sensor(id),
    PRIMARY KEY (id)
);

/*
    Humidity
*/
CREATE TABLE humidity (
    id INT GENERATED ALWAYS AS IDENTITY UNIQUE,
    humidity FLOAT NOT NULL,
    log_time TIMESTAMP NOT NULL DEFAULT NOW(),
    sensor_id INT NOT NULL REFERENCES sensor(id),
    PRIMARY KEY (id)
);

/*
    Electricity price 
*/
CREATE TABLE electricity_price (
    id INT GENERATED ALWAYS AS IDENTITY UNIQUE, 
    price DECIMAL(10,2) NOT NULL,
    log_time TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id)
);

