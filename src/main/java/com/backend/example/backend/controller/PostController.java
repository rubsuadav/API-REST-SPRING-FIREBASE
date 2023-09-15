package com.backend.example.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
		return new ResponseEntity<>(service.list(), HttpStatus.OK);
	}

	@PostMapping(value = "/add")
	public ResponseEntity<?> add(@RequestBody Post post) {
		return new ResponseEntity<>(service.add(post), HttpStatus.CREATED);
	}

	@PutMapping(value = "/{id}/update")
	public ResponseEntity<?> edit(@PathVariable(value = "id") String id, @RequestBody Post post) {
		return new ResponseEntity<>(service.edit(id, post), HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}/delete")
	public ResponseEntity<?> delete(@PathVariable(value = "id") String id) {
		return new ResponseEntity<>(service.delete(id), HttpStatus.NO_CONTENT);
	}

}
