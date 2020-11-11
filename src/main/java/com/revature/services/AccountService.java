package com.revature.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.dao.AccountDaoImpl;
import com.revature.dao.UserAccountDaoImpl;
import com.revature.dao.UserDaoImpl;
import com.revature.models.Account;
import com.revature.models.User;
import com.revature.utils.ConnectionFactory;
import com.revature.utils.StringUtil;

import jdk.internal.org.jline.utils.Log;

public class AccountService {
	
	private static Logger log = Logger.getLogger(AccountService.class);

	public static boolean createAccount() {
		AccountDaoImpl accountDao = new AccountDaoImpl();
		UserAccountDaoImpl userAccountDao = new UserAccountDaoImpl();
		Scanner sc = new Scanner(System.in);
		
		double balance;
		String type = null;
		String status;
		
		balance = 0.00; //balance will always start at 0
		
		System.out.println("What type of account would you like to open?\n1. Checking\n2. Savings");
		String optionChosen = "";
		while (!StringUtil.isValidInput(optionChosen, 
				new ArrayList<String>(Arrays.asList("1", "2", "Checking", "Savings")), true)) {
			System.out.print("What option would you like to choose? ");
			optionChosen = sc.next();
		}
		
		if (optionChosen.equalsIgnoreCase("1") || optionChosen.equalsIgnoreCase("Checking")) {
			type = "Checking";
		} else if (optionChosen.equalsIgnoreCase("2") || optionChosen.equalsIgnoreCase("Savings")) {
			type = "Savings";
		} else {
			return false; //this should never happen because the program should only allow good input
		}
		
		status = "Pending"; //the account status is always pending until an employee/admin updates it
		
		Account account = new Account(balance, type, status);
		
		int newAccountId = accountDao.create(account);
		if (newAccountId == 0) return false;
		
		return userAccountDao.create(LoginService.getSignedInUser().getId(), newAccountId);
	}
	
	public static void displayAccount(int accountid) {
		AccountDaoImpl accountDao = new AccountDaoImpl();
		Account account = accountDao.getById(accountid);
		if (account != null) {
			log.debug("Displayed account at id: " + accountid);
			System.out.println(account);
		} else {
			log.debug("No account was found at id: " + accountid);
			System.out.println("No account was found at id: " + accountid);
		}
	}
	public static void displayAccountsByUsername(String username) {
		UserDaoImpl userDao = new UserDaoImpl();
		User user = userDao.getByUsername(username);
		List<Account> accounts = user.getAccounts();
		for (Account a : accounts) {
			System.out.println(a);
		}
	}
	public static void displayAccountsByUserId(int userid) {
		UserDaoImpl userDao = new UserDaoImpl();
		User user = userDao.getById(userid);
		List<Account> accounts = user.getAccounts();
		for (Account a : accounts) {
			System.out.println(a);
		}
	}

	public static void displayAll() {
		AccountDaoImpl accountDao = new AccountDaoImpl();
		List<Account> accountList = accountDao.getAll();
		if (accountList == null) {System.out.println("No accounts found"); return;}
		
		for (Account a : accountList) {
			System.out.println(a);
		}
	}
}
