package dev.muldev.productdetails.controller;

import dev.muldev.productdetails.exception.ResourceNotFoundException;
import dev.muldev.productdetails.model.ProductDetail;
import dev.muldev.productdetails.service.ProductDetailService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ProductDetailController.PRODUCTS_URL)
@AllArgsConstructor
public class ProductDetailController {
    public static final String PRODUCTS_URL = "/product";
    public static final String PRODUCT_ID_SIMILAR_URL = "/{productId}/similar";

    private final ProductDetailService productDetailService;

    @GetMapping(PRODUCT_ID_SIMILAR_URL)
    @ResponseStatus(HttpStatus.OK)
    @SneakyThrows
    public List<ProductDetail> getSimilarProducts(@PathVariable String productId){
        return this.productDetailService.getDetailsOfSimilarProducts(productId);
    }
}
