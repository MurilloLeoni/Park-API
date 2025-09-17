package com.mleoni.demo_park_api.services;


import com.mleoni.demo_park_api.entities.User;
import com.mleoni.demo_park_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User searchById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found.")
        );
    }

    @Transactional
    public User editPassword(Long id, String password) {
        User user = searchById(id);
        user.setPassword(password);
        return user;
    }

    @Transactional(readOnly = true)
    public List<User> searchAll() {
        return userRepository.findAll();
    }

    @Transactional
    public void delete(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("ERROR!");
        }
    }

}
