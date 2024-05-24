package com.codesoft.edu.service.impl;

import com.codesoft.edu.model.State;
import com.codesoft.edu.repository.StateRepository;
import com.codesoft.edu.service.StateService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StateServiceImpl implements StateService {
    private StateRepository stateRepository;

    public StateServiceImpl(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    @Override
    public State create(State state) {
            return stateRepository.save(state);
    }

    @Override
    public State readById(long id) {
        Optional<State> optional = stateRepository.findById(id);
            return optional.get();
    }

    @Override
    public State update(State state) {
            State oldState = readById(state.getId());
                return stateRepository.save(state);
    }

    @Override
    public void delete(long id) {
        State state = readById(id);
            stateRepository.delete(state);
    }

    @Override
    public State getByName(String name) {
        Optional<State> optional = Optional.ofNullable(stateRepository.getByName(name));
            return optional.get();
    }

    @Override
    public List<State> getAll() {
        List<State> states = stateRepository.getAll();
        return states.isEmpty() ? new ArrayList<>() : states;
    }
}
