package com.backend.example.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backend.example.backend.model.Post;
import com.backend.example.backend.service.PostManagementService;

@RestController
@RequestMapping(value = "/api/post")
public class PostController {

	@Autowired
	private PostManagementService service;

	@GetMapping(value = "")
	public ResponseEntity<?> list() {
		return service.list();
	}

	@PostMapping(value = "/add")
	public ResponseEntity<?> add(@RequestBody Post post) {
		return service.add(post);
	}

	@PutMapping(value = "/{id}/update")
	public ResponseEntity<?> edit(@PathVariable(value = "id") String id, @RequestBody Post post) {
		return service.edit(id, post);
	}

	@DeleteMapping(value = "/{id}/delete")
	public ResponseEntity<?> delete(@PathVariable(value = "id") String id) {
		return service.delete(id);
	}

}
