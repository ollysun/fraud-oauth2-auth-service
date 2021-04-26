package com.etz.authorisationserver.controller;

import com.etz.authorisationserver.dto.request.CreateUserRequest;
import com.etz.authorisationserver.dto.request.UpdateUserRequest;
import com.etz.authorisationserver.dto.response.*;
import com.etz.authorisationserver.services.UserEntityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/users")
@Validated
public class UserController {

    @Autowired
    private UserEntityService userService;

    @PostMapping
    public ResponseEntity<ModelResponse<UserResponse>> createUser(@Valid @RequestBody CreateUserRequest request){
        ModelResponse<UserResponse> response = new ModelResponse<>(userService.createUser(request));
        response.setStatus(HttpStatus.CREATED.value());
        response.setMessage("UserEntity Created Successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<CollectionResponse<UserResponse>> getUser(
                  @RequestParam(name = "userId", required = false) Long userId,
                  @RequestParam(value = "status", required = false) Boolean status){

        List<UserResponse> userResponseList = userService.getAllUsers(userId, status);
        CollectionResponse<UserResponse> collectionResponse = new CollectionResponse<>(userResponseList);
        collectionResponse.setMessage("All Users");
        return new ResponseEntity<>(collectionResponse, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<StringResponse> updateUser(@RequestBody @Valid UpdateUserRequest request){
        StringResponse result = new StringResponse();
        Boolean boolVal = userService.updateUser(request);
        if(Boolean.TRUE.equals(boolVal)){
            result.setMessage("UserEntity Updated");
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
