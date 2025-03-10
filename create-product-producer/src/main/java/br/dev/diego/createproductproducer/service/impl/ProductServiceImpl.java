package br.dev.diego.createproductproducer.service.impl;

import br.dev.diego.createproductproducer.controller.request.CreateProductRequest;
import br.dev.diego.createproductproducer.service.ProductCreatedEvent;
import br.dev.diego.createproductproducer.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class ProductServiceImpl implements ProductService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    public ProductServiceImpl(KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String createdProduct(CreateProductRequest request) {
        String productId = UUID.randomUUID().toString();

        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(
                productId,
                request.title(),
                request.price(),
                request.quantity()
        );
        log.info("Antes de solicitar o envio da mensagem....");

        try {
            kafkaTemplate.send("product-created-events-topic", productId, productCreatedEvent).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        log.info("Returning productId {}", productId);
        return productId;
    }
}
