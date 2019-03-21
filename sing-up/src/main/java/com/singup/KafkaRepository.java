package com.singup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ExecutionException;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;
import static java.lang.String.format;

@Repository
public class KafkaRepository {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final String topicName;

  public KafkaRepository(
      KafkaTemplate<String, String> kafkaTemplate, @Value("${kafka.topic.name}") String topicName) {
    this.kafkaTemplate = kafkaTemplate;
    this.topicName = topicName;
  }

  void send(String key, String value) {
    checkArgument(!isNullOrEmpty(key));
    checkNotNull(value);

    try {
      kafkaTemplate.send(topicName, key, value).get();
    } catch (InterruptedException | ExecutionException e) {
      log.error(
          format("An error occurred while sending message [key:%s, value:%s]", key, value), e);
      throw new KafkaException(
          format("An error occurred while sending message [key:%s, value:%s]", key, value), e);
    }
  }
}
