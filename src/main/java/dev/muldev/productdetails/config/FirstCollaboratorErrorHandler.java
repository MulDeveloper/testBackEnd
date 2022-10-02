package dev.muldev.productdetails.config;

import dev.muldev.productdetails.exception.ResourceNotFoundException;
import dev.muldev.productdetails.exception.messages.Constants;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class FirstCollaboratorErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR
                || response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR;
    }

    @SneakyThrows
    @Override
    public void handleError(ClientHttpResponse response) {
        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new ResourceNotFoundException(Constants.NOT_FOUND);
        }
    }
}