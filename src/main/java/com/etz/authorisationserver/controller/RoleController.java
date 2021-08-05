package com.etz.authorisationserver.controller;

import com.etz.authorisationserver.dto.request.CreateRoleRequest;
import com.etz.authorisationserver.dto.request.UpdateRoleRequest;
import com.etz.authorisationserver.dto.response.*;
import com.etz.authorisationserver.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/roles")
@RequiredArgsConstructor
@Validated
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<ModelResponse<RoleResponse>> addRole(@RequestBody @Valid CreateRoleRequest request){
        ModelResponse<RoleResponse> response = new ModelResponse<>(roleService.addRole(request));
        response.setStatus(HttpStatus.CREATED.value());
        response.setMessage("Role Created Successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<CollectionResponse<RoleResponse>> getRole(
            @RequestParam(name = "roleId", required = false) Long roleId,
            @RequestParam(value = "status", required = false) Boolean status){
        List<RoleResponse> roleResponseList = roleService.getRoles(roleId, status);
        CollectionResponse<RoleResponse> collectionResponse = new CollectionResponse<>(roleResponseList);
        return new ResponseEntity<>(collectionResponse, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<StringResponse> updateRole(@RequestBody @Valid UpdateRoleRequest request){
        StringResponse result = new StringResponse();
        Boolean boolVal = roleService.updateRole(request);
        if(Boolean.TRUE.equals(boolVal)){
            result.setMessage("Role Updated");
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{roleId}")
    public BooleanResponse deleteRole(@PathVariable Long roleId){
        return new BooleanResponse(roleService.deleteRoleInTransaction(roleId));
    }

}