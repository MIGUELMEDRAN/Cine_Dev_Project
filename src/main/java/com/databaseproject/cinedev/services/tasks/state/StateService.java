package com.databaseproject.cinedev.services.tasks.state;

import com.databaseproject.cinedev.models.task.State;
import com.databaseproject.cinedev.repository.task.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StateService implements IStateService{
    @Autowired
    private StateRepository stateRepository;

    @Override
    public State getByName(String name) {
        return stateRepository.findByName(name).orElse(null);
    }
}
