package com.example.travelplanner.service.impl;

import com.example.travelplanner.model.entity.User;
import com.example.travelplanner.repository.UserRepository;
import com.example.travelplanner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user) {
        // 检查用户名是否已存在
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 检查邮箱是否已存在
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("邮箱已存在");
        }
        
        // 密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // 保存用户
        return userRepository.save(user);
    }

    @Override
    public Optional<User> login(String username, String password) {
        // 根据用户名查找用户
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        // 验证密码
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            return userOpt;
        }
        
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}