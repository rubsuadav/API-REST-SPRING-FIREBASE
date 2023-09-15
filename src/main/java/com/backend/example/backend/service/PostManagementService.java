package com.backend.example.backend.service;

import java.util.List;

import com.backend.example.backend.model.Post;

public interface PostManagementService {

	List<Post> list();

	Boolean add(Post post);

	Boolean edit(String id, Post post);

	Boolean delete(String id);

}
