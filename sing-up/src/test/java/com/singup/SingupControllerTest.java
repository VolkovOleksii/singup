package com.singup;

import static com.common.User.toJson;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.common.User;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(SingupController.class)
public class SingupControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private SingupService service;

  @Test
  public void createUserValidUserJsonReturnUserUuid() throws Exception {
    UUID uuid = UUID.randomUUID();

    when(service.createUser(new User(null, "user_name@email.com", "password"))).thenReturn(uuid);

    this.mockMvc
        .perform(
            post("/users")
                .contentType(APPLICATION_JSON)
                .content(toJson(new User(null, "user_name@email.com", "password"))))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(TEXT_PLAIN))
        .andExpect(content().string(uuid.toString()));
  }

  @Test
  public void createUserIllegalEmailBadRequest() throws Exception {
    this.mockMvc
        .perform(
            post("/users")
                .contentType(APPLICATION_JSON)
                .content(toJson(new User(null, "IllegalEmail", "password"))))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentTypeCompatibleWith(TEXT_PLAIN))
        .andExpect(content().string("KO"));
  }
}
