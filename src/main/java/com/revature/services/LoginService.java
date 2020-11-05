package com.revature.services;

import com.revature.models.User;

public class LoginService {

	private User signedInUser = null;
	
	public boolean login(String username, String password) {
		//if username/password is valid, set signedInUser, return true
		//else return false
		return false;
	}
	
	public boolean logout() {
		signedInUser = null;
		return true;
	}
}
