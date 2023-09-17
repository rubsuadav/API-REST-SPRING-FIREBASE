package com.backend.example.backend.model;

import lombok.*;

@Getter
@Setter
public class User {

	private String email;
	private String password;
	private String name;
	private String lastName;
	private String phoneNumber;

	public String getDisplayName() {
		return getName() + " " + getLastName();
	}

}
