package com.singup;

import static com.common.User.toJson;
import static com.google.common.base.Preconditions.checkNotNull;

import static java.lang.String.format;

import com.common.User;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
class SingupService {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final KafkaRepository repository;

  SingupService(KafkaRepository repository) {
    this.repository = repository;
  }

  UUID createUser(User user) {
    checkNotNull(user);

    user.setUuid(generateUuid());

    log.info(format("Sending user: %s", user));
    repository.send(user.getUuid().toString(), toJson(user));
    log.info(format("User: %s is sent", user));

    return user.getUuid();
  }

  UUID generateUuid() {
    return UUID.randomUUID();
  }
}
