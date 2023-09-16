package com.backend.example.backend.service.impl;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.backend.example.backend.firebase.FirebaseInitializer;
import com.backend.example.backend.model.Post;
import com.backend.example.backend.service.PostManagementService;
import com.backend.example.backend.utils.FormatJson;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;

@Service
public class PostManagementServiceImpl implements PostManagementService {

	@Autowired
	private FirebaseInitializer firebase;

	@Override
	public ResponseEntity<?> list() {
		List<Post> response = new ArrayList<>();
		FormatJson format = new FormatJson();
		Post post;

		ApiFuture<QuerySnapshot> querySnapshotAppiFuture = getCollection().get();
		try {
			if (querySnapshotAppiFuture.get().isEmpty())
				return new ResponseEntity<>(format.createJSONResponse("There isn't posts").toPrettyString(),
						HttpStatusCode.valueOf(404));

			for (DocumentSnapshot doc : querySnapshotAppiFuture.get().getDocuments()) {
				post = doc.toObject(Post.class);
				post.setId(doc.getId());
				response.add(post);
			}
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}

	}

	@Override
	public ResponseEntity<String> add(Post post) {
		FormatJson format = new FormatJson();
		try {
			validateData(post);

			Map<String, Object> docDdata = getDocData(post);
			ApiFuture<WriteResult> writeResultApiFuture = getCollection().document().create(docDdata);

			if (null != writeResultApiFuture.get())
				return new ResponseEntity<>(format.createJSONResponse("Post created succesfully").toPrettyString(),
						HttpStatusCode.valueOf(201));
			return null;
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(format.createJSONResponse(e.getMessage()).toPrettyString());
		}
	}

	@Override
	public ResponseEntity<String> edit(String id, Post post) {
		FormatJson format = new FormatJson();
		try {
			validateData(post);
			Map<String, Object> docDdata = getDocData(post);

			DocumentReference docRef = getCollection().document(id);
			DocumentSnapshot document = docRef.get().get();

			if (document.exists()) {
				ApiFuture<WriteResult> writeResultApiFuture = docRef.set(docDdata);

				if (null != writeResultApiFuture.get())
					return ResponseEntity.ok(format.createJSONResponse("Post updated succesfully").toPrettyString());
				return null;
			}
			return new ResponseEntity<>(format.createJSONResponse("Post with id " + id + " not found").toPrettyString(),
					HttpStatusCode.valueOf(404));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(format.createJSONResponse(e.getMessage()).toPrettyString());
		}
	}

	@Override
	public ResponseEntity<String> delete(String id) {
		FormatJson format = new FormatJson();
		try {
			DocumentReference docRef = getCollection().document(id);
			DocumentSnapshot document = docRef.get().get();

			if (document.exists()) {
				ApiFuture<WriteResult> writeResultApiFuture = docRef.delete();

				if (null != writeResultApiFuture.get())
					return new ResponseEntity<>(format.createJSONResponse("Post deleted successfully").toPrettyString(),
							HttpStatusCode.valueOf(204));
				return null;
			}
			return new ResponseEntity<>(format.createJSONResponse("Post with id " + id + " not found").toPrettyString(),
					HttpStatusCode.valueOf(404));
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(e.getMessage());
		}
	}

	private CollectionReference getCollection() {
		return firebase.getFirestore().collection("posts");
	}

	private Map<String, Object> getDocData(Post post) {
		Map<String, Object> docDdata = new HashMap<>();
		docDdata.put("title", post.getTitle());
		docDdata.put("content", post.getContent());
		return docDdata;
	}

	private void validateData(Post post) {
		checkArgument(post.getTitle().length() >= 3, "title must have more than 3 characters");
		checkArgument(post.getContent().length() >= 3, "content must have more than 3 characters");
	}

}
