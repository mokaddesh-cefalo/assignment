package com.cefalo.assignment;

import com.cefalo.assignment.model.orm.Story;
import com.cefalo.assignment.security.AuthenticationRequest;
import com.cefalo.assignment.security.AuthenticationResponse;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

public class TestHelper {
    public static Date stringToDate(String dateInString) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd MMMM yyyy");

        return DateTime.parse(dateInString, formatter).toDate();
    }

    public static void checkIfStoriesSortedByPublishedDate(ResponseEntity<Story[]> result) throws Exception {
        SortedByStoryField sorted = (Story cur, Story nex) -> {
            Date curDate = cur.getPublishedDate();
            Date nexDate = nex.getPublishedDate();

            if(nexDate.before(curDate))
                throw new Exception("Get all Not working, stories are not sorted by published date");
        };
        checkIfStoriesSorted(result, sorted);
    }

    public static void checkIfStoriesSortedByTitle(ResponseEntity<Story[]> result) throws Exception {
        SortedByStoryField sorted = (Story cur, Story nex) -> {
            String curTitle = cur.getTitle();
            String nexTitle = nex.getTitle();

            if(curTitle.compareTo(nexTitle) > 0) {
                throw new Exception("Sorting for pagination Not working for title");
            }
        };
        checkIfStoriesSorted(result, sorted);
    }

    static void checkIfStoriesSorted(ResponseEntity<Story[]> result, SortedByStoryField sorted) throws Exception {
        Assert.assertNotNull(result.getBody());
        Story[] stories = result.getBody();

        for(int i = 0; i < stories.length - 1; i++) {
            sorted.check(stories[i], stories[i + 1]);
        }
    }


    public static RestTemplate getRestTemplate() {
        /**without this part there will be a a error for multiple authentication retry
         * The problem is due to chunking and subsequent retry mechanism incase of authentication.
         * */
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);

        return new RestTemplate(requestFactory);
    }

    public static String generateTokenForUserNameAndPassword(String userName, String password, int randomServerPort) throws URISyntaxException {
        String baseUrl = "http://localhost:" + randomServerPort + "/api/authenticate";
        URI uri = new URI(baseUrl);
        AuthenticationRequest auth = new AuthenticationRequest(userName, password);
        HttpHeaders headers = new HttpHeaders();

        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<AuthenticationRequest> request = new HttpEntity<>(auth, headers);

        return "Bearer " +
                new RestTemplate().postForEntity(uri, request, AuthenticationResponse.class).getBody().getJwt();
    }


}
