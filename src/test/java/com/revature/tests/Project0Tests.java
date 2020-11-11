package com.revature.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.revature.services.AccountService;
import com.revature.services.LoginService;
import com.revature.services.UserService;
import com.revature.utils.StringUtil;

public class Project0Tests {

	@Test
	public void loginServiceTests() {
		assertTrue(LoginService.login("root", "changeme"));
		assertFalse(LoginService.login("fakeUsername", "fakePassword"));
	}
	
	@Test
	public void userServiceTests() {
		assertTrue(UserService.isValidUsername("thisisanUnusedUsername1023984"));
		assertFalse(UserService.isValidUsername("root"));
		
		assertTrue(UserService.isValidPassword("anyPassword"));
		assertFalse(UserService.isValidPassword(""));
	}
	
	@Test
	public void accountServiceTests() {
		assertTrue(AccountService.isOpenAccount(1));
		assertFalse(AccountService.hasFunds(1));
	}
	
	@Test
	public void stringUtilTests() {
		assertTrue(StringUtil.isInt("5"));
		assertFalse(StringUtil.isInt("s"));
	}

}
