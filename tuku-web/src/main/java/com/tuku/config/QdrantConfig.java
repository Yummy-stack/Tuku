package com.tuku.config;

import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class QdrantConfig {
    @Value("${qdrant.host}")
    private String host;

    @Value("${qdrant.port}")
    private int port;

    @Value("${qdrant.useTls}")
    private boolean useTls;

    @Bean
    public QdrantClient qdrantClient() {
        QdrantGrpcClient grpcClient = QdrantGrpcClient.newBuilder(host, port, useTls)
                .build();
        return new QdrantClient(grpcClient);
    }
}
