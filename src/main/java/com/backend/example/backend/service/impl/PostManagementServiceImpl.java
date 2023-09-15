package com.backend.example.backend.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.example.backend.firebase.FirebaseInitializer;
import com.backend.example.backend.model.Post;
import com.backend.example.backend.service.PostManagementService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;

@Service
public class PostManagementServiceImpl implements PostManagementService {

	@Autowired
	private FirebaseInitializer firebase;

	@Override
	public List<Post> list() {
		List<Post> response = new ArrayList<>();
		Post post;

		ApiFuture<QuerySnapshot> querySnapshotAppiFuture = getCollection().get();
		try {
			for (DocumentSnapshot doc : querySnapshotAppiFuture.get().getDocuments()) {
				post = doc.toObject(Post.class);
				post.setId(doc.getId());
				response.add(post);
			}
			return response;
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public Boolean add(Post post) {
		Map<String, Object> docDdata = getDocData(post);
		ApiFuture<WriteResult> writeResultApiFuture = getCollection().document().create(docDdata);

		try {
			if (null != writeResultApiFuture.get()) {
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		} catch (Exception e) {
			return Boolean.FALSE;
		}
	}

	@Override
	public Boolean edit(String id, Post post) {
		Map<String, Object> docDdata = getDocData(post);
		ApiFuture<WriteResult> writeResultApiFuture = getCollection().document(id).set(docDdata);
		try {
			if (null != writeResultApiFuture.get()) {
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		} catch (Exception e) {
			return Boolean.FALSE;
		}
	}

	@Override
	public Boolean delete(String id) {
		ApiFuture<WriteResult> writeResultApiFuture = getCollection().document(id).delete();
		try {
			if (null != writeResultApiFuture.get()) {
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		} catch (Exception e) {
			return Boolean.FALSE;
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

}
