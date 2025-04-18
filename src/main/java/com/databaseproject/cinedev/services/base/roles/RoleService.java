package com.databaseproject.cinedev.services.base.roles;

import com.databaseproject.cinedev.models.base.Roles;
import com.databaseproject.cinedev.repository.base.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService implements IRoleService {
    @Autowired
    RolesRepository rolesRepository;


    @Override
    public Roles findByName(String role) {
        return rolesRepository.findByName(role);
    }
}
