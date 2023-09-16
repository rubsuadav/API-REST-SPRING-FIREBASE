package com.backend.example.backend.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
	public String register(User user) {
		Map<String, Object> docDdata = getDocData(user);
		CreateRequest request = new CreateRequest().setEmail(user.getEmail()).setPassword(user.getPassword())
				.setDisplayName(user.getDisplayName()).setPhoneNumber("+34".concat(user.getPhoneNumber()));

		UserRecord newUser;
		try {
			newUser = getAuth().createUser(request);

			ApiFuture<WriteResult> writeResultApiFuture = getCollection().document().create(docDdata);

			try {
				if (null != writeResultApiFuture.get()) {
					return getAuth().createCustomToken(newUser.getUid());
				}
				return null;
			} catch (Exception e) {
				return null;
			}
		} catch (Exception e) {
			return null;
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

}
