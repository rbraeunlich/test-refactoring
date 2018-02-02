package de.codecentric.example.clean.test.code.chucknorris;

import de.codecentric.example.clean.test.code.chucknorris.domain.ChuckNorrisFact;

import org.junit.Test;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

import static de.codecentric.example.clean.test.code.chucknorris.ChuckNorrisService.FACT_URL;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChuckNorrisServiceStepOneTest {

    private static final Long EXISTING_JOKE = 1L;
    private static final Map<String, Long> GOOD_HTTP_PARAMS = Collections.singletonMap("id", EXISTING_JOKE);
    private static final Long NON_EXISTING_JOKE = 15123123L;
    private static final Map<String, Long> NON_EXISTING_HTTP_PARAMS = Collections.singletonMap("id", NON_EXISTING_JOKE);
    private static final Long BAD_JOKE = 99999999L;
    private static final Map<String, Long> BAD_HTTP_PARAMS = Collections.singletonMap("id", BAD_JOKE);

    private static final ResponseEntity<ChuckNorrisFactResponse> ERROR_RESPONSE =
            new ResponseEntity<>(new ChuckNorrisFactResponse("NoSuchQuoteException", "No quote with id=15123123."), HttpStatus.OK);
    private static final ResponseEntity<ChuckNorrisFactResponse> ITEM_RESPONSE =
            new ResponseEntity<>(new ChuckNorrisFactResponse("success", new ChuckNorrisFact(1L, "Chuck Norris is awesome")), HttpStatus.OK);

    @Test
    public void testSomething() {
        RestTemplate restTemplate = restEndpointShouldAnswer(GOOD_HTTP_PARAMS, (invocation) -> ITEM_RESPONSE);
        ChuckNorrisService myServiceUnderTest = new ChuckNorrisService(restTemplate);

        ChuckNorrisFact chuckNorrisFact = myServiceUnderTest.retrieveFact(EXISTING_JOKE);

        assertThat(chuckNorrisFact, is(new ChuckNorrisFact(EXISTING_JOKE, "Chuck Norris is awesome")));
    }

    @Test
    public void testSomethingElse() {
        RestTemplate restTemplate = restEndpointShouldAnswer(NON_EXISTING_HTTP_PARAMS, (invocation -> ERROR_RESPONSE));
        ChuckNorrisService myServiceUnderTest = new ChuckNorrisService(restTemplate);

        ChuckNorrisFact chuckNorrisFact = myServiceUnderTest.retrieveFact(NON_EXISTING_JOKE);

        assertThat(chuckNorrisFact, is(nullValue()));
    }

    @Test(expected = ResourceAccessException.class)
    public void testSomeError() {
        RestTemplate restTemplate = restEndpointShouldAnswer(BAD_HTTP_PARAMS, (invocation -> {throw new ResourceAccessException("I/O error");}));
        ChuckNorrisService myServiceUnderTest = new ChuckNorrisService(restTemplate);

        myServiceUnderTest.retrieveFact(BAD_JOKE);
    }

    private RestTemplate restEndpointShouldAnswer(Map<String, Long> httpParams, Answer<ResponseEntity<ChuckNorrisFactResponse>> response){
        RestTemplate restTemplate = mock(RestTemplate.class);
        when(restTemplate.getForEntity(FACT_URL, ChuckNorrisFactResponse.class, httpParams)).thenAnswer(response);
        return restTemplate;
    }
}
