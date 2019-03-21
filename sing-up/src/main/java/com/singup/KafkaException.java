package com.singup;

class KafkaException extends RuntimeException {

  KafkaException(String message, Throwable cause) {
    super(message, cause);
  }
}
