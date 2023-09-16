package com.backend.example.backend.service;

import org.springframework.http.ResponseEntity;

import com.backend.example.backend.model.Post;

public interface PostManagementService {

	ResponseEntity<?> list();

	ResponseEntity<?> add(Post post);

	ResponseEntity<?> edit(String id, Post post);

	ResponseEntity<?> delete(String id);

}
