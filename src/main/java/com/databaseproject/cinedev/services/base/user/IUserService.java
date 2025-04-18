package com.databaseproject.cinedev.services.base.user;

import com.databaseproject.cinedev.models.base.User;

public interface IUserService {
    public void saveUser(User user);

    public User getUserByEmail(String email);

    boolean existsByEmail(String email);
}
