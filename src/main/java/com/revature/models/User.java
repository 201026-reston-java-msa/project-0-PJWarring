package com.revature.models;

import java.util.ArrayList;
import java.util.List;

public class User {
	
	private int id;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private String role;
	private List<Account> accounts = new ArrayList<>();

	public User() {
		this("", "", "", "", "");
	}
	
	public User(String username, String password, String firstName, String lastName, String email) {
		this.id = 0; //this is preset to 0 because the database handles the id
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.role = "Standard";
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("User [id=" + id + ", username=" + username + ", password=" + password + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", email=" + email + ", role=" + role + "]");
		sb.append("\nAccounts [");
		if (accounts != null) {
			for (Account a : accounts) {
				sb.append(a.toString());
			}
		} else {
			sb.append("null");
		}
		sb.append("]");
		return sb.toString();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public List<Account> getAccounts() {
		return accounts;
	}
	
	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}
}