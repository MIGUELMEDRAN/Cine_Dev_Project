package com.databaseproject.cinedev.repository.base;

import com.databaseproject.cinedev.models.base.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRolesRepository extends JpaRepository<UserRoles, Integer> {
}
