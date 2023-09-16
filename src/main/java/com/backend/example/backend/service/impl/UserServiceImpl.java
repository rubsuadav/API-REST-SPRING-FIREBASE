package com.backend.example.backend.service.impl;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.backend.example.backend.firebase.FirebaseInitializer;
import com.backend.example.backend.model.User;
import com.backend.example.backend.service.UserService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private FirebaseInitializer firebase;

	@Override
	public ResponseEntity<String> register(User user) {
		try {
			validateData(user);

			CreateRequest request = new CreateRequest().setEmail(user.getEmail()).setPassword(user.getPassword())
					.setDisplayName(user.getDisplayName()).setPhoneNumber("+34".concat(user.getPhoneNumber()));

			UserRecord newUser = getAuth().createUser(request);

			Map<String, Object> docDdata = getDocData(user);

			ApiFuture<WriteResult> writeResultApiFuture = getCollection().document().create(docDdata);

			if (null != writeResultApiFuture.get()) {
				return ResponseEntity.ok(getAuth().createCustomToken(newUser.getUid()));
			}
			return null;

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	private CollectionReference getCollection() {
		return firebase.getFirestore().collection("users");
	}

	private FirebaseAuth getAuth() {
		return firebase.getAuth();
	}

	private Map<String, Object> getDocData(User user) {
		Map<String, Object> docDdata = new HashMap<>();
		docDdata.put("email", user.getEmail());
		docDdata.put("password", user.getPassword());
		docDdata.put("displayName", user.getDisplayName());
		docDdata.put("phoneNumber", user.getPhoneNumber());
		return docDdata;
	}

	private void validateData(User user) {
		checkArgument(user.getEmail().matches("^\\w+([\\.-]?\\w+)*@(gmail|hotmail|outlook)\\.com$"), "email malformed");
		checkArgument(user.getName().length() >= 3, "name must have more than 3 characters");
		checkArgument(user.getLastName().length() >= 3, "lastName must have more than 3 characters");
		checkArgument(user.getPhoneNumber().matches("^(\\+34|0034|34)?[ -]*(6|7)[ -]*([0-9][ -]*){8}$"),
				"phone malformed");
	}

}
