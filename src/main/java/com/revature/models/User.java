package com.revature.models;

public class User {
	
	private int id;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private String role;

	public User() {
		this("", "", "", "", "");
	}
	
	public User(String username, String password, String firstName, String lastName, String email) {
		this.id = 0; //this is preset to 0 because 
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.role = "Standard";
	}
}