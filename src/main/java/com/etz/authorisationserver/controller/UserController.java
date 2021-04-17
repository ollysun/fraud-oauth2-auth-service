package com.etz.authorisationserver.controller;

import com.etz.authorisationserver.dto.request.CreateUserRequest;
import com.etz.authorisationserver.dto.request.UpdateUserRequest;
import com.etz.authorisationserver.dto.response.*;
import com.etz.authorisationserver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Validated
@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<ModelResponse<UserResponse>> createUser(@RequestBody @Valid CreateUserRequest request){
        ModelResponse<UserResponse> response = new ModelResponse<>(userService.createUser(request));
        response.setStatus(HttpStatus.CREATED);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<CollectionResponse<UserResponse>> getUser(
                  @RequestParam(name = "userId", defaultValue = "-1", required = false) Long userId,
                  @RequestParam(value = "status", required = false) Boolean status){

        List<UserResponse> userResponseList = userService.getAllUsers(userId, status);
        CollectionResponse<UserResponse> collectionResponse = new CollectionResponse<>(userResponseList);
        return new ResponseEntity<>(collectionResponse, HttpStatus.OK);
    }

    @PutMapping(path = "/{userId}")
    public ResponseEntity<StringResponse> updateUser(@PathVariable(name = "userid") Long userid,
                                                                         @RequestBody @Valid UpdateUserRequest request){
        StringResponse result = new StringResponse();
        Boolean boolVal = userService.updateUser(request, userid);
        if(Boolean.TRUE.equals(boolVal)){
            result.setData("User Updated");
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
