package com.backend.example.backend.service;

import org.springframework.http.ResponseEntity;

import com.backend.example.backend.model.User;

public interface UserService {

	ResponseEntity<String> register(User user);

}
