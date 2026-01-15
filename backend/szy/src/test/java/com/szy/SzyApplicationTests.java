package com.szy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.szy.entity.Dict;
import com.szy.entity.DictDetail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class SzyApplicationTests {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    void contextLoads() {
//        // 验证Spring上下文加载
//    }
//
//    @Test
//    @WithMockUser(authorities = "xtgl_yhgl") // 模拟具有权限的用户
//    void testSaveDict() throws Exception {
//        // 模拟get请求
//        mockMvc.perform(get("/dict/list?blurry=&currentPage=1&pageSize=10")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk()) // 期望返回状态码为200
//                .andExpect(jsonPath("$.code").value(200)) // 期望返回的JSON中包含code = 200
//                .andExpect(jsonPath("$.data.content").exists()) // 期望返回的JSON中有content字段
//                .andExpect(jsonPath("$.data.totalElements").exists());
//    }
//
//    @Test
//    @WithMockUser(authorities = "xtgl_yhgl")
//    void getById_whenDictDetailExists_shouldReturnOk() throws Exception {
//        // 发起POST请求并检查返回结果
//        mockMvc.perform(post("/dict-detail/delete/1")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }
}
