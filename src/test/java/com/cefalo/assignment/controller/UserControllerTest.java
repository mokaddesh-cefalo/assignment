package com.cefalo.assignment.controller;

import com.cefalo.assignment.model.orm.Story;
import com.cefalo.assignment.model.orm.User;
import com.cefalo.assignment.security.AuthenticationRequest;
import com.cefalo.assignment.security.AuthenticationResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static com.cefalo.assignment.TestHelper.getRestTemplate;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    TestInfo testInfo;
    TestReporter testReporter;
    URI uri;
    User user;
    HttpEntity<User> request;

    @LocalServerPort
    int randomServerPort;

    @BeforeEach
    void init(TestInfo testInfo, TestReporter testReporter) throws URISyntaxException {

        this.testInfo = testInfo;
        this.testReporter = testReporter;
    }

    @DisplayName("All of the stories created by the user fish")
    @Test
    public void getUserStories_shouldGet200() throws Exception{
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        String url = "http://localhost:" + randomServerPort + "/api/users/fish/stories";

        setHttpEntity();
        ResponseEntity<Story[]> result
                = testRestTemplate.exchange(url, HttpMethod.GET, request,Story[].class);

        Assert.assertEquals(200, result.getStatusCodeValue());
        Assert.assertNotNull(result.getBody());
    }

    @DisplayName(" created a new user")
    @Test
    public void postNewUser_shouldGet201()throws Exception{
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        String userName = "money", password = "pass";

        beforePost(userName, password, "");

        ResponseEntity<User> result
                = testRestTemplate.postForEntity(uri, request, User.class);

        Assert.assertEquals(201, result.getStatusCodeValue());
        Assert.assertNotNull(result.getBody());
        Assert.assertNotNull(result.getBody().getRoles());
        assert (result.getBody().getUserName().compareTo(userName) == 0);
    }

    @DisplayName("trying to register same user twice")
    @Test
    public void postNewUser_shouldGet422() throws Exception {
        RestTemplate restTemplate = getRestTemplate();

        beforePost("fish", "pass", "");
        assertThrows(HttpClientErrorException.UnprocessableEntity.class, () ->
                restTemplate.postForEntity(uri, request, User.class));
    }

    @DisplayName("trying to register user without name")
    @Test
    public void postNewUserWithNullUserName_shouldGet422() throws Exception {
        RestTemplate restTemplate = getRestTemplate();

        beforePost(null, "pass", "");
        assertThrows(HttpClientErrorException.UnprocessableEntity.class, () ->
                restTemplate.postForEntity(uri, request, User.class));
    }

    @DisplayName("trying to register user without password")
    @Test
    public void postNewUserWithNullPassword_shouldGet422() throws Exception {
        RestTemplate restTemplate = getRestTemplate();

        beforePost("fish", null, "");
        assertThrows(HttpClientErrorException.UnprocessableEntity.class, () ->
                restTemplate.postForEntity(uri, request, User.class));
    }

    private void beforePost(String userName, String password, String urlExtention) throws URISyntaxException {
        String baseUrl = "http://localhost:" + randomServerPort + "/api/users" + urlExtention;
        uri = new URI(baseUrl);
        user = new User(userName, password);
        setHttpEntity();
    }

    private void setHttpEntity() {
        HttpHeaders headers = new HttpHeaders();

        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        request = new HttpEntity<>(user, headers);
    }
}
