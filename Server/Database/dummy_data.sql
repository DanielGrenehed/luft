
insert into luft_sc.person (first_name, last_name, username) values ('dummy', 'person', 'dummy');
insert into luft_sc.sensor (token, sensor_name) values ('dummy', 'test_sensor'), ('test', 'dummy_sensor');

insert into luft_sc.humidity (humidity, sensor_id) values (22.5, 1);
insert into luft_sc.humidity (humidity, sensor_id) values (23.4, 1);
insert into luft_sc.humidity (humidity, sensor_id, log_time) values (33.3, 1, '2016-06-22 19:10:25-07');

insert into luft_sc.temperature (temperature, sensor_id) values (16.3, 1);
insert into luft_sc.temperature (temperature, sensor_id) values (18.2, 1);
insert into luft_sc.temperature (temperature, sensor_id, log_time) values (33.3, 1, '2016-06-22 19:10:25-07');

insert into luft_sc.electricity_price (price) values (30.4);

insert into luft_sc.electricity_price (price) values (32.4);

insert into luft_sc.sensor_zone (tag) values ('test_zone');
update sensor set zone_id=1 where id>0;

