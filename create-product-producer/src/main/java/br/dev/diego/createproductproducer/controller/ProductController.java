package br.dev.diego.createproductproducer.controller;

import br.dev.diego.createproductproducer.controller.request.CreateProductRequest;
import br.dev.diego.createproductproducer.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody CreateProductRequest request) {
        String productId = service.createdProduct(request);
        return ResponseEntity.status(CREATED).body("Product created id: " + productId);
    }

}
