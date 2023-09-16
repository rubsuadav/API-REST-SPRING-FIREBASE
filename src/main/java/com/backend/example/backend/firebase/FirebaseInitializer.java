package com.backend.example.backend.firebase;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.FirebaseApp;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FirebaseInitializer {

	@PostConstruct
	private void initFirestore() throws IOException {
		// InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("serviceAccount.json");
		String credentialsPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
		log.warn("Credentials file founded");

		if (credentialsPath == null) {
			log.error("Credentials file not found");
		}

		FirebaseOptions options = // FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(serviceAccount))
				FirebaseOptions.builder()
						.setCredentials(GoogleCredentials.fromStream(new FileInputStream(credentialsPath)))
						.setDatabaseUrl("https://integracion-spring.firebaseio.com/").build();

		if (FirebaseApp.getApps().isEmpty()) {
			FirebaseApp.initializeApp(options);
		}
		log.warn("Connected to Firebase!");

	}

	public Firestore getFirestore() {
		log.warn("Connected to Firestore DB!");
		return FirestoreClient.getFirestore();
	}

	public FirebaseAuth getAuth() {
		log.warn("Auth Service available");
		return FirebaseAuth.getInstance();
	}
}
