package de.codecentric.example.clean.test.code.chucknorris;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.codecentric.example.clean.test.code.chucknorris.domain.ChuckNorrisFact;

public class ChuckNorrisFactResponse {

    private final String type;
    private final ChuckNorrisFact fact;
    private final String errorMessage;

    public ChuckNorrisFactResponse(@JsonProperty("type") String type, @JsonProperty("value") ChuckNorrisFact fact){
        this.type = type;
        this.fact = fact;
        this.errorMessage = null;
    }

    public ChuckNorrisFactResponse(@JsonProperty("type") String type, @JsonProperty("value") String errorResponse) {
        this.type = type;
        this.fact = null;
        this.errorMessage = errorResponse;
    }

    public String getType() {
        return type;
    }

    public ChuckNorrisFact getFact() {
        return fact;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
