//package shallwe.movie.security;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import shallwe.movie.security.controller.SecurityMessage;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//
//@WebMvcTest
//public class UserAccessTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private ObjectMapper mapper;
//
//    @DisplayName("1. user 로 user 페이지를 접근할 수 있다.")
//    @Test
//    @WithMockUser(username = "user",roles = {"USER"})
//    void test_user_access_userPage() throws Exception {
//        String resp = mockMvc.perform(get("/user"))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        SecurityMessage message = mapper.readValue(resp, SecurityMessage.class);
//        Assertions.assertThat("user page").isEqualTo(message.getMessage());
//    }
//
//    @DisplayName("2. user 로 admin 페이지를 접근할 수 없다.")
//    @Test
//    @WithMockUser(username = "user",roles = {"USER"})
//    void test_user_cannot_access_adminPage() throws Exception {
//          mockMvc.perform(get("/admin"))
//                  .andExpect(status().is2xxSuccessful());
//    }
//
//    @DisplayName("3. admin 로 user 페이지와 admin 페이지를 접근할 수 있다.")
//    @Test
//    @WithMockUser(username = "admin",roles = {"ADMIN"})
//    void test_admin_can_access_user_and_admin() throws Exception {
//        String resp = mockMvc.perform(get("/user"))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        SecurityMessage message1 = mapper.readValue(resp, SecurityMessage.class);
//        Assertions.assertThat("user page").isEqualTo(message1.getMessage());
//
//        resp = mockMvc.perform(get("/admin"))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        SecurityMessage message2 = mapper.readValue(resp, SecurityMessage.class);
//        Assertions.assertThat("admin page").isEqualTo(message2.getMessage());
//    }
//}
