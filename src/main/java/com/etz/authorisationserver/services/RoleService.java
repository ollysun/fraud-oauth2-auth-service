package com.etz.authorisationserver.services;

import com.etz.authorisationserver.repository.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private IRoleRepository iRoleRepository;


}
