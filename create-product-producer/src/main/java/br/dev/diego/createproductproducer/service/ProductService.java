package br.dev.diego.createproductproducer.service;

import br.dev.diego.createproductproducer.controller.request.CreateProductRequest;

public interface ProductService {

    String createdProduct(CreateProductRequest request);

}
