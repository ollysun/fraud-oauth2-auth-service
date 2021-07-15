package com.etz.authorisationserver.controller;

import com.etz.authorisationserver.dto.request.CreatePermissionRequest;
import com.etz.authorisationserver.dto.response.BooleanResponse;
import com.etz.authorisationserver.dto.response.CollectionResponse;
import com.etz.authorisationserver.dto.response.ModelResponse;
import com.etz.authorisationserver.dto.response.PermissionEntityResponse;
import com.etz.authorisationserver.services.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/v1/permissions")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping
    public ResponseEntity<CollectionResponse<PermissionEntityResponse>> getPermissions(
            @RequestParam(name = "permissionId", required = false) Long permissionId,
            @RequestParam(value = "status", required = false) Boolean status){
        List<PermissionEntityResponse> permissionList = permissionService.getAllPermissions(permissionId, status);
        CollectionResponse<PermissionEntityResponse> collectionResponse = new CollectionResponse<>(permissionList);
        return new ResponseEntity<>(collectionResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ModelResponse<PermissionEntityResponse>> createPermission(@RequestBody @Valid CreatePermissionRequest request){
        ModelResponse<PermissionEntityResponse> response = new ModelResponse<>(permissionService.createPermission(request));
        response.setStatus(HttpStatus.CREATED.value());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public BooleanResponse deletePermission(@PathVariable Long id){
        return new BooleanResponse(permissionService.deletePermissionInTransaction(id));
    }

}
