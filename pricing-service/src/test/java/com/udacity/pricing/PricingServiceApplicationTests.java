package com.udacity.pricing;

import com.udacity.pricing.domain.price.Price;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PricingServiceApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void contextLoads(){
    }

    @Test
    public void eurekaReady() throws Exception {
        mockMvc.perform(get("localhost:" + 8082 + "/services/price?vehicleId=1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getPrice() {
        //happy path
        ResponseEntity<Price> responseEntity =
                restTemplate.getForEntity("http://192.168.31.55:" + port + "/services/price?vehicleId=1",
                        Price.class);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));

        responseEntity =
                restTemplate.getForEntity("http://192.168.31.55:" + port + "/services/price?vehicleId=19",
                        Price.class);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));

    }

    @Test
    public void getPriceBadRequest() {
        //bad request
        ResponseEntity<Price> responseEntity =
                restTemplate.getForEntity("http://192.168.31.55:" + port + "/services/price?vehicleId",
                        Price.class);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void getPriceException() {
        //exception
        ResponseEntity<Price> responseEntity =
                restTemplate.getForEntity("http://192.168.31.55:" + port + "/services/price?vehicleId=24",
                        Price.class);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));

    }

}
