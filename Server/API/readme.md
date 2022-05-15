#

## URLs

Get latest zone data (time, temperature, humidity, pice)
/zone/{zone}

### Humidity
Get latest humidity (time, humidity)
/humidity/{sensor_id}

Get average humidity per day
/humidities/{sensor_id}

Log bogus humidity
/humidity (body: humidity=x)



### Temperature
Get latest temperature (time, temperature)
/temperature/{sensor_id}

Get average temperature per day
/temperatures/{sensor_id}

Log bogus temp (post)
/temperature (body: temperature=x)

Log temp
/temperature/{sensor_token} (temperature=x,time=y)


### Electricity price
Get latest electricity price
/electricity

Set price (post)
/electicity (body: price=x)


### Zone
Get sensors in zone
/zone/{id}

Get latest temperature in zone
/zone/{id}/temperature

Get latest humidity in zone
/zone/{id}/humidity
