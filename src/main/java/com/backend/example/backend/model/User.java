package com.backend.example.backend.model;

import lombok.Data;

@Data
public class User {

	private String id;
	private String email;
	private String password;
	private String name;
	private String lastName;
	private String phoneNumber;

	public String getDisplayName() {
		return getName() + " " + getLastName();
	}

}
