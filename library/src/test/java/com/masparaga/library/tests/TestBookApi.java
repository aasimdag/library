package com.masparaga.library.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masparaga.library.JsonHelper;
import com.masparaga.library.model.dto.SearchBookQuery;
import com.masparaga.library.model.requests.BuyingStepsRequest;
import com.masparaga.library.model.requests.SearchRequest;
import com.masparaga.library.model.responses.MessageResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("user3@gmail.com")
public class TestBookApi {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Autowired
    public TestBookApi(MockMvc mockMvc, ObjectMapper objectMapper){
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    private MessageResponse execute(String url, SearchBookQuery query) throws Exception {
        MvcResult result = this.mockMvc
                .perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(objectMapper, new SearchRequest<>(query))))
                .andExpect(status().isOk())
                .andReturn();

        return new MessageResponse(result.getResponse().getContentAsString());
    }

    @Test
    public void testSuccessfulNameSearch() throws Exception{
        SearchBookQuery query = new SearchBookQuery();
        query.setName("bookname");
        execute("/api/home/shop/search", query);
    }

    @Test
    public void testSuccessfulBuy() throws Exception{
        BuyingStepsRequest buyingStepsRequest = new BuyingStepsRequest("address", "cardInfos");

        MvcResult createResult = this.mockMvc
                .perform(post("/api/home/shop/buy/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(objectMapper, buyingStepsRequest)))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(createResult.getResponse().getContentAsString());
    }
}
