package com.example.demoJpa.service;

import com.example.demoJpa.entity.Role;
import com.example.demoJpa.entity.User;
import com.example.demoJpa.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    public User update(Integer id, User updatedUser) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            user.setUsername(updatedUser.getUsername());
            user.setPassword(updatedUser.getPassword());
            user.setRole(Role.valueOf(updatedUser.getRole()));
            return userRepository.save(user);
        }
        throw new EntityNotFoundException("User not found");
    }


    public String delete(Integer id) {
        userRepository.deleteById(id);
        return "Comment deleted successfully";
    }
}
