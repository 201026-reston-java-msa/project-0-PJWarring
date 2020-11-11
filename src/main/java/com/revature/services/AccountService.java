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
	
	public static boolean deposit(int accountid, double amount) {
		if (isOpenAccount(accountid)) {
			AccountDaoImpl accountDao = new AccountDaoImpl();
			Account account = accountDao.getById(accountid);
			double newBalance = account.getBalance() + amount;
			log.info("Depositing $" + amount + " into account at the id " + accountid +
					" this changes the balance from $" + account.getBalance() + " to $" + newBalance);
			account.setBalance(newBalance);
			return accountDao.update(account);
		} else {
			log.debug("Can not deposit into accounts that are not open.");
			System.out.println("You can not deposit into accounts that are not open.");
			return false;
		}
	}
	
	public static boolean withdraw(int accountid, double amount) {
		if (isOpenAccount(accountid)) {
			AccountDaoImpl accountDao = new AccountDaoImpl();
			Account account = accountDao.getById(accountid);
			if (!isValidWithdrawAmount(accountid, amount)) {
				log.info("Could not withdraw from account at id " + accountid + " because it would result " + 
						"in an overdraft (withdraw amount: $" + amount + ", account balance: $" + 
						account.getBalance());
				System.out.println("You can not overdraw from accounts.");
				return false;
			}
			double newBalance = account.getBalance() - amount;
			log.info("Withdrawing $" + amount + " from account at the id " + accountid +
					" this changes the balance from $" + account.getBalance() + " to $" + newBalance);
			account.setBalance(newBalance);
			return accountDao.update(account);
		} else {
			log.debug("Can not withdraw from accounts that are not open.");
			System.out.println("You can not withdraw from accounts that are not open.");
			return false;
		}
	}
	
	public static boolean transfer(int senderAccountId, int recieverAccountId, double amount) {
		if (isOpenAccount(senderAccountId) && isOpenAccount(recieverAccountId)) {	
			log.info("Begining transfer of $" + amount + " from id " + senderAccountId + " to id " + recieverAccountId);
			if (withdraw(senderAccountId, amount)) {
				if (deposit(recieverAccountId, amount)) {
					log.info("Transfer successful.");
					return true;
				} else {
					log.info("Transfer failed."); //this dbg message is so simple because deposit should log its own failure states
					log.debug("Transfer failed at deposit stage.");
					return false;
				}
			} else {
				log.info("Transfer failed."); //this dbg message is so simple because withdraw should log its own failure states
				log.debug("Transfer failed at withdraw stage.");
				return false;
			}
		} else {
			log.debug("Can not transfer to or from accounts that are not open.");
			System.out.println("You can not transfer to or from accounts that are not open.");
			return false;
		}
	}
	
	public static boolean isValidWithdrawAmount(int accountid, double amount) {
		AccountDaoImpl accountDao = new AccountDaoImpl();
		Account account = accountDao.getById(accountid);
		if (account.getBalance() - amount >= 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isOpenAccount(int accountid) {
		AccountDaoImpl accountDao = new AccountDaoImpl();
		return accountDao.getById(accountid).getStatus().equals("Open");
	}
	
	public static boolean hasFunds(int accountid) {
		AccountDaoImpl accountDao = new AccountDaoImpl();
		return accountDao.getById(accountid).getBalance() > 0;
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
