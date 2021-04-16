package com.etz.authorisationserver.controller;

import com.etz.authorisationserver.dto.request.CreateUserRequest;
import com.etz.authorisationserver.dto.response.ModelResponse;
import com.etz.authorisationserver.dto.response.UserResponse;
import com.etz.authorisationserver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping
    public ResponseEntity<ModelResponse<UserResponse>> createUser(@RequestBody @Valid CreateUserRequest request){
        ModelResponse<UserResponse> response = new ModelResponse<>(userService.createUser(request));
        response.setStatus(201);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
