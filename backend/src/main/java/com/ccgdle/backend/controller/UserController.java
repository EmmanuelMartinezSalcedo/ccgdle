package com.ccgdle.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ccgdle.backend.dto.user.CreateUserResponse;
import com.ccgdle.backend.dto.user.AttemptRequest;
import com.ccgdle.backend.dto.user.AttemptResponse;
import com.ccgdle.backend.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @PostMapping("/create")
  public ResponseEntity<CreateUserResponse> createUser() {
    return ResponseEntity.ok(new CreateUserResponse(userService.createUser().getId()));
  }

  @PostMapping("/attempt")
  public ResponseEntity<AttemptResponse> attempt(@RequestBody AttemptRequest request) {
    return ResponseEntity.ok(userService.attempt(request));
  }
}
