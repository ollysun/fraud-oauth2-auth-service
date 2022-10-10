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
import com.etz.authorisationserver.dto.request.CreateRoleRequest;
import com.etz.authorisationserver.dto.request.UpdateRoleRequest;
import com.etz.authorisationserver.dto.response.BooleanResponse;
import com.etz.authorisationserver.dto.response.CollectionResponse;
import com.etz.authorisationserver.dto.response.ModelResponse;
import com.etz.authorisationserver.dto.response.RoleResponse;
import com.etz.authorisationserver.dto.response.StringResponse;
import com.etz.authorisationserver.services.RoleService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/roles")
@RequiredArgsConstructor
@Validated
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<ModelResponse<RoleResponse<Long>>> addRole(@RequestBody @Valid CreateRoleRequest request){
        ModelResponse<RoleResponse<Long>> response = new ModelResponse<>(roleService.addRole(request));
        response.setStatus(HttpStatus.CREATED.value());
        response.setMessage("Role Created Successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<CollectionResponse<RoleResponse<String>>> getRole(
            @RequestParam(name = "roleId", required = false) Long roleId,
            @RequestParam(value = "status", required = false) Boolean status){
        List<RoleResponse<String>> roleResponseList = roleService.getRoles(roleId, status);
        CollectionResponse<RoleResponse<String>> collectionResponse = new CollectionResponse<>(roleResponseList);
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

    @PutMapping("/authoriser")
    @ApiOperation(hidden = true, value = "Used internally")
    public ResponseEntity<ModelResponse<RoleResponse<String>>> updateRoleAuthoriser(@RequestBody @Valid ApprovalRequest request){
        ModelResponse<RoleResponse<String>> response = new ModelResponse<>(roleService.updateRoleAuthoriser(request));
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Role Authoriser Updated Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/permissionroles")
    @ApiOperation(hidden = true, value = "Used internally")
    public ResponseEntity<CollectionResponse<Long>> getPermissionRoleIds(@RequestParam String permissionName){
        CollectionResponse<Long> collectionResponse = new CollectionResponse<>(roleService.getRoleIdsByPermissionNane(permissionName));
        return new ResponseEntity<>(collectionResponse, HttpStatus.OK);
    }
}