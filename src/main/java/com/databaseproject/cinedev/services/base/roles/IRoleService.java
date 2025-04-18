package com.databaseproject.cinedev.services.base.roles;

import com.databaseproject.cinedev.models.base.Roles;

public interface IRoleService {
    public Roles findByName(String role);
}
