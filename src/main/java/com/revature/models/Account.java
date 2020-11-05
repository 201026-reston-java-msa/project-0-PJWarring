package com.revature.models;

public class Account {

	private int id;
	private double balance;
	private String type;
	private String status;
	
	public Account() {
		this(0.0, "Savings");
	}
	
	public Account(double balance, String type) {
		this.id = 0;
		this.balance = balance;
		this.type = type;
		this.status = "Pending";
	}
}
