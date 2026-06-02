package com.tuku.controller;

import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Collections;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest(classes = TukuWebApplication.class)
@Slf4j
public class QdrantTest {
    @Resource
    private QdrantClient qdrantClient;

    @Test
    void test1() {
    
    }
}
