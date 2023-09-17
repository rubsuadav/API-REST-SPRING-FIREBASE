package com.backend.example.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class BackendApiApplicationTests {

	@Test
	void contextLoads() {
		ResponseEntity<String> response = ResponseEntity.notFound().build();
		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
	}

}
