package de.codecentric.example.clean.test.code.chucknorris;

import de.codecentric.example.clean.test.code.chucknorris.domain.ChuckNorrisFact;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

import javax.annotation.PostConstruct;

@Service
public class ChuckNorrisService {

    static final String FACT_URL = "http://api.icndb.com/jokes/{id}";

    private final RestTemplate restTemplate;

    @Autowired
    public ChuckNorrisService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ChuckNorrisFact retrieveFact(Long id) {
        Map<String, Long> uriVariables = Collections.singletonMap("id", id);
        ResponseEntity<ChuckNorrisFactResponse> response = restTemplate.getForEntity(FACT_URL, ChuckNorrisFactResponse.class, uriVariables);
        return response.getBody().getFact();
    }

}
