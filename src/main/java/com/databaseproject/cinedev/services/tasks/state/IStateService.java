package com.databaseproject.cinedev.services.tasks.state;

import com.databaseproject.cinedev.models.task.State;

public interface IStateService {
    State getByName(String name);
}
