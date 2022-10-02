package dev.muldev.productdetails.shared;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class BaseControllerTest {

    @Autowired
    protected MockMvc mvc;

    protected abstract String getBasePath();

    protected final String getResourceUrl(String reltiveUrlResource) {
        if(reltiveUrlResource.isEmpty()){
            return getBasePath();
        }
        return getBasePath() + reltiveUrlResource;
    }

}
