package com.revature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.dao.AccountDaoImpl;
import com.revature.dao.UserDaoImpl;
import com.revature.models.Account;
import com.revature.models.User;
import com.revature.services.AccountService;
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
		AccountDaoImpl accountDao = new AccountDaoImpl();
		UserDaoImpl userDao = new UserDaoImpl();
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
					log.debug("Could not log in user - invalid username or password.");
					System.out.println("Invalid username or password.");
				}
			} else {
				log.debug("Could not log in user - user already logged in.");
				System.out.println("You are already logged in.");
			}
			break;
		case "logout":		//logout
			if (userAccessLevel > 0) {
				log.info("User logged out.");
				LoginService.logout();
				break;
			}
		case "signup":		//signup
			if (userAccessLevel == 0) {
				if (UserService.signup()) {
					log.info("User has signed up.");
					System.out.println("Sign up successful.");
				} else {
					log.debug("Sign up failed.");
					System.out.println("Sign up failed.");
				}
			}
			break;
		case "create account":
			if (userAccessLevel >= 1) {
				log.debug("User is creating an account.");
				if (AccountService.createAccount()) {
					log.info("User has made a new account.");
					System.out.println("You have made a new account.");
				} else {
					log.debug("Create account failed.");
					System.out.println("Account creation failed.");
				}
			}
			break;
		case "close account":
			if (userAccessLevel >= 3) {
				log.debug("User is closing an account.");
				String tempAccountId = "";
				int accountId;
				while (!StringUtil.isInt(tempAccountId) || tempAccountId.equalsIgnoreCase("cancel")) {
					System.out.print("What is the id of the account you would like to close (or " + 
							"type 'cancel' to cancel)? ");
					tempAccountId = sc.next();
				}
				sc.nextLine(); //removing extra characters on input line
				if (tempAccountId.equalsIgnoreCase("cancel")) break;
				accountId = Integer.parseInt(tempAccountId);
				Account account = accountDao.getById(accountId);
				account.setStatus("Closed");
				if (accountDao.update(account)) {
					log.info("Closed account at id: " + accountId);
					System.out.println("You have successfully closed the account.");
				} else {
					log.debug("Closing account " + accountId + " failed.");
					System.out.println("Closing account failed.");
				}
			}
			break;
		case "review pending accounts":
			if (userAccessLevel >= 2) {
				log.debug("User is reviewing pending accounts.");
				List<Account> pendingAccounts = accountDao.getByStatus("Pending");
				String optionChosen;
				for (Account a : pendingAccounts) {
					User user = userDao.getByAccountId(a.getId());
					user.setPassword("****");
					System.out.println(user);
					System.out.println("The pending account is: " + a);
					optionChosen = "";
					while (!StringUtil.isValidInput(optionChosen, 
							new ArrayList<String>(Arrays.asList("Open", "Deny", "Skip")), true)) {
						System.out.print("Would you like to Open, Deny, or Skip the account? ");
						optionChosen = sc.next();
						sc.nextLine(); //clean the input field of misc characters
					}
					switch (optionChosen.toLowerCase()) {
					case "open":
						log.info("User opened account at id " + a.getId());
						a.setStatus("Open");
						accountDao.update(a);
						break;
					case "deny":
						log.info("User denied account at id " + a.getId());
						a.setStatus("Denied");
						accountDao.update(a);
						break;
					case "skip":
						continue;
					default:
						continue;
					}
				}
				System.out.println("There are no more pending accounts.");
			}
			break;
		case "deposit":
			deposit();
			break;
		case "withdraw":
			withdraw();
			break;
		case "transfer":
			transfer();
			break;
		case "view user":
			viewUser();
			break;
		case "view account":
			viewAccount();
			break;
		case "view all users":
			if (userAccessLevel >= 2) {
				log.debug("User displayed all users.");
				UserService.displayAll();
			}
			break;
		case "view all accounts":
			if (userAccessLevel >= 2) {
				log.debug("User displayed all accounts.");
				AccountService.displayAll();
			}
			break;
		case "help":		//help
			log.debug("User accessed help command.");
			printHelp();
			break;
		case "q":			//quit
			log.info("User quit the program.");
			return false;
		default:			//default
			log.debug("User used unknown command.");
			System.out.println("You used an unknown command - Try using 'help' to view" + 
					" available commands.");
			break;
		}
		return true;
	}
	
	public static void printHelp() {
		//this function displays the help commands info
		int userAccessLevel = LoginService.getUserAccessLevel();
		
		System.out.println("Help: display a list of available commands.");
		System.out.println("Q: quit the program.");
		if (userAccessLevel == 0) {
			System.out.println("Login: enter your username and password to login.");
			System.out.println("Signup: create an account.");
		} else {
			if (userAccessLevel >= 1) {
				//Standard user commands
				System.out.println("Logout: logout of the application.");
				System.out.println("Create Account: create a new account for yourself.");
			}
			if (userAccessLevel == 1) {
				System.out.println("View User: display your own account information.");
				System.out.println("View Account: display your accounts.");
			}
			if (userAccessLevel >= 2) {
				//Employee commands
				System.out.println("Review Pending Accounts: step through pending account " +
						"applications and approve/deny them.");
				System.out.println("View All Users: display a list of all users in the database.");
				System.out.println("View All Accounts: display a list of all accounts in the database.");
				System.out.println("View User: view data for any user.");
				System.out.println("View Account: display data for any account.");
			}
			if (userAccessLevel == 1 || userAccessLevel == 2) {
				System.out.println("Deposit: add money to one of your accounts.");
				System.out.println("Withdraw: subtract money from one of your accounts.");
				System.out.println("Transfer: move money from one of your accounts to any other account.");
			}
			if (userAccessLevel >= 3) {
				//Admin commands
				System.out.println("Close Account: close an account.");
				System.out.println("Deposit: add money to any account.");
				System.out.println("Withdraw: subtract money from any account.");
				System.out.println("Transfer: move money from any account to any other account.");
			}
		}
	}
	
	public static void viewAccount() {
		Scanner sc = new Scanner(System.in);
		User signedInUser = LoginService.getSignedInUser();
		int userAccessLevel = LoginService.getUserAccessLevel();
		log.debug("User entered view account.");
		if (userAccessLevel >= 2) {
			System.out.println("How would you like to select the account?\n1. Id\n2. User");
			String optionChosen = "";
			while (!StringUtil.isValidInput(optionChosen, 
					new ArrayList<String>(Arrays.asList("1", "2", "Id", "User")), true)) {
				System.out.print("What option would you like to choose? ");
				optionChosen = sc.next();
				sc.nextLine(); //clean the input field of misc characters
			}
			//view by id
			if (optionChosen.equalsIgnoreCase("1") || optionChosen.equalsIgnoreCase("Id")) {
				log.debug("User chose to view account by id.");
				int selectedAccountId;
				String tempAccountId = "";
				while (!StringUtil.isInt(tempAccountId)) {
					System.out.print("What is the id of the account you would like to view? ");
					tempAccountId = sc.next();
				}
				sc.nextLine(); //clean the input field of misc characters
				selectedAccountId = Integer.parseInt(tempAccountId);
				AccountService.displayAccount(selectedAccountId);
			//view by user
			} else if (optionChosen.equalsIgnoreCase("2") || optionChosen.equalsIgnoreCase("User")) {
				System.out.println("How would you like to select the user?\n1. Id\n2. Username");
				optionChosen = "";
				while (!StringUtil.isValidInput(optionChosen, 
						new ArrayList<String>(Arrays.asList("1", "2", "Id", "Username")), true)) {
					System.out.print("What option would you like to choose? ");
					optionChosen = sc.next();
					sc.nextLine(); //clean the input field of misc characters
				}
				//view user accounts by id
				if (optionChosen.equalsIgnoreCase("1") || optionChosen.equalsIgnoreCase("Id")) {
					log.debug("User chose to view user accounts by id.");
					int selectedUserId;
					String tempUserId = "";
					while (!StringUtil.isInt(tempUserId)) {
						System.out.print("What is the id of the user's accounts you would like to view? ");
						tempUserId = sc.next();
					}
					sc.nextLine(); //clean the input field of misc characters
					selectedUserId = Integer.parseInt(tempUserId);
					AccountService.displayAccountsByUserId(selectedUserId);
				//view user accounts by username
				} else if (optionChosen.equalsIgnoreCase("2") || optionChosen.equalsIgnoreCase("Username")) {
					log.debug("User chose to view user accounts by username.");
					String selectedUsername = "";
					while (selectedUsername.equals("")) {
						System.out.print("What is the username of the user's accounts you would like to view? ");
						selectedUsername = sc.nextLine();
					}
					AccountService.displayAccountsByUsername(selectedUsername);
				}
			}
		} else if (userAccessLevel == 1) {
			log.debug("User displayed their own account information - had an access level of 1.");
			try {
				AccountService.displayAccountsByUsername(signedInUser.getUsername());
			} catch (NullPointerException e) {
				log.warn("Null pointer exception - user does not have any accounts.");
				System.out.println("You don't have any accounts.");
			}
		}
	}
	
	public static void viewUser() {
		Scanner sc = new Scanner(System.in);
		User signedInUser = LoginService.getSignedInUser();
		int userAccessLevel = LoginService.getUserAccessLevel();
		log.debug("User entered view user.");
		if (userAccessLevel >= 2) {
			System.out.println("How would you like to select the user?\n1. Id\n2. Username");
			String optionChosen = "";
			while (!StringUtil.isValidInput(optionChosen, 
					new ArrayList<String>(Arrays.asList("1", "2", "Id", "Username")), true)) {
				System.out.print("What option would you like to choose? ");
				optionChosen = sc.next();
				sc.nextLine(); //clean the input field of misc characters
			}
			if (optionChosen.equalsIgnoreCase("1") || optionChosen.equalsIgnoreCase("Id")) {
				log.debug("User chose to view user by id.");
				int selectedUserId;
				String tempUserId = "";
				while (!StringUtil.isInt(tempUserId)) {
					System.out.print("What is the id of the user you would like to view? ");
					tempUserId = sc.next();
				}
				sc.nextLine(); //clean the input field of misc characters
				selectedUserId = Integer.parseInt(tempUserId);
				UserService.displayUser(selectedUserId);
			} else if (optionChosen.equalsIgnoreCase("2") || optionChosen.equalsIgnoreCase("Username")) {
				log.debug("User chose to view user by username.");
				String selectedUsername = "";
				while (selectedUsername.equals("")) {
					System.out.print("What is the username of the user you would like to view? ");
					selectedUsername = sc.nextLine();
				}
				UserService.displayUser(selectedUsername);
			}
		} else if (userAccessLevel == 1) {
			log.debug("User displayed their own info - had an access level of 1.");
			UserService.displayUser(signedInUser.getUsername());
		}
	}
	
	public static void deposit() {
		Scanner sc = new Scanner(System.in);
		User signedInUser = LoginService.getSignedInUser();
		int userAccessLevel = LoginService.getUserAccessLevel();
		UserDaoImpl userDao = new UserDaoImpl();
		log.debug("User entered deposit");
		if (userAccessLevel == 3) {
			log.debug("An admin is depositing into an account.");
			String accountChosenStr = "";
			while (!StringUtil.isInt(accountChosenStr)) {
				System.out.print("Enter the id of the account you want to deposit into " + 
						" (or type 'cancel' to cancel): ");
				accountChosenStr = sc.nextLine();
				if (accountChosenStr.equalsIgnoreCase("Cancel")) return;
			}
			int accountChosen = Integer.parseInt(accountChosenStr);
			if (!AccountService.isOpenAccount(accountChosen)) {
				log.debug("Deposit command failed - user entered an account that isn't open.");
				System.out.println("You can not deposit into an account that isn't open.");
				return;
			}
			double amount = -1;
			while (amount <= 0) {
				System.out.print("What amount would you like to deposit (must be greater than zero): ");
				if (sc.hasNextDouble()) amount = sc.nextDouble();
			}
			amount = Math.floor(amount * 100) / 100; //remove all values after the second decimal place
			AccountService.deposit(accountChosen, amount);
		} else if (userAccessLevel >= 1) {
			log.debug("User is deposting into their own accounts - they have an access level of 1 or 2");
			User user = userDao.getById(signedInUser.getId());
			List<Account> accounts = user.getAccounts();
			List<String> validInput = new ArrayList<>();
			for (Account a : accounts) validInput.add(Integer.toString(a.getId()));
			AccountService.displayAccountsByUserId(signedInUser.getId());
			String accountChosenStr = "";
			while (!StringUtil.isValidInput(accountChosenStr, validInput, true)) {
				System.out.print("Enter the id of the account you want to deposit into " + 
						" (or type 'cancel' to cancel): ");
				accountChosenStr = sc.nextLine();
				if (accountChosenStr.equalsIgnoreCase("Cancel")) return;
			}
			
			int accountChosen = Integer.parseInt(accountChosenStr);
			if (!AccountService.isOpenAccount(accountChosen)) {
				log.debug("Deposit command failed - user entered an account that isn't open.");
				System.out.println("You can not deposit into an account that isn't open.");
				return;
			}
			double amount = -1;
			while (amount <= 0) {
				System.out.print("What amount would you like to deposit (must be greater than zero): ");
				if (sc.hasNextDouble()) amount = sc.nextDouble();
			}
			amount = Math.floor(amount * 100) / 100; //remove all values after the second decimal place
			AccountService.deposit(accountChosen, amount);
		}
	}
	
	public static void withdraw() {
		Scanner sc = new Scanner(System.in);
		User signedInUser = LoginService.getSignedInUser();
		int userAccessLevel = LoginService.getUserAccessLevel();
		UserDaoImpl userDao = new UserDaoImpl();
		log.debug("User entered withdraw");
		if (userAccessLevel == 3) {
			log.debug("An admin is withdrawing from an account.");
			String accountChosenStr = "";
			while (!StringUtil.isInt(accountChosenStr)) {
				System.out.print("Enter the id of the account you want to withdraw from " + 
						" (or type 'cancel' to cancel): ");
				accountChosenStr = sc.nextLine();
				if (accountChosenStr.equalsIgnoreCase("Cancel")) return;
			}
			int accountChosen = Integer.parseInt(accountChosenStr);
			if (!AccountService.isOpenAccount(accountChosen)) {
				log.debug("Withdraw command failed - user entered an account that isn't open.");
				System.out.println("You can not withdraw from an account that isn't open.");
				return;
			}
			AccountService.displayAccount(accountChosen);
			double amount = -1;
			while (!(amount >= 0 && AccountService.isValidWithdrawAmount(accountChosen, amount))) {
				System.out.print("What amount would you like to withdraw (must be greater than zero, and can not result in an overdraft): ");
				if (sc.hasNextDouble()) amount = sc.nextDouble();
			}
			amount = Math.floor(amount * 100) / 100; //remove all values after the second decimal place
			AccountService.withdraw(accountChosen, amount);
			
		} else if (userAccessLevel >= 1) {
			log.debug("User is withdrawing from their own accounts - they have an access level of 1 or 2");
			User user = userDao.getById(signedInUser.getId());
			List<Account> accounts = user.getAccounts();
			List<String> validInput = new ArrayList<>();
			for (Account a : accounts) validInput.add(Integer.toString(a.getId()));
			AccountService.displayAccountsByUserId(signedInUser.getId());
			String accountChosenStr = "";
			while (!StringUtil.isValidInput(accountChosenStr, validInput, true)) {
				System.out.print("Enter the id of the account you want to withdraw from " + 
						" (or type 'cancel' to cancel): ");
				accountChosenStr = sc.nextLine();
				if (accountChosenStr.equalsIgnoreCase("Cancel")) return;
			}
			
			int accountChosen = Integer.parseInt(accountChosenStr);
			if (!AccountService.isOpenAccount(accountChosen)) {
				log.debug("Withdraw command failed - user entered an account that isn't open.");
				System.out.println("You can not withdraw from an account that isn't open.");
				return;
			}
			if (!AccountService.hasFunds(accountChosen)) {
				log.debug("Transfer command failed - user entered a sender account that doesn't have funds.");
				System.out.println("That account doesn't have funds.");
				return;
			}
			
			AccountService.displayAccount(accountChosen);
			double amount = -1;
			while (!(amount >= 0 && AccountService.isValidWithdrawAmount(accountChosen, amount))) {
				System.out.print("What amount would you like to withdraw (must be greater than zero, and can not result in an overdraft): ");
				if (sc.hasNextDouble()) amount = sc.nextDouble();
			}
			amount = Math.floor(amount * 100) / 100; //remove all values after the second decimal place
			AccountService.withdraw(accountChosen, amount);
		}
	}
	
	public static void transfer() {
		Scanner sc = new Scanner(System.in);
		User signedInUser = LoginService.getSignedInUser();
		int userAccessLevel = LoginService.getUserAccessLevel();
		UserDaoImpl userDao = new UserDaoImpl();
		log.debug("User entered transfer");
		if (userAccessLevel == 3) {
			log.debug("An admin is transferring funds between accounts.");
			
			String senderAccountStr = "";
			while (!StringUtil.isInt(senderAccountStr)) {
				System.out.print("Enter the id of the account you want to withdraw from " + 
						" (or type 'cancel' to cancel): ");
				senderAccountStr = sc.nextLine();
				if (senderAccountStr.equalsIgnoreCase("Cancel")) return;
			}
			int senderAccountId = Integer.parseInt(senderAccountStr);
			if (!AccountService.isOpenAccount(senderAccountId)) {
				log.debug("Transfer command failed - user entered a sender account that isn't open.");
				System.out.println("That account isn't open.");
				return;
			}
			if (!AccountService.hasFunds(senderAccountId)) {
				log.debug("Transfer command failed - user entered a sender account that doesn't have funds.");
				System.out.println("That account doesn't have funds.");
				return;
			}
			
			String recieverAccountStr = "";
			while (!StringUtil.isInt(recieverAccountStr)) {
				System.out.print("Enter the id of the account you want to deposit into " + 
						" (or type 'cancel' to cancel): ");
				recieverAccountStr = sc.nextLine();
				if (recieverAccountStr.equalsIgnoreCase("Cancel")) return;
			}
			int recieverAccountId = Integer.parseInt(recieverAccountStr);
			if (!AccountService.isOpenAccount(recieverAccountId)) {
				log.debug("Transfer command failed - user entered a reciever account that isn't open.");
				System.out.println("That account isn't open.");
				return;
			}
			
			System.out.print("Sender Account: ");
			AccountService.displayAccount(senderAccountId);
			System.out.print("Reciever Account: ");
			AccountService.displayAccount(recieverAccountId);
			
			double amount = -1;
			while (!(amount >= 0 && AccountService.isValidWithdrawAmount(senderAccountId, amount))) {
				System.out.print("What amount would you like to transfer (must be greater than zero, and can not result in an overdraft): ");
				if (sc.hasNextDouble()) amount = sc.nextDouble();
			}
			amount = Math.floor(amount * 100) / 100; //remove all values after the second decimal place
			AccountService.transfer(senderAccountId, recieverAccountId, amount);
		} else if (userAccessLevel >= 1) {
			log.debug("User is transfering from their own account - they have an access level of 1 or 2");
			User user = userDao.getById(signedInUser.getId());
			
			List<Account> accounts = user.getAccounts();
			List<String> validInput = new ArrayList<>();
			for (Account a : accounts) validInput.add(Integer.toString(a.getId()));
			AccountService.displayAccountsByUserId(signedInUser.getId());
			String senderAccountStr = "";
			while (!StringUtil.isValidInput(senderAccountStr, validInput, true)) {
				System.out.print("Enter the id of the account you want to withdraw from " + 
						" (or type 'cancel' to cancel): ");
				senderAccountStr = sc.nextLine();
				if (senderAccountStr.equalsIgnoreCase("Cancel")) return;
			}
			int senderAccountId = Integer.parseInt(senderAccountStr);
			if (!AccountService.isOpenAccount(senderAccountId)) {
				log.debug("Transfer command failed - user entered a sender account that isn't open.");
				System.out.println("That account isn't open.");
				return;
			}
			if (!AccountService.hasFunds(senderAccountId)) {
				log.debug("Transfer command failed - user entered a sender account that doesn't have funds.");
				System.out.println("That account doesn't have funds.");
				return;
			}
			
			String recieverAccountStr = "";
			while (!StringUtil.isValidInput(recieverAccountStr, validInput, true)) {
				System.out.print("Enter the id of the account you want to deposit into " + 
						" (or type 'cancel' to cancel): ");
				recieverAccountStr = sc.nextLine();
				if (recieverAccountStr.equalsIgnoreCase("Cancel")) return;
			}
			int recieverAccountId = Integer.parseInt(recieverAccountStr);
			if (!AccountService.isOpenAccount(recieverAccountId)) {
				log.debug("Transfer command failed - user entered a reciever account that isn't open.");
				System.out.println("That account isn't open.");
				return;
			}
			
			System.out.print("Sender Account: ");
			AccountService.displayAccount(senderAccountId);
			System.out.print("Reciever Account: ");
			AccountService.displayAccount(recieverAccountId);
			
			double amount = -1;
			while (!(amount >= 0 && AccountService.isValidWithdrawAmount(senderAccountId, amount))) {
				System.out.print("What amount would you like to transfer (must be greater than zero, and can not result in an overdraft): ");
				if (sc.hasNextDouble()) amount = sc.nextDouble();
			}
			amount = Math.floor(amount * 100) / 100; //remove all values after the second decimal place
			AccountService.transfer(senderAccountId, recieverAccountId, amount);
		}
	}
}
