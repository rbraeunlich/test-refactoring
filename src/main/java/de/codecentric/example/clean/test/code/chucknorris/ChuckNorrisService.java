package de.codecentric.example.clean.test.code.chucknorris;

import de.codecentric.example.clean.test.code.chucknorris.domain.ChuckNorrisFact;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class ChuckNorrisService {

    static final String RANDOM_FACT_URL = "http://api.icndb.com/jokes/random/{id}";

    private final RestTemplate restTemplate;

    @Autowired
    public ChuckNorrisService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ChuckNorrisFact retrieveRandomFact(Long id) {
        Map<String, Long> uriVariables = Collections.singletonMap("id", id);
        ResponseEntity<ChuckNorrisFactResponse> response = restTemplate.getForEntity(RANDOM_FACT_URL, ChuckNorrisFactResponse.class, uriVariables);
        return response.getBody().getFact();
    }
}
