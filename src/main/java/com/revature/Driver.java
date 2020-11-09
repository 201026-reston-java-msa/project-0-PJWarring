package com.revature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.models.User;
import com.revature.services.LoginService;
import com.revature.services.UserService;
import com.revature.utils.StringUtil;

import jdk.internal.org.jline.utils.Log;

public class Driver {

	private static Logger log = Logger.getLogger(Driver.class);
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String input;
		
		System.out.println("-----\nProject 0\n-----\n");
		
		while (true) {
			System.out.print("Enter command: ");
			input = sc.nextLine();
			if (!parseInput(input)) {
				break;
			}
		}
		sc.close();
	}
	
	public static boolean parseInput(String input) {
		Scanner sc = new Scanner(System.in);
		User signedInUser = LoginService.getSignedInUser();
		int userAccessLevel = LoginService.getUserAccessLevel();
		switch (input.toLowerCase()) {
		case "login":		//login
			if (userAccessLevel == 0) {
				//get input
				System.out.print("Enter username: ");
				String username = sc.nextLine();
				System.out.print("Enter password: ");
				String password = sc.nextLine();
				if (LoginService.login(username, password)) { 
					log.info("User " + username + " has been logged in.");
					System.out.println("You have successfully logged in.");
				} else {
					log.info("Could not log in user - invalid username or password.");
					System.out.println("Invalid username or password.");
				}
			} else {
				log.info("Could not log in user - user already logged in.");
				System.out.println("You are already logged in.");
			}
			break;
		case "logout":		//logout
			log.debug("User logged out.");
			LoginService.logout();
			break;
		case "signup":		//signup
			if (UserService.signup()) {
				log.info("User has signed up.");
				System.out.println("Sign up successful.");
			} else {
				log.info("Sign up failed.");
				System.out.println("Sign up failed.");
			}
			break;
		case "register account":
			//TODO: log register command
			//TODO: do logic
			break;
		case "view account":
			//TODO: log command
			System.out.println("view account " + userAccessLevel);
			if (userAccessLevel >= 2) {
				//TODO: if employee, ask if by username or id
				System.out.println("How would you like to select the user?\n1. Id\n2. Username");
				String optionChosen = "";
				while (!StringUtil.isValidInput(optionChosen, 
						new ArrayList<String>(Arrays.asList("1", "2", "Id", "Username")), true)) {
					System.out.print("What option would you like to choose? ");
					optionChosen = sc.next();
				}
			} else if (userAccessLevel == 1) {
				UserService.displayUser(signedInUser.getUsername());
			}
			break;
		case "view all":
			//TODO: log view all command
			System.out.println("view all " + userAccessLevel);
			if (userAccessLevel >= 2) {
				UserService.displayAll();
			}
			break;
		case "help":		//help
			//TODO: log help command
			printHelp();
			break;
		case "q":			//quit
			log.info("User quit the program.");
			return false;
		default:			//default
			log.info("User used unknown command.");
			//throw error? or give bad input message?
			break;
		}
		return true;
	}
	
	public static void printHelp() {
		//this function displays the help commands info
		System.out.println("TODO - display a list of commands in alphabetic order - consider displaying only acceptable commands");
	}
}
