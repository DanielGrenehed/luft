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

