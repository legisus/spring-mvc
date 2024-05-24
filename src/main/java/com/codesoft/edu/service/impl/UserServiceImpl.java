package com.codesoft.edu.service.impl;

import com.codesoft.edu.model.User;
import com.codesoft.edu.repository.UserRepository;
import com.codesoft.edu.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(User user) {
            return userRepository.save(user);
    }

    @Override
    public User readById(long id) {
//        Optional<User> optional = userRepository.findById(id);
//            return optional.get();
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

    }

    @Override
    public User update(User user) {
            User oldUser = readById(user.getId());
                return userRepository.save(user);
    }

    @Override
    public void delete(long id) {
        User user = readById(id);
            userRepository.delete(user);
    }

    @Override
    public List<User> getAll() {
        List<User> users = userRepository.findAll();
        return users.isEmpty() ? new ArrayList<>() : users;
    }

}
