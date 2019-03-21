package com.singup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(
    classes = SingupApplication.class,
    properties = "kafka.topic.name=fake-kafka-topic-name")
@RunWith(SpringRunner.class)
public class SingupApplicationTest {

  @Test
  public void contextLoads() {}
}
