package com.example.partnermap.partner;

import com.example.partnermap.Configuration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static com.example.partnermap.security.SecurityConstants.HEADER;
import static com.example.partnermap.security.SecurityConstants.TOKEN_PREFIX;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Configuration.class)
@AutoConfigureMockMvc
public class PartnerMappingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private static final String TEST_BODY =
            "{\"partnerId\": \"123abc\"," +
                    "\"accountId\": \"f1f31ff3\"," +
                    "\"name\": \"partner_mapping_1\"}";
    private static final String VALID_CUSTOMER_ID = "1";
    private static final String INVALID_CUSTOMER_ID = "100";
    private static final String VALID_MAPPING_ID = "4";
    private static final String INVALID_MAPPING_ID = "123";

    @Test
    public void whenPostWithInvalidCustomer_returnError() throws Exception {
        MockHttpServletRequestBuilder req =
                post("/customers/@me/mappings")
                        .header(HEADER, TOKEN_PREFIX + INVALID_CUSTOMER_ID)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(TEST_BODY);

        mockMvc.perform(req)
                .andExpect(status().isNotFound());
    }

    @Test
    public void addMappingTest() throws Exception {
        MockHttpServletRequestBuilder req =
                post("/customers/" + VALID_CUSTOMER_ID + "/mappings")
                        .header(HEADER, TOKEN_PREFIX + VALID_CUSTOMER_ID)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(TEST_BODY);

        mockMvc.perform(req)
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().json("{\"partnerId\": \"123abc\"}"));
    }

    @Test
    public void whenGetMappingWithInvalidCustomer_thenReturnNotFound() throws Exception {
        MockHttpServletRequestBuilder req =
                get("/customers/@me/mappings")
                        .header(HEADER, TOKEN_PREFIX + INVALID_CUSTOMER_ID)
                        .contentType(APPLICATION_JSON_UTF8);

        mockMvc.perform(req)
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenGetInvalidMappingId_thenReturnNotFound() throws Exception {
        MockHttpServletRequestBuilder req =
                get("/customers/@me/mappings/" + INVALID_MAPPING_ID)
                .header(HEADER, TOKEN_PREFIX + VALID_CUSTOMER_ID)
                .contentType(APPLICATION_JSON_UTF8);

        mockMvc.perform(req)
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(INVALID_MAPPING_ID)));
    }

    @Test
    public void whenGoodGetRequest_thenReturnMappings() throws Exception {

        MockHttpServletRequestBuilder postReq =
                post("/customers/" + VALID_CUSTOMER_ID + "/mappings")
                        .header(HEADER, TOKEN_PREFIX + VALID_CUSTOMER_ID)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(TEST_BODY);
        mockMvc.perform(postReq);

        MockHttpServletRequestBuilder req =
                get("/customers/@me/mappings")
                        .header(HEADER, TOKEN_PREFIX + VALID_CUSTOMER_ID)
                        .contentType(APPLICATION_JSON_UTF8);

        mockMvc.perform(req)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void whenPutInvalidCustomer_thenReturnError() throws Exception {
        MockHttpServletRequestBuilder req =
                put("/customers/@me/mappings/" + VALID_MAPPING_ID)
                        .header(HEADER, TOKEN_PREFIX + INVALID_CUSTOMER_ID)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(TEST_BODY);

        mockMvc.perform(req)
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenPutValidMapping_thenReturnUpdatedMapping() throws Exception {
        MockHttpServletRequestBuilder req =
                put("/customers/@me/mappings/" + VALID_MAPPING_ID)
                        .header(HEADER, TOKEN_PREFIX + VALID_CUSTOMER_ID)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(TEST_BODY);


        mockMvc.perform(req)
                .andExpect(status().isOk())
                .andExpect(content().json("{\"customerId\":" + VALID_CUSTOMER_ID + "}"));
    }

    @Test
    public void whenDeleteInvalidCustomer_thenReturnError() throws Exception {
        MockHttpServletRequestBuilder req =
                delete("/customers/@me/mappings/" + VALID_CUSTOMER_ID)
                        .header(HEADER, TOKEN_PREFIX + INVALID_CUSTOMER_ID)
                        .contentType(APPLICATION_JSON_UTF8);

        mockMvc.perform(req)
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenDeleteInvalidMapping_thenReturnError() throws Exception {
        MockHttpServletRequestBuilder req =
                delete("/customers/@me/mappings/" + INVALID_CUSTOMER_ID)
                        .header(HEADER, TOKEN_PREFIX + VALID_CUSTOMER_ID)
                        .contentType(APPLICATION_JSON_UTF8);

        mockMvc.perform(req)
                .andExpect(status().isNotFound());
    }
}