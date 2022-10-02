package dev.muldev.productdetails.service;

import dev.muldev.productdetails.exception.ResourceNotFoundException;
import dev.muldev.productdetails.exception.messages.Constants;
import dev.muldev.productdetails.model.ProductDetail;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProductDetailServiceTest {

    @Mock
    private RestTemplate restTemplateFirstApi;

    @Mock
    private RestTemplate restTemplateSecondApi;

    @InjectMocks
    private ProductDetailService sut;

    @Value("${collaborator.api.url.product.similarids}")
    private String collaboratorApiSimilarIdsUrl;

    @Value("${collaborator.api.url.product}")
    private String collaboratorApiProductDetails;


    @Test
    void should_return_similar_products() throws ResourceNotFoundException {
        ReflectionTestUtils.setField(sut, "collaboratorApiSimilarIdsUrl", collaboratorApiSimilarIdsUrl);
        ReflectionTestUtils.setField(sut, "collaboratorApiProductDetails", collaboratorApiProductDetails);

        //mock both responses form collaborator services

        //similarids

        String [] similarIds = {"2"};

        Map<String, String> params = new HashMap<>();
        params.put("productId", "1");

        UriComponents uriSimilarIds = UriComponentsBuilder
                .fromUriString(collaboratorApiSimilarIdsUrl)
                .buildAndExpand(params);

        when(restTemplateFirstApi.getForEntity(uriSimilarIds.toUri(), String[].class)).thenReturn(ResponseEntity.ok(similarIds));

        //product details

        ProductDetail productDetail = new ProductDetail();
        productDetail.setId("9999");
        productDetail.setName("myName");
        productDetail.setAvailability(true);
        productDetail.setPrice(BigDecimal.valueOf(100));

        Map<String, String> paramProductDetailsUrl = new HashMap<>();
        paramProductDetailsUrl.put("productId", "2");

        UriComponents uriProductDetails = UriComponentsBuilder
                .fromUriString(collaboratorApiProductDetails)
                .buildAndExpand(paramProductDetailsUrl);

        when(restTemplateSecondApi.getForEntity(uriProductDetails.toUri(), ProductDetail.class)).thenReturn(ResponseEntity.ok(productDetail));

        //response

        List<ProductDetail> mockedResponse = this.sut.getDetailsOfSimilarProducts("1");

        assertEquals(productDetail.getName(), mockedResponse.get(0).getName());


    }

    @Test()
    public void throw_not_found_collaborator() throws Exception {
        ReflectionTestUtils.setField(sut, "collaboratorApiSimilarIdsUrl", collaboratorApiSimilarIdsUrl);

        Map<String, String> params = new HashMap<>();
        params.put("productId", "1");

        UriComponents uriSimilarIds = UriComponentsBuilder
                .fromUriString(collaboratorApiSimilarIdsUrl)
                .buildAndExpand(params);


        when(restTemplateFirstApi.getForEntity(uriSimilarIds.toUri(), String[].class)).thenReturn(ResponseEntity.ok(null));

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> this.sut.getDetailsOfSimilarProducts("1")
        );

        assertEquals(Constants.NOT_FOUND, thrown.getMessage());
    }

}
