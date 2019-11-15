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
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static com.cefalo.assignment.TestHelper.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StoryControllerPostTest {

    URI uri;
    HttpEntity<Story> request;

    @LocalServerPort
    int randomServerPort;

    @DisplayName("UnAuthorizes users can not add stories")
    @Test(expected = HttpClientErrorException.Unauthorized.class)
    public void postANewStory_GetStatus401() throws Exception {
        RestTemplate restTemplate = getRestTemplate();
        beforePostNewStory();
        restTemplate.postForEntity(uri, request, Story.class);
    }

    @DisplayName("Authorized users can add stories")
    @Test
    public void postANewStory_GetStatus201() throws Exception {
        RestTemplate restTemplate = getRestTemplate();
        String token = generateTokenForUserNameAndPassword("fish", "pass", randomServerPort);

        beforePostNewStory("Authorization", token);
        ResponseEntity<Story> result = restTemplate.postForEntity(uri, request, Story.class);

        assertEquals(result.getStatusCode(), HttpStatus.CREATED, "Could not create new story");
    }

    private void beforePostNewStory(String... headers) throws URISyntaxException{
        String baseUrl = "http://localhost:" + randomServerPort + "/api/stories";
        Date publishedDate = stringToDate("24 nov 1994");
        Story story = new Story("new title", "new body", publishedDate);

        uri = new URI(baseUrl);
        setHttpEntityForPOSTStory(story, headers);
    }

    private void setHttpEntityForPOSTStory(Story story, String... extraHeaders) {
        HttpHeaders headers = new HttpHeaders();

        for(int i = 0; i < extraHeaders.length; i += 2) {
            headers.add(extraHeaders[i], extraHeaders[i + 1]);
        }

        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        request = new HttpEntity<>(story, headers);
    }
}
