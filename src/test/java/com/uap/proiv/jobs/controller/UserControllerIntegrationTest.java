package com.uap.proiv.jobs.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uap.proiv.jobs.client.UserApiRepository;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Duration;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired 
    private ObjectMapper objectMapper;

    @Autowired
    UserApiRepository userApiRepository;

    static MockWebServer mockWebServer; // Es lo que levanta el servicio.

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }


    @AfterAll
    public static void tearDown() throws IOException {
        mockWebServer.close();
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public UserApiRepository userApiRespository(ObjectMapper objectMapper) {
            HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
            String baseUrl = mockWebServer.url("/api/users").toString();
            String apiKey = "free_user_3HYTiqu2JKQ4TfGq884xW5mqfrd";

            return new UserApiRepository(httpClient, objectMapper, baseUrl, apiKey);
        }
    }

    @Test
    @DisplayName("GET /api/users/{id} integracion UserController, UserService, UserRepository, mock api externa")
    void getUserById() throws Exception {
        String jsonResponse = """
                {
                    "id": 2,
                    "email": "juan@gmail.com",
                    "first_name": "Juan",
                    "last_name": "Perez",
                    "avatar": "https://reqres.in/img/faces/2-image.jpg"
        }
                """;
        mockWebServer.enqueue(new MockResponse()
        .setBody(jsonResponse)
        .setResponseCode(200)
        .addHeader("Content-Type", "application/json"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/id/2"))
            .andExpect(status().isOk());
    }
}