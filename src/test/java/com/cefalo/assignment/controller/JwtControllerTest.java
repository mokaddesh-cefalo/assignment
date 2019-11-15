package com.cefalo.assignment.controller;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import static com.cefalo.assignment.TestHelper.getRestTemplate;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JwtControllerTest {

    /*@Autowired
    private TestRestTemplate restTemplate;*/

    TestInfo testInfo;
    TestReporter testReporter;
    URI uri;
    AuthenticationRequest auth;
    HttpEntity<AuthenticationRequest> request;

    @LocalServerPort
    int randomServerPort;

    @BeforeEach
    void init(TestInfo testInfo, TestReporter testReporter) throws URISyntaxException {

        this.testInfo = testInfo;
        this.testReporter = testReporter;
    }

    @Test
    public void postRequestForJWTToken_shouldGet200()throws Exception{
        TestRestTemplate testRestTemplate = new TestRestTemplate();

        beforePost("fish", "pass");
        ResponseEntity<AuthenticationResponse> result
                = testRestTemplate.postForEntity(uri, request, AuthenticationResponse.class);

        Assert.assertEquals(200, result.getStatusCodeValue());
        Assert.assertNotNull(result.getBody());
        Assert.assertNotNull(result.getBody().getJwt());
    }

    @DisplayName("password will be wrong in auth request")
    @Test//(expected = HttpClientErrorException.Unauthorized.class)
    public void postRequestForJWTToken_shouldGet401() throws Exception{
        RestTemplate restTemplate = getRestTemplate();
        beforePost("fish", "love"); ///password for fish is pass 'see data.sql'
        assertThrows(HttpClientErrorException.Unauthorized.class, () ->
                restTemplate.postForEntity(uri, request, AuthenticationResponse.class));
    }

    @DisplayName("password will be missing in auth request")
    @Test
    public void postRequestForJWTToken_shouldGet422() throws Exception{
        RestTemplate restTemplate = getRestTemplate();
        beforePost("fish", null); //it will give a validation error as password should not be null
        assertThrows(HttpClientErrorException.UnprocessableEntity.class, () ->
                restTemplate.postForEntity(uri, request, AuthenticationResponse.class));
    }

    @DisplayName("Username is not valid")
    @Test
    public void postRequestForJWTToken_shouldGet404() throws Exception{
        RestTemplate restTemplate = getRestTemplate();
        beforePost("fishi", "pass");
        assertThrows(HttpClientErrorException.Unauthorized.class, () ->
                restTemplate.postForEntity(uri, request, AuthenticationResponse.class));
    }



    private void beforePost(String userName, String password) throws URISyntaxException {
        String baseUrl = "http://localhost:" + randomServerPort + "/api/authenticate";
        uri = new URI(baseUrl);
        auth = new AuthenticationRequest(userName, password);
        HttpHeaders headers = new HttpHeaders();

        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        request = new HttpEntity<>(auth, headers);
    }
}
