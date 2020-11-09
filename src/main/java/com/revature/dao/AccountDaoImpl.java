package com.revature.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.log4j.Logger;

import com.revature.models.Account;
import com.revature.utils.ConnectionFactory;

public class AccountDaoImpl implements GenericDao<Account>{

	private static Logger log = Logger.getLogger(ConnectionFactory.class);

	@Override
	public int create(Account t) {
		try {
			PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(
					"insert into accounts (balance, statusid, typeid) "
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
			e.printStackTrace();
		}
		return 0; //no account was made
	}

	@Override
	public Account getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(Account t) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(Account t) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Account> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
