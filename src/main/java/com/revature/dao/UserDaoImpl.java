package com.revature.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.revature.models.Account;
import com.revature.models.User;
import com.revature.utils.ConnectionFactory;

public class UserDaoImpl implements GenericDao<User> {

	private static Logger log = Logger.getLogger(ConnectionFactory.class);

	@Override
	public int create(User t) {
		try {
			PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(
					"insert into users (username, password, first_name, last_name, email, role) "
					+ "values (?, ?, ?, ?, ?, ?)");
			ps.setString(1, t.getUsername());
			ps.setString(2, t.getPassword());
			ps.setString(3, t.getFirstName());
			ps.setString(4, t.getLastName());
			ps.setString(5, t.getEmail());
			ps.setString(6, t.getRole());
			
			ps.executeUpdate();
			
			return 1;
		} catch (SQLException e) {
			//log the warning
			e.printStackTrace();
		}
		return 0; //no user was created
	}

	@Override
	public User getById(int id) {
		try {
			PreparedStatement ps = 
					ConnectionFactory.getConnection().prepareStatement(
							"select * from users as u "
							+ "left join users_accounts as ua on u.id = ua.userid "
							+ "left join accounts as a on ua.accountid = a.id "
							+ "where u.id = ?;");
			ps.setInt(1, id);
			
			// We use executeQuery because it is a DQL command.
			ResultSet rs = ps.executeQuery();
			User user = new User();
			Account account = new Account();
			List<Account> accounts = new ArrayList<Account>();
			if (rs.next()) {
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setFirstName(rs.getString("first_name"));
				user.setLastName(rs.getString("last_name"));
				user.setEmail(rs.getString("email"));
				user.setRole(rs.getString("role"));
				do {
					account = new Account();
					account.setId(rs.getInt("accountId"));
					account.setStatus(rs.getString("status"));
					account.setType(rs.getString("type"));
					account.setBalance(rs.getDouble("balance"));
					accounts.add(account);
					if (account.getId() == 0) {accounts=null;}
				} while (rs.next());
			}
			
			user.setAccounts(accounts);
			System.out.println(user);
			return user;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// There were 0 records returned
		return null;
	}
	
	public User getByUsername(String username) {
		try {
			PreparedStatement ps = 
					ConnectionFactory.getConnection().prepareStatement(
							"select * from users as u "
							+ "left join users_accounts as ua on u.id = ua.userid "
							+ "left join accounts as a on ua.accountid = a.id "
							+ "where u.username = ?;");
			ps.setString(1, username);
			
			ResultSet rs = ps.executeQuery();
			User user = new User();
			Account account = new Account();
			List<Account> accounts = new ArrayList<Account>();
			if (rs.next()) {
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setFirstName(rs.getString("first_name"));
				user.setLastName(rs.getString("last_name"));
				user.setEmail(rs.getString("email"));
				user.setRole(rs.getString("role"));
				do {
					account = new Account();
					account.setId(rs.getInt("accountId"));
					account.setStatus(rs.getString("status"));
					account.setType(rs.getString("type"));
					account.setBalance(rs.getDouble("balance"));
					accounts.add(account);
					if (account.getId() == 0) {accounts=null;}
				} while (rs.next());
			}
			
			user.setAccounts(accounts);
			System.out.println(user);
			return user;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// There were 0 records returned
		return null;
	}

	@Override
	public boolean update(User t) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(User t) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<User> getAll() {
		try {
			PreparedStatement ps = 
					ConnectionFactory.getConnection().prepareStatement(
							"select * from users as u "
							+ "left join users_accounts as ua on u.id = ua.userid "
							+ "left join accounts as a on ua.accountid = a.id "
							+ "order by u.id;");
			
			// We use executeQuery because it is a DQL command.
			ResultSet rs = ps.executeQuery();
	
			User prevUser = null;
			List<User> users = new ArrayList<User>();
			List<Account> accounts = new ArrayList<Account>();
			while (rs.next()) {
				User user = new User();
				Account account = new Account();
				
				//get user data
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setFirstName(rs.getString("first_name"));
				user.setLastName(rs.getString("last_name"));
				user.setEmail(rs.getString("email"));
				user.setRole(rs.getString("role"));
				
				if (!user.equals(prevUser)) accounts = new ArrayList<Account>();
				
				account = new Account();
				account.setId(rs.getInt("accountId"));
				account.setStatus(rs.getString("status"));
				account.setType(rs.getString("type"));
				account.setBalance(rs.getDouble("balance"));
				accounts.add(account);
				if (account.getId() == 0) {accounts=null;}
				
				user.setAccounts(accounts);
				
				if (!user.equals(prevUser)) users.add(user);
				else users.get(prevUser.getId()-1).setAccounts(accounts);
				//save the current user
				prevUser = user;
				
				return users;
			}
		} catch (SQLException e) {
			//log crash here
		}
		return null; //no users found
	}

}
