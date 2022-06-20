# DATABASE

## POSTGRESQL

```SQL
CREATE ROLE root WITH LOGIN SUPERUSER PASSWORD 'password';
```
```POSTGRESQL
\i setup.sql
```

Create user and allow access to tables
```SQL
CREATE USER luft WITH PASSWORD 'pluft';
GRANT CONNECT ON DATABASE luft_db TO luft;
\c luft_db
GRANT USAGE ON SCHEMA luft_sc TO luft;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA luft_sc TO luft;
```

Group 
```SQL
select luft_sc.sensor.sensor_name, luft_sc.humidity.humidity, luft_sc.temperature.temperature, luft_sc.humidity.log_time from luft_sc.sensor 
inner join luft_sc.humidity on luft_sc.sensor.id = luft_sc.humidity.sensor_id 
inner join luft_sc.temperature on luft_sc.sensor.id = luft_sc.temperature.sensor_id;
```

Average Humidity
```SQL
select AVG(humidity) as AVG_Humidity from luft_sc.humidity where sensor_id=1;
```

Average Temperature
```SQL
select sensor_id, AVG(temperature) as Average from luft_sc.temperature group by sensor_id;
```

Average Temperature per day
```SQL
SELECT sensor_id, CAST(log_time AS date), AVG(temperature) as temperature FROM luft_sc.temperature where sensor_id=1 GROUP BY sensor_id, CAST(log_time as date);
```

Average Humidity per day
```SQL
SELECT sensor_id, CAST(log_time AS DATE), AVG(humidity) AS humidity FROM luft_sc.humidity WHERE sensor_id=1 GROUP BY sensor_id, CAST(log_time AS DATE);
```

Average temperature with sensor name
```SQL
SELECT luft_sc.sensor.sensor_name, log_time, temperature AS avg_temp 
FROM (SELECT sensor_id, CAST(log_time AS date), AVG(temperature) AS temperature FROM luft_sc.temperature WHERE sensor_id=1 GROUP BY sensor_id, CAST(log_time AS DATE)) AS _
INNER JOIN luft_sc.sensor ON sensor_id=luft_sc.sensor.id;
```

```SQL
UPDATE luft_sc.sensor SET proprietor=? WHERE id=?;
```

Create test zone
```SQL
insert into luft_sc.sensor_zone (tag) values ('test');
```

Set sensor zone
```SQL
UPDATE luft_sc.sensor SET zone_id=? WHERE id=?;
```

List sensors in zone
```SQL
SELECT id, token, sensor_name FROM luft_sc.sensor WHERE zone_id=?;
```

List zones and number of sensors in zone
```SQL
SELECT sensor_zone.id, sensor_zone.tag, COUNT(sensor.zone_id) AS sensor_count FROM sensor_zone LEFT JOIN sensor ON sensor_zone.id = sensor.zone_id GROUP BY sensor_zone.id, sensor_zone.tag;
```

Get sensors by zone
```SQL
SELECT * FROM luft_sc.sensor WHERE zone_id=?;
```

Get all humidity entries in zone
```SQL
SELECT * FROM luft_sc.humidity WHERE sensor_id IN (SELECT id FROM luft_sc.sensor WHERE zone_id=?) ORDER BY log_time DESC LIMIT 1;
```

Insert humidity
```SQL
INSERT INTO luft_sc.humidity (humidity, sensor_id, log_time) values (?, SELECT id FROM (SELECT id FROM luft_sc.sensor WHERE token=?), ?);
```

Get zone with id
```SQL
SELECT sensor_zone.id, sensor_zone.tag, COUNT(sensor.zone_id) AS sensor_count FROM luft_sc.sensor_zone LEFT JOIN luft_sc.sensor ON sensor_zone.id = sensor.zone_id WHERE sensor_zone.id=? GROUP BY sensor_zone.id, sensor_zone.tag ORDER BY sensor_count DESC;
```

Create sensor
```SQL
insert into luft_sc.sensor (token, sensor_name, zone_id) values ('0C:B8:15:D2:14:BC', 'esp32_TH', 1);
```

Insert new humidity entry by sensor token
```SQL
INSERT INTO luft_sc.humidity (humidity, sensor_id) values (0, (SELECT id FROM luft_sc.sensor WHERE token='0C:B8:15:D2:14:BC'));
```

Create Zones
```SQL
insert into luft_sc.sensor_zone (tag) values ('A'), ('B'), ('C');
```

Create price now
```SQL
insert into luft_sc.electricity_price (price) values (22.5);
```

Get average humidity for every 10 minutes one day from one sensor
```SQL
SELECT AVG(humidity) AS humidity, log_time FROM 
(SELECT humidity, to_timestamp(FLOOR((EXTRACT(epoch from log_time))/600)*600) AS log_time FROM luft_sc.humidity WHERE sensor_id=4 AND CAST(log_time AS DATE)='2022-05-15' GROUP BY log_time, humidity ORDER BY log_time DESC) AS ten_minute_logs GROUP BY log_time ORDER BY log_time DESC;
```

Get average temperature for every 10 minutes one day from one sensor
```SQL
SELECT AVG(temperature) AS temperature, MAX(temperature), MIN(temperature), log_time FROM 
(SELECT temperature, to_timestamp(FLOOR((EXTRACT(epoch from log_time))/600)*600) AS log_time FROM luft_sc.temperature WHERE sensor_id=4 AND CAST(log_time AS DATE)='2022-05-15' GROUP BY log_time, temperature ORDER BY log_time DESC) AS ten_minute_logs GROUP BY log_time ORDER BY log_time DESC;
```

Get average temperature of every 10 minutes of all days in period
```SQL
SELECT AVG(temperature) AS temperature, to_timestamp(to_char(log_time, 'HH24:MI'), 'HH24:MI') AS log_time FROM (SELECT temperature, to_timestamp(FLOOR((EXTRACT(epoch FROM log_time))/600)*600) AS log_time FROM luft_sc.temperature WHERE sensor_id=4 AND CAST(log_time AS DATE) BETWEEN '2022-05-15' AND '2022-05-15' GROUP BY log_time, temperature ORDER BY log_time DESC) AS ten_minute_logs GROUP BY log_time ORDER BY log_time DESC;
```

Get average humidity of every 10 minutes of all days in period
```SQL
SELECT AVG(humidity) AS humidity, to_timestamp(to_char(log_time, 'HH24:MI'), 'HH24:MI') AS log_time FROM (SELECT humidity, to_timestamp(FLOOR((EXTRACT(epoch FROM log_time))/600)*600) AS log_time FROM luft_sc.humidity WHERE sensor_id=? AND CAST(log_time AS DATE) BETWEEN ? AND ? GROUP BY log_time, humidity ORDER BY log_time DESC) AS ten_minute_logs GROUP BY log_time ORDER BY log_time DESC;
```

Get average humidity of every 10 minutes of all days in period with min and max humidities
```SQL
SELECT AVG(humidity) AS humidity, log_time, MIN(min), MAX(max) FROM (SELECT AVG(humidity) AS humidity, to_timestamp(to_char(log_time, 'HH24:MI'), 'HH24:MI') AS log_time, MIN(humidity), MAX(humidity) FROM (SELECT humidity, to_timestamp(FLOOR((EXTRACT(epoch FROM log_time))/600)*600) AS log_time FROM luft_sc.humidity WHERE sensor_id=? AND CAST(log_time AS DATE) BETWEEN ? AND ? GROUP BY log_time, humidity ORDER BY log_time DESC) AS ten_minute_logs GROUP BY log_time ORDER BY log_time DESC) AS daily GROUP BY log_time ORDER BY log_time DESC;
```


Get average temperature of every 10 minutes of all days in period with min and max humidities
```SQL
SELECT AVG(temperature) AS temperature, log_time, MIN(min), MAX(max) FROM (SELECT AVG(temperature) AS temperature, to_timestamp(to_char(log_time, 'HH24:MI'), 'HH24:MI') AS log_time, MIN(temperature), MAX(temperature) FROM (SELECT temperature, to_timestamp(FLOOR((EXTRACT(epoch FROM log_time))/600)*600) AS log_time FROM luft_sc.temperature WHERE sensor_id=? AND CAST(log_time AS DATE) BETWEEN ? AND ? GROUP BY log_time, temperature ORDER BY log_time DESC) AS ten_minute_logs GROUP BY log_time ORDER BY log_time DESC) AS daily GROUP BY log_time ORDER BY log_time DESC;
```