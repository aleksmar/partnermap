package com.example.partnermap.customer;

import com.example.partnermap.Configuration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import static com.example.partnermap.security.SecurityConstants.HEADER;
import static com.example.partnermap.security.SecurityConstants.TOKEN_PREFIX;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Configuration.class)
@AutoConfigureMockMvc
public class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenNoToken_thenReturnNotAuthorized() throws Exception {
        RequestBuilder req = get("/customers/1");
        mockMvc.perform(req)
                .andExpect(status().isForbidden());
    }

    @Test
    public void whenUrlWithWrongToken_thenReturnForbidden() throws Exception {
        RequestBuilder req = get("/customers/1")
                .header(HEADER, TOKEN_PREFIX + "2");
        mockMvc.perform(req).andExpect(status().isForbidden());
    }

    @Test
    public void whenGoodTokenAndNoSuchId_thenReturnNotFound() throws Exception {
        RequestBuilder req = get("/customers/@me")
                .header(HEADER, TOKEN_PREFIX + "2");
        mockMvc.perform(req).andExpect(status().isNotFound());
    }

    @Test
    public void whenMeWithToken_thenReturnCustomer() throws Exception {
        RequestBuilder req = get("/customers/@me")
                .header(HEADER, TOKEN_PREFIX + "1");
        mockMvc.perform(req)
                .andExpect(status().isOk());
    }

    @Test
    public void whenUrlWithGoodToken_thenReturnCustomer() throws Exception {
        RequestBuilder req = get("/customers/1")
                .header(HEADER, TOKEN_PREFIX + "1");
        mockMvc.perform(req).andDo(print()).andExpect(status().isOk());
    }
}