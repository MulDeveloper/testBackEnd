package dev.muldev.productdetails.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.muldev.productdetails.exception.ControllerExceptionAdvice;
import dev.muldev.productdetails.exception.ResourceNotFoundException;
import dev.muldev.productdetails.exception.messages.Constants;
import dev.muldev.productdetails.model.ProductDetail;
import dev.muldev.productdetails.service.ProductDetailService;
import dev.muldev.productdetails.shared.BaseControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static dev.muldev.productdetails.shared.TestUtils.mapFromJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductControllerTest extends BaseControllerTest {

    @Mock
    private ProductDetailService productDetailService;

    @InjectMocks
    private ProductDetailController productDetailController;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        this.mvc = MockMvcBuilders.standaloneSetup(productDetailController)
                                  .setControllerAdvice(new ControllerExceptionAdvice())
                                  .build();
    }

    @Test
    void should_return_mocked_result() throws Exception {
        List<ProductDetail> mockedList = new ArrayList<>();
        ProductDetail productDetailOne = new ProductDetail();

        productDetailOne.setId("9999");
        productDetailOne.setName("myName");
        productDetailOne.setAvailability(true);
        productDetailOne.setPrice(BigDecimal.valueOf(100));

        mockedList.add(productDetailOne);

        when(productDetailService.getDetailsOfSimilarProducts("1")).thenReturn(mockedList);

        final var result = this.mvc.perform(
                get(getResourceUrl(ProductDetailController.PRODUCT_ID_SIMILAR_URL), "1")
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        List<ProductDetail> resultDto = Arrays.asList(mapFromJson(result.andReturn().getResponse().getContentAsString(), ProductDetail[].class));

        assertEquals(1, resultDto.size());
        assertEquals("myName", resultDto.get(0).getName());

    }

    @Test()
    public void throw_not_found() throws Exception {
        when(productDetailService.getDetailsOfSimilarProducts("1")).thenThrow(new ResourceNotFoundException(Constants.NOT_FOUND));


        final var result = this.mvc.perform(
                get(getResourceUrl(ProductDetailController.PRODUCT_ID_SIMILAR_URL), "1")
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

    }


    @Override
    protected String getBasePath() {
        return ProductDetailController.PRODUCTS_URL;
    }
}
