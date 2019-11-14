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
public class StoryControllerPutTest {

    URI uri;
    HttpEntity<Story> request;

    @LocalServerPort
    int randomServerPort;

    @DisplayName("UnAuthorizes users can not put stories")
    @Test(expected = HttpClientErrorException.Unauthorized.class)
    public void putAStory_GetStatus401() throws Exception {
        RestTemplate restTemplate = getRestTemplate();
        beforePutPutObject();
        restTemplate.exchange(uri, HttpMethod.PUT, request, Story.class);
    }

    @DisplayName("creator can put stories")
    @Test
    public void putAStory_GetStatus201() throws Exception {
        RestTemplate restTemplate = getRestTemplate();
        String token = generateTokenForUserNameAndPassword("fish", "pass", randomServerPort);

        beforePutPutObject("Authorization", token);
        ResponseEntity<Story> result =  restTemplate.exchange(uri, HttpMethod.PUT, request, Story.class);;

        assertEquals(result.getStatusCode(), HttpStatus.OK, "Could not create new story");
    }

    @DisplayName("Only creator can put stories")
    @Test(expected = HttpClientErrorException.Unauthorized.class)
    public void putAStoryWithoutCreator_GetStatus401() throws Exception {
        RestTemplate restTemplate = getRestTemplate();
        String token = generateTokenForUserNameAndPassword("i", "love", randomServerPort);

        beforePutPutObject("Authorization", token);
        restTemplate.exchange(uri, HttpMethod.PUT, request, Story.class);;

    }

    private void beforePutPutObject(String... headers) throws URISyntaxException{
        String baseUrl = "http://localhost:" + randomServerPort + "/api/stories/1";
        Date publishedDate = stringToDate("24 nov 1994");
        Story story = new Story("new title", "new body", publishedDate);
        uri = new URI(baseUrl);

        setHttpEntityForPut(story, headers);
    }

    private void setHttpEntityForPut(Story story, String... extraHeaders) {
        HttpHeaders headers = new HttpHeaders();

        for(int i = 0; i < extraHeaders.length; i += 2) {
            headers.add(extraHeaders[i], extraHeaders[i + 1]);
        }

        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        request = new HttpEntity<>(story, headers);
    }
}

