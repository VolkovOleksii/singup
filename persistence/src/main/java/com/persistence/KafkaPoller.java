package com.persistence;

import static com.common.User.fromJson;
import static java.lang.String.format;

import com.common.User;
import java.util.UUID;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
class KafkaPoller {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final UserRepository repository;

  KafkaPoller(UserRepository repository) {
    this.repository = repository;
  }

  @KafkaListener(topics = "${kafka.topic.name}")
  void consume(ConsumerRecord<UUID, String> record) {

    User user = fromJson(record.value());

    log.info(format("Persisting user %s", user));
    user = repository.save(user);
    log.info(format("User %s is persisted", user));
  }
}
