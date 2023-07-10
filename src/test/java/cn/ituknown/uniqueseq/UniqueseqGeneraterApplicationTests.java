package cn.ituknown.uniqueseq;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UniqueseqGeneraterApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    private static final Logger LOGGER = LoggerFactory.getLogger(UniqueseqGeneraterApplicationTests.class);

    @Test
    void contextLoads() throws Exception {

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/api/seq/take/{n}", 10)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"seqname\":\"commons\"}");

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> {
                    String response = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
                    LOGGER.info("response: {}", response);
                });
    }

}
