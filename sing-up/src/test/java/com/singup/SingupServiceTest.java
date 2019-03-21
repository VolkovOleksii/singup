package com.singup;

import static com.common.User.toJson;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.common.User;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SingupServiceTest {

  @Mock KafkaRepository repository;
  @Spy @InjectMocks SingupService service;

  @Test
  public void createUserValidUserSuccessSend() {
    UUID uuid = UUID.randomUUID();
    User expectedUser = new User(uuid, "user_name@email.com", "password");

    when(service.generateUuid()).thenReturn(uuid);

    UUID actualUuid = service.createUser(new User(null, "user_name@email.com", "password"));

    assertEquals(uuid, actualUuid);
    verify(repository).send(expectedUser.getUuid().toString(), toJson(expectedUser));
  }
}
