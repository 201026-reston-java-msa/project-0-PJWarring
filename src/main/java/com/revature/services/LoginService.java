package com.revature.services;

import com.revature.dao.UserDaoImpl;
import com.revature.models.User;

public class LoginService {

	private static User signedInUser = null;
	
	public static boolean login(String username, String password) {
		//if username/password is valid, set signedInUser, return true
		
		UserDaoImpl userDao = new UserDaoImpl();
		//get user by username, compare password, set password to null
		User tempUser = userDao.getByUsername(username);
		if (tempUser != null && tempUser.getPassword() != null && tempUser.getPassword().equals(password)) { //user credentials are valid
			//sets users password and accounts to null for privacy reasons
			tempUser.setPassword("");
			tempUser.setAccounts(null);
			
			signedInUser = tempUser;
			return true;
		} else { //user credentials are invalid
			//log username or password is invalid -- and display a message stating the same
			return false;
		}
	}
	
	public static boolean logout() { 
		//this returns a boolean for possible future logic that would prevent a user from logging out, and subsequently returning false
		signedInUser = null;
		return true;
	}

	public static int getUserAccessLevel() {
		if (signedInUser == null) return 0;		//user not signed in
		switch (signedInUser.getRole().toLowerCase()) {
		case "standard":	//standard user
			return 1;
		case "employee":	//employee
			return 2;
		case "admin":		//admin
			return 3;
		default:		//unknown role
			return 0;
		}
	}
	
	public static User getSignedInUser() {
		return signedInUser;
	}
}
