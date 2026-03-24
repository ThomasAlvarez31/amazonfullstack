package com.ecommerce.users.service;

import com.ecommerce.users.model.User;
import com.ecommerce.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() { return userRepository.findAll(); }
    public User save(User user) { return userRepository.save(user); }
    public void delete(Long id) { userRepository.deleteById(id); }
}