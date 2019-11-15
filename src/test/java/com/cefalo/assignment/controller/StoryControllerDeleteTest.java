package com.cefalo.assignment.controller;

import com.cefalo.assignment.model.orm.Story;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static com.cefalo.assignment.TestHelper.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StoryControllerDeleteTest {

    @LocalServerPort
    int randomServerPort;

    @DisplayName("creator can delete stories")
    @Test
    public void deleteAStory_GetStatus200() throws Exception {
        RestTemplate restTemplate = getRestTemplate();
        String baseUrl = "http://localhost:" + randomServerPort + "/api/stories/{id}";
        String token = generateTokenForUserNameAndPassword("fish", "pass", randomServerPort);
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("Authorization", token);
        HttpEntity request = new HttpEntity<>(httpHeaders);

        Map<String, String> params = new HashMap<>();
        params.put("id", "1");

        ResponseEntity result = restTemplate.exchange(baseUrl, HttpMethod.DELETE, request, void.class, params);
        assertEquals(result.getStatusCode(), HttpStatus.OK);
    }

    @DisplayName("Only creator can delete stories")
    @Test(expected = HttpClientErrorException.Unauthorized.class)
    public void deleteAStory_GetStatus401() throws Exception {
        RestTemplate restTemplate = getRestTemplate();
        String baseUrl = "http://localhost:" + randomServerPort + "/api/stories/{id}";
        String token = generateTokenForUserNameAndPassword("i", "love", randomServerPort);
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("Authorization", token);
        HttpEntity request = new HttpEntity<>(httpHeaders);

        Map<String, String> params = new HashMap<>();
        params.put("id", "3");

       restTemplate.exchange(baseUrl, HttpMethod.DELETE, request, void.class, params);
    }

    @DisplayName("Only creator can delete stories")
    @Test(expected = HttpClientErrorException.Unauthorized.class)
    public void deleteAStoryWithOutToken_GetStatus401() throws Exception {
        RestTemplate restTemplate = getRestTemplate();
        String baseUrl = "http://localhost:" + randomServerPort + "/api/stories/{id}";
        HttpEntity request = new HttpEntity<>(null);

        Map<String, String> params = new HashMap<>();
        params.put("id", "3");

        restTemplate.exchange(baseUrl, HttpMethod.DELETE, request, void.class, params);
    }

}
