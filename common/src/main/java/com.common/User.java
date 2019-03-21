package com.common;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;
import static java.lang.String.format;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Objects;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users_test")
public class User {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Id
  @Column(name = "id", nullable = false)
  private UUID uuid;

  @Email
  @NotNull
  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @NotNull
  @Column(name = "password", nullable = false)
  private String password;

  @JsonCreator
  public User(
      @JsonProperty("uuid") UUID uuid,
      @JsonProperty(value = "email") String email,
      @JsonProperty(value = "password") String password) {

    checkArgument(!isNullOrEmpty(email));
    checkArgument(!isNullOrEmpty(password));

    this.uuid = uuid;
    this.email = email;
    this.password = password;
  }

  public User() {}

  public static User fromJson(String json) {
    checkArgument(!isNullOrEmpty(json));

    try {
      return objectMapper.readValue(json, User.class);
    } catch (IOException e) {
      throw new UncheckedIOException(format("%s is not valid", json), e);
    }
  }

  public static String toJson(User user) {
    checkNotNull(user);

    try {
      return objectMapper.writeValueAsString(user);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(format("User %s is not valid", user), e);
    }
  }

  @JsonProperty("uuid")
  public UUID getUuid() {
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  @JsonProperty("password")
  public String getPassword() {
    return password;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equal(uuid, user.uuid)
        && Objects.equal(email, user.email)
        && Objects.equal(password, user.password);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(uuid, email, password);
  }

  @Override
  public String toString() {
    return format("User{uuid=%s, email='%s', password='%s'}", uuid, email, password);
  }
}
