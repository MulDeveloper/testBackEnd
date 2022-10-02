package dev.muldev.productdetails.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SecondaryCollaboratorErrorHandlerTest{

    SecondaryCollaboratorErrorHandler secondaryCollaboratorErrorHandler;
    ClientHttpResponse clientHttpResponse;

    @BeforeEach
    void setup(){
        secondaryCollaboratorErrorHandler = new SecondaryCollaboratorErrorHandler();
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
    void should_return_has_error() throws IOException {
        assertTrue(this.secondaryCollaboratorErrorHandler.hasError(clientHttpResponse));
    }


}
