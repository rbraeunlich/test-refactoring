package de.codecentric.example.clean.test.code.chucknorris;

import de.codecentric.example.clean.test.code.chucknorris.domain.ChuckNorrisFact;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static de.codecentric.example.clean.test.code.chucknorris.ChuckNorrisService.FACT_URL;

public class ChuckNorrisServiceNeedsRefactoringTest {

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
    public void serviceShouldReturnFact() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        when(restTemplate.getForEntity(FACT_URL, ChuckNorrisFactResponse.class, GOOD_HTTP_PARAMS))
                .thenReturn(ITEM_RESPONSE);
        ChuckNorrisService myServiceUnderTest = new ChuckNorrisService(restTemplate);

        ChuckNorrisFact chuckNorrisFact = myServiceUnderTest.retrieveFact(EXISTING_JOKE);

        assertThat(chuckNorrisFact, is(new ChuckNorrisFact(EXISTING_JOKE, "Chuck Norris is awesome")));
    }

    @Test
    public void serviceShouldReturnNothing() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        when(restTemplate.getForEntity(FACT_URL, ChuckNorrisFactResponse.class, NON_EXISTING_HTTP_PARAMS))
                .thenReturn(ERROR_RESPONSE);
        ChuckNorrisService myServiceUnderTest = new ChuckNorrisService(restTemplate);

        ChuckNorrisFact chuckNorrisFact = myServiceUnderTest.retrieveFact(NON_EXISTING_JOKE);

        assertThat(chuckNorrisFact, is(nullValue()));
    }

    @Test(expected = ResourceAccessException.class)
    public void serviceShouldPropagateException() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        when(restTemplate.getForEntity(FACT_URL, ChuckNorrisFactResponse.class, BAD_HTTP_PARAMS))
                .thenThrow(new ResourceAccessException("I/O error"));
        ChuckNorrisService myServiceUnderTest = new ChuckNorrisService(restTemplate);

        myServiceUnderTest.retrieveFact(BAD_JOKE);
    }
}
