package br.dev.diego.emailnotificationservice.handler;

import br.dev.diego.core.ProductCreatedEvent;
import br.dev.diego.emailnotificationservice.error.RetryableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
@KafkaListener(topics = "product-created-events-topic")
public class ProductCreatedEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductCreatedEventHandler.class);

    private final RestTemplate restTemplate;

    public ProductCreatedEventHandler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @KafkaHandler
    public void handle(ProductCreatedEvent event) {
        LOGGER.info("Handling product created event: {}", event);
//        if(true) {
//            throw new NotRetryableException("Simulated exception for testing error handling");
//        }
        String requestUrl = "http://localhost:8084/responses/500";

        try {
            ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, null, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                LOGGER.info("Successfully handled product created event: {}", event);
            }
        } catch (ResourceAccessException ex) {
            LOGGER.error("Failed to handle product created event due to network error: {}", ex.getMessage());
            throw new RetryableException("Failed to handle product created event, non-2xx response received");
        } catch (HttpServerErrorException ex) {
            LOGGER.error("Failed to handle product created event due to server error: {}", ex.getMessage());
            throw new RetryableException("Failed to handle product created event, non-2xx response received");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error while handling product created event: {}", ex.getMessage());
            throw new RetryableException("Unexpected error while handling product created event");
        }

    }

}
