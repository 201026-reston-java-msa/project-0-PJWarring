package com.revature.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.revature.dao.UserDaoImpl;
import com.revature.models.Account;
import com.revature.models.User;

public class UserService {

	public static boolean signup() {
		UserDaoImpl userDao = new UserDaoImpl();
		Scanner sc = new Scanner(System.in);
		//get info for the db information for the user object, that can be sent to the db
		String username = "";
		while (!isValidUsername(username)) {
			System.out.print("Enter Username: ");
			username = sc.nextLine();
		}	
		
		String password = "";
		
		while (!isValidPassword(password)) {
			System.out.print("Enter Password: ");
			password = sc.nextLine();
		}
		
		System.out.print("Enter First Name: ");
		String firstName = sc.nextLine();
		
		System.out.print("Enter Last Name: ");
		String lastName = sc.nextLine();
		
		System.out.print("Enter Email: ");
		String email = sc.nextLine();
		
		//id is handled by the database, accounts isnt relavent to creating a user
		//and role is automatically set to 'Standard' in the user constructor
		return userDao.create(new User(username, password, firstName, lastName, email)) != 0;
	}
	
	public static void displayAll() {
		UserDaoImpl userDao = new UserDaoImpl();
		List<User> userList = userDao.getAll();
		if (userList == null) {System.out.println("No users found"); return;}
		
		for (User u : userList) {
			System.out.println(u);
		}
	}
	
	public static void displayUser(int userId) {
		UserDaoImpl userDao = new UserDaoImpl();
		System.out.println(userDao.getById(userId));
	}
	public static void displayUser(String username) {
		UserDaoImpl userDao = new UserDaoImpl();
		System.out.println(userDao.getByUsername(username));
	}
	
	public static boolean isValidUsername(String username) {
		UserDaoImpl userDao = new UserDaoImpl();
		List<User> userList = userDao.getAll();
		if (userList == null) return false;
		
		if (username == "") return false;
		
		for (User u : userList) {
			if (u.getUsername() == username) return false;
		}
		return true;
	}
	
	public static boolean isValidPassword(String password) {
		//this exists for future versions that add password restrictions
		//for now it only checks if the password isnt empty
		UserDaoImpl userDao = new UserDaoImpl();
		if (password == "") return false;
		else return true;
	}
}