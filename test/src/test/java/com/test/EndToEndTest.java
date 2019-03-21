package com.test;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.HttpStatus.OK;

import com.common.User;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class EndToEndTest {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Autowired private UserRepository repository;
  @Autowired private RestTemplate restTemplate;

  @Value("${singup.rest.url}")
  private String singupRestUrl;

  @Test(timeout = 2000)
  public void endToEndTest() {
    User user = generateRandomUser();
    log.info(format("Generated user: %s", user));

    ResponseEntity<String> response = doSingupRequest(user);
    log.info(format("Gotten response: %s", response));

    assertEquals(OK, response.getStatusCode());
    assertTrue(isUUID(response.getBody()));

    UUID userId = toUuid(response.getBody());

    log.info(format("Finding user by id: %s", userId));
    User actualUser = blockingFindUserFromDB(userId);
    log.info(format("Found user: %s", actualUser));

    assertEquals(userId, actualUser.getUuid());
    assertEquals(user.getEmail(), actualUser.getEmail());
    assertEquals(user.getPassword(), actualUser.getPassword());
  }

  private User generateRandomUser() {
    return new User(null, generateEmail(), generatePassword());
  }

  private String generateEmail() {
    String availableEmailChars = "abcdefghijklmnopqrstuvwxyz";
    StringBuilder builder = new StringBuilder();
    Random random = new Random();

    for (int i = 0; i < 25; i++) {
      builder.append(availableEmailChars.charAt(random.nextInt(availableEmailChars.length())));
    }

    builder.append("@email.com");

    return builder.toString();
  }

  private String generatePassword() {
    String availablePasswordChars = "abcdefghijklmnopqrstuvwxyz";
    StringBuilder builder = new StringBuilder();
    Random random = new Random();

    for (int i = 0; i < 25; i++) {
      builder.append(
          availablePasswordChars.charAt(random.nextInt(availablePasswordChars.length())));
    }

    return builder.toString();
  }

  private ResponseEntity<String> doSingupRequest(User user) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

    HttpEntity<User> request = new HttpEntity<>(user, headers);

    return restTemplate.postForEntity(singupRestUrl + "users/", request, String.class);
  }

  private boolean isUUID(String string) {
    try {
      UUID.fromString(string);
      return true;
    } catch (IllegalArgumentException ex) {
      return false;
    }
  }

  private UUID toUuid(String string) {
    return UUID.fromString(string);
  }

  private User blockingFindUserFromDB(UUID id) {
    Optional<User> user;

    while (isNotPresent(user = repository.findById(id))) {
      sleepUninterruptibly(200, MILLISECONDS);
    }

    return user.orElseThrow(RuntimeException::new);
  }

  private <T> boolean isNotPresent(Optional<T> optional) {
    return !optional.isPresent();
  }
}
