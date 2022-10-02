package dev.muldev.productdetails.config;

import dev.muldev.productdetails.exception.ResourceNotFoundException;
import dev.muldev.productdetails.exception.messages.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

public class FirstCollaboratorErrorHandlerTest {

    FirstCollaboratorErrorHandler firstCollaboratorErrorHandler;
    ClientHttpResponse clientHttpResponse;

    @BeforeEach
    void setup(){
        firstCollaboratorErrorHandler = new FirstCollaboratorErrorHandler();
        clientHttpResponse = new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.NOT_FOUND;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return 404;
            }

            @Override
            public String getStatusText() throws IOException {
                return "Not Found";
            }

            @Override
            public void close() {

            }

            @Override
            public InputStream getBody() throws IOException {
                return null;
            }

            @Override
            public HttpHeaders getHeaders() {
                return null;
            }
        };

    }


    @Test
    void should_throw_not_found(){
        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> this.firstCollaboratorErrorHandler.handleError(clientHttpResponse)
        );

        assertEquals(Constants.NOT_FOUND, thrown.getMessage());
    }

    @Test
    void should_return_has_error() throws IOException {
        assertTrue(this.firstCollaboratorErrorHandler.hasError(clientHttpResponse));
    }


}
