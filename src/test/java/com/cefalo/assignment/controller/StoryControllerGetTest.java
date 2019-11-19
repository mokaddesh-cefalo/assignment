package com.cefalo.assignment.controller;

import com.cefalo.assignment.SortedByStoryField;
import com.cefalo.assignment.model.orm.Story;
import com.cefalo.assignment.security.AuthenticationRequest;
import com.cefalo.assignment.security.AuthenticationResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
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
public class StoryControllerGetTest {

    HttpEntity<Story> request;

    @LocalServerPort
    int randomServerPort;

    @DisplayName("Get a the story By Id")
    @Test
    public void getAStoryById_shouldGet200() throws Exception {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        String url = "http://localhost:" + randomServerPort + "/api/stories/12";

        ResponseEntity<Story> result
                = testRestTemplate.exchange(url, HttpMethod.GET, request,Story.class);

        Assert.assertEquals(200, result.getStatusCodeValue());
    }

    @DisplayName("Get a the story By Id")
    @Test
    public void getAStoryById_shouldGet404() throws Exception {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        String url = "http://localhost:" + randomServerPort + "/api/stories/11111";

        ResponseEntity result = testRestTemplate.exchange(url, HttpMethod.GET, request, Story.class);
        assertEquals(result.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @DisplayName("Get all of the stories sorted by published date")
    @Test
    public void getAllStories_shouldGet200() throws Exception {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        String url = "http://localhost:" + randomServerPort + "/api/stories";

        ResponseEntity<Story[]> result
                = testRestTemplate.exchange(url, HttpMethod.GET, request, Story[].class);

        Assert.assertEquals(200, result.getStatusCodeValue());
        checkIfStoriesSortedByPublishedDate(result);

    }

    @DisplayName("Get all of the stories sorted by Title and limit is set to 3 and page number is 1")
    @Test
    public void getAllStoriesAndUsePagination_shouldGet200() throws Exception {
        TestRestTemplate testRestTemplate = new TestRestTemplate();

        int limit = 3;
        String url = "http://localhost:" + randomServerPort + "/api/stories?sort=title&limit=" + limit;

        ResponseEntity<Story[]> result
                = testRestTemplate.exchange(url, HttpMethod.GET, request, Story[].class);

        Assert.assertEquals(200, result.getStatusCodeValue());
        Assert.assertNotNull(result.getBody());

        Story[] stories = result.getBody();
        assert (limit >= stories.length);
        checkIfStoriesSortedByTitle(result);

    }


}
