package com.etz.authorisationserver.controller;

import com.etz.authorisationserver.dto.request.CreatePermissionRequest;
import com.etz.authorisationserver.dto.response.CollectionResponse;
import com.etz.authorisationserver.dto.response.ModelResponse;
import com.etz.authorisationserver.entity.Permission;
import com.etz.authorisationserver.services.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Validated
@RestController
@RequestMapping("/v1/permissions")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping
    public ResponseEntity<CollectionResponse<Permission>> getPermissions(
            @RequestParam(name = "permissionId", required = false) Long permissionId,
            @RequestParam(value = "status", required = false) Boolean status){
        List<Permission> permissionList = permissionService.getAllPermissions(permissionId, status);
        CollectionResponse<Permission> collectionResponse = new CollectionResponse<>(permissionList);
        return new ResponseEntity<>(collectionResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ModelResponse<Permission>> createPermission(@RequestBody @Valid CreatePermissionRequest request){
        ModelResponse<Permission> response = new ModelResponse<>(permissionService.createPermission(request));
        response.setStatus(HttpStatus.CREATED);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
