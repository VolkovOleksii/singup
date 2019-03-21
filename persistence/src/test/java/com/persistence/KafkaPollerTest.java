package com.persistence;

import static com.common.User.toJson;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.common.User;
import java.util.UUID;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;

@RunWith(MockitoJUnitRunner.class)
public class KafkaPollerTest {

  @Mock private UserRepository repository;
  @Mock private ConsumerRecord<UUID, String> record;

  @InjectMocks private KafkaPoller poller;

  @Test
  public void consumeValidConsumerRecordSuccessPersist() {
    User user = new User(UUID.randomUUID(), "user_name@email.com", "password");
    when(record.value()).thenReturn(toJson(user));
    when(repository.save(user)).thenReturn(user);

    poller.consume(record);

    verify(repository).save(user);
  }

  @Test(expected = RuntimeException.class)
  public void consumeNonValidConsumerRecordExceptionThrown() {
    when(record.value()).thenReturn("{}");

    poller.consume(record);
  }

  @Test(expected = DataIntegrityViolationException.class)
  public void consumeValidAlreadyExistEmailExceptionThrown() {
    User user = new User(UUID.randomUUID(), "user_name@email.com", "password");
    when(record.value()).thenReturn(toJson(user));
    when(repository.save(user)).thenThrow(new DataIntegrityViolationException(""));

    poller.consume(record);
  }
}
