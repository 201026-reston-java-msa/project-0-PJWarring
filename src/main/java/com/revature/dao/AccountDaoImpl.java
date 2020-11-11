package com.revature.dao;

import java.sql.Connection;
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

public class AccountDaoImpl implements GenericDao<Account>{

	private static Logger log = Logger.getLogger(ConnectionFactory.class);

	@Override
	public int create(Account t) {
		try (Connection conn = ConnectionFactory.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(
					"insert into project0.accounts (balance, status, type) "
					+ "values (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			ps.setDouble(1, t.getBalance());
			ps.setString(2, t.getStatus());
			ps.setString(3, t.getType());
			
			ps.executeUpdate();
			
			try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	                return generatedKeys.getInt(1);
	            } else {
	            	log.warn("Creating user failed - no keys found.");
	                throw new SQLException("Creating user failed - no keys found.");
	            }
	        }
		} catch (SQLException e) {
			log.warn("SQLException " + e);
		}
		return 0; //no account was made
	}

	@Override
	public Account getById(int id) {
		try (Connection conn = ConnectionFactory.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(
					"select * from project0.accounts where id = ?;");
			ps.setInt(1, id);
			
			ResultSet rs = ps.executeQuery();
			Account account = new Account();
			if (rs.next()) {
				account = new Account();
				account.setId(rs.getInt("id"));
				account.setStatus(rs.getString("status"));
				account.setType(rs.getString("type"));
				account.setBalance(rs.getDouble("balance"));
			}
			return account;
		} catch (SQLException e) {
			log.warn("SQLException " + e);
		}
		return null; //no account was found
	}
	
	public List<Account> getByStatus(String status) {
		try (Connection conn = ConnectionFactory.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(
					"select * from project0.accounts where status=? order by id;");
			ps.setString(1, status);
			
			ResultSet rs = ps.executeQuery();
			List<Account> accounts = new ArrayList<>();
			Account account = new Account();
			while (rs.next()) {
				account = new Account();
				account.setId(rs.getInt("id"));
				account.setStatus(rs.getString("status"));
				account.setType(rs.getString("type"));
				account.setBalance(rs.getDouble("balance"));
				accounts.add(account);
			}
			return accounts;
		} catch (SQLException e) {
			log.warn("SQLException " + e);
		}
		return null; //no account was found
	}

	@Override
	public boolean update(Account t) {
		try (Connection conn = ConnectionFactory.getConnection()) {
			PreparedStatement ps = conn.prepareStatement("UPDATE project0.accounts"
					+ " SET balance = ?, status = ?, type = ?"
					+ " WHERE id = ?;");
			
			//Get info
			ps.setDouble(1, t.getBalance());
			ps.setString(2, t.getStatus());
			ps.setString(3, t.getType());
			//What account do we update
			ps.setInt(4, t.getId());
			
			// We use executeUpdate because it is a DML command
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			log.warn("SQLException " + e);
		}
		return false;
	}

	@Override
	public boolean delete(Account t) {
		try (Connection conn = ConnectionFactory.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(
					"delete from project0.accounts where account.id = ?");
			ps.setInt(1, t.getId());
			
			ps.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			log.warn("SQLException " + e);
		}
		return false;
	}

	@Override
	public List<Account> getAll() {
		try (Connection conn = ConnectionFactory.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(
					"select * from project0.accounts order by id;");
			
			ResultSet rs = ps.executeQuery();
			Account account = new Account();
			List<Account> accounts = new ArrayList<>();
			while (rs.next()) {
				account = new Account();
				account.setId(rs.getInt("id"));
				account.setStatus(rs.getString("status"));
				account.setType(rs.getString("type"));
				account.setBalance(rs.getDouble("balance"));
				accounts.add(account);
			}
			return accounts;
		} catch (SQLException e) {
			log.warn("SQLException " + e);
		}
		
		return null; //no accounts were found
	}

}
