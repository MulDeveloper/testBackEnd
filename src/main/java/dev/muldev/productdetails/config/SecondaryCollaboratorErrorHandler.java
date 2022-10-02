package dev.muldev.productdetails.config;

import dev.muldev.productdetails.exception.messages.Constants;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Slf4j
public class SecondaryCollaboratorErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR
                || response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR;
    }

    @SneakyThrows
    @Override
    public void handleError(ClientHttpResponse response) {
        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            log.error(Constants.COLLABORATOR_NOT_FOUND);
        }
        if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
            log.error(Constants.COLLABORATOR_INTERNAL_SERVER_ERROR);
        }
    }
}