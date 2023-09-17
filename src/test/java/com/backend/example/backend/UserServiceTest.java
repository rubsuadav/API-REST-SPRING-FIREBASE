package com.backend.example.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import com.backend.example.backend.controller.UserController;
import com.backend.example.backend.model.User;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;

@SpringBootTest()
@TestInstance(Lifecycle.PER_CLASS)
public class UserServiceTest {

	@Autowired
	private UserController userController;

	@AfterAll
	public static void tearDown() throws Exception {
		Firestore firestore = FirestoreClient.getFirestore();
		firestore.listCollections().forEach(collection -> {
			collection.listDocuments().forEach(document -> {
				document.delete();
			});
		});

		FirebaseAuth.getInstance().listUsers(null).iterateAll().forEach(user -> {
			try {
				FirebaseAuth.getInstance().deleteUser(user.getUid());
			} catch (Exception e) {
				e.getMessage();
			}
		});
	}

	@Test
	public void testRegisterSuccess() {
		User user = new User();
		user.setEmail("test@gmail.com");
		user.setPassword("password12");
		user.setName("John");
		user.setLastName("Doe");
		user.setPhoneNumber("628074495");

		assertEquals(HttpStatus.OK, userController.register(user).getStatusCode());
	}

	@Test
	public void testRegisterBadRequest() {
		User user = new User();
		user.setEmail("test");
		user.setPassword("");
		user.setName("J");
		user.setLastName("D");
		user.setPhoneNumber("123");

		assertEquals(HttpStatus.BAD_REQUEST, userController.register(user).getStatusCode());
	}
	
	

}
