package com.persistence;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(
    classes = PersistenceApplication.class,
    properties = "kafka.topic.name=fake-kafka-topic-name")
@RunWith(SpringRunner.class)
public class PersistenceApplicationTest {

  @MockBean UserRepository userRepository;

  @Test
  public void contextLoads() {}
}
