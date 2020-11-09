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

	@Override
	public String toString() {
		return "Account [id=" + id + ", balance=" + balance + ", type=" + type + ", status=" + status + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
