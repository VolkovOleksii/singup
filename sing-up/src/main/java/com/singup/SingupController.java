package com.singup;

import com.common.User;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class SingupController {

  private final SingupService service;

  public SingupController(SingupService service) {
    this.service = service;
  }

  @PostMapping
  String createUser(@RequestBody @Valid User user) {
    return service.createUser(user).toString();
  }
}
