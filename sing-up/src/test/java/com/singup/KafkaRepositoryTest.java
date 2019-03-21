package com.singup;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

@RunWith(MockitoJUnitRunner.class)
public class KafkaRepositoryTest {

  @Mock KafkaTemplate<String, String> kafkaTemplate;
  @Mock ListenableFuture<SendResult<String, String>> mockedFuture;

  @InjectMocks KafkaRepository repository;

  @Test
  public void sendValidKeyValidMessageSuccessfulSend()
      throws ExecutionException, InterruptedException {

    when(kafkaTemplate.send(any(), eq("key"), eq("message"))).thenReturn(mockedFuture);
    when(mockedFuture.get()).thenReturn(mock(SendResult.class));

    repository.send("key", "message");
  }

  @Test(expected = IllegalArgumentException.class)
  public void sendEmptyKeyExceptionThrown() {
    repository.send("", "message");
  }

  @Test(expected = IllegalArgumentException.class)
  public void sendNullKeyExceptionThrown() {
    repository.send(null, "message");
  }

  @Test(expected = NullPointerException.class)
  public void sendNullValueExceptionThrown() {
    repository.send("key", null);
  }

  @Test(expected = KafkaException.class)
  public void sendKafkaErrorOccurredExceptionThrown()
      throws ExecutionException, InterruptedException {
    when(kafkaTemplate.send(any(), eq("key"), eq("message"))).thenReturn(mockedFuture);
    when(mockedFuture.get()).thenThrow(new ExecutionException("", new RuntimeException()));

    repository.send("key", "message");
  }
}
