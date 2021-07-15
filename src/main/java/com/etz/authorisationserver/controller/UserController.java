package com.etz.authorisationserver.controller;

import com.etz.authorisationserver.dto.request.CreateUserRequest;
import com.etz.authorisationserver.dto.request.ResetTokenRequestModel;
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
        response.setMessage("UserEntity Created Successfully");//send a notification to provided email next..
        return new ResponseEntity<>(response, HttpStatus.CREATED);//..that came with the create user payload
    }//..then create a securityquestion controller/endpoint that will be called upon first login, find a way to track 1st time login
    //...the contoller takes the security questions and answers and persist it in a security question table(one to many relationships)	
    //..ask the user to change the default password, within the same controller, using passwordresetController
    //..this controller will take the provided email addr and check against the db if exist it will send an OTP
    //..to the same email. Deliberate password change shd call securityquestion controller which will return the security questions
    //..collect the answers, check them against the record on the db, if correct, request for current password
    
    
    
    
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
