package com.mediconnect.iotservice.config;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxDBConfig {

    @Value("${influxdb.url:http://localhost:8086}")
    private String url;

    @Value("${influxdb.token:mediconnect-influx-token-2024}")
    private String token;

    @Value("${influxdb.org:mediconnect}")
    private String org;

    @Value("${influxdb.bucket:iot_data}")
    private String bucket;

    @Bean
    public InfluxDBClient influxDBClient() {
        return InfluxDBClientFactory.create(url, token.toCharArray(), org, bucket);
    }
}