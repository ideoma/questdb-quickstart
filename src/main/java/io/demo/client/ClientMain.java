package io.demo.client;

import io.questdb.client.Sender;

public class ClientMain {
    public static void main(String[] args) {
        // http::addr=localhost:9000 - Sends data over HTTP transport to the server at localhost:9000
        // retry_timeout=20000 - Sets timeout for retries to 20 seconds. This means recoverable errors will be retried for 20 seconds.
        // auto_flush_rows=100000 - Sets maximum number of rows to be sent in a single request to 100,000.
        // auto_flush_interval=1000 - Sets maximum interval between requests to 1 second - to avoid long lags in low-throughput scenarios.
        try (Sender sender = Sender.fromConfig("http::addr=localhost:9000;retry_timeout=20000;auto_flush_rows=100000;auto_flush_interval=1000;")) {
            for(int i = 0; i < 10_000_000; i++) {
                sender.table("weather_sensor")
                        .symbol("id", "toronto1" + i%100)
                        .doubleColumn("temperature", 23.5)
                        .doubleColumn("humidity", 0.49)
                        .atNow();
                sender.table("weather_sensor")
                        .symbol("id", "dubai2" + i%100)
                        .doubleColumn("temperature", 41.2)
                        .doubleColumn("humidity", 0.34)
                        .atNow();
            }

            sender.flush();
        }
    }
}