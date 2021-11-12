package com.etz.authorisationserver.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.etz.authorisationserver.dto.request.ApprovalRequest;
import com.etz.authorisationserver.dto.request.CreateUserRequest;
import com.etz.authorisationserver.dto.request.UpdateUserRequest;
import com.etz.authorisationserver.dto.response.BooleanResponse;
import com.etz.authorisationserver.dto.response.CollectionResponse;
import com.etz.authorisationserver.dto.response.ModelResponse;
import com.etz.authorisationserver.dto.response.StringResponse;
import com.etz.authorisationserver.dto.response.UserResponse;
import com.etz.authorisationserver.services.UserEntityService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {


    private final UserEntityService userService;

    @PostMapping
    public ResponseEntity<ModelResponse<UserResponse>> createUser(@Valid @RequestBody CreateUserRequest request) {
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

    @DeleteMapping("/{id}")
    public BooleanResponse deleteUser(@PathVariable Long id){
        return new BooleanResponse(userService.deleteUserInTransaction(id));
    }

    @PutMapping("/authoriser")
    @ApiOperation(hidden = true, value = "Used internally")
    public ResponseEntity<ModelResponse<UserResponse>> createUser(@Valid @RequestBody ApprovalRequest request) {
        ModelResponse<UserResponse> response = new ModelResponse<>(userService.updateUserAuthoriser(request));
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("UserEntity Authoriser Updated Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
}
