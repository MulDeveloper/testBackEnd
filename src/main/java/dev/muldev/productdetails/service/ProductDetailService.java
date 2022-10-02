package dev.muldev.productdetails.service;


import dev.muldev.productdetails.exception.ResourceNotFoundException;
import dev.muldev.productdetails.exception.messages.Constants;
import dev.muldev.productdetails.model.ProductDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductDetailService {

    @Autowired
    private RestTemplate restTemplateFirstApi;

    @Autowired
    private RestTemplate restTemplateSecondApi;

    @Value("${collaborator.api.url.product.similarids}")
    private String collaboratorApiSimilarIdsUrl;

    @Value("${collaborator.api.url.product}")
    private String collaboratorApiProductDetails;

    public List<ProductDetail> getDetailsOfSimilarProducts(String productId) throws ResourceNotFoundException {
        String[] similarIds = this.getSimilarIds(productId);

        if(similarIds == null){
            throw new ResourceNotFoundException(Constants.NOT_FOUND);
        }

        List<ProductDetail> responseList = new ArrayList<>();
        for(String id: similarIds){
            ProductDetail details = this.getDetailsFromProduct(id);
            if(details != null && details.getId() != null) {
                responseList.add(details);
            }
        }
        return responseList;
    }

    private String[] getSimilarIds(String productId) throws ResourceNotFoundException {
        Map<String, String> params = new HashMap<>();
        params.put("productId", productId);

        UriComponents uri = UriComponentsBuilder
                .fromUriString(collaboratorApiSimilarIdsUrl)
                .buildAndExpand(params);

        ResponseEntity<String[]> response = this.restTemplateFirstApi.getForEntity(uri.toUri(), String[].class);
        if(response.getStatusCode() == HttpStatus.NOT_FOUND){
            throw new ResourceNotFoundException(Constants.NOT_FOUND);
        }

        return (String[]) this.getBody(response);
    }

    private ProductDetail getDetailsFromProduct(String productId) {
        Map<String, String> params = new HashMap<>();
        params.put("productId", productId);

        UriComponents uri = UriComponentsBuilder
                .fromUriString(collaboratorApiProductDetails)
                .buildAndExpand(params);

        ResponseEntity<ProductDetail> response = this.restTemplateSecondApi.getForEntity(uri.toUri(), ProductDetail.class);

        if(response.getStatusCode() == HttpStatus.OK) {
            return (ProductDetail) this.getBody(response);
        }

        return null;

    }

    private Object getBody(ResponseEntity response){
        if(response.getBody() == null){
            return null;
        }
        return response.getBody();
    }

}
