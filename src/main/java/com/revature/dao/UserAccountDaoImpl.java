package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.revature.utils.ConnectionFactory;

public class UserAccountDaoImpl {
	//this does not implement generic dao because it does not need all 4 CRUD operations
	//just create and delete
	
	public boolean create(int userId, int accountId) {
		try (Connection conn = ConnectionFactory.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(
					"insert into project0.users_accounts (userid, accountid) "
					+ "values (?, ?)");
			ps.setInt(1, userId);
			ps.setInt(2, accountId);
			
			ps.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			//log the warning
			e.printStackTrace();
		}
		return false; //no user_account was created
	}
	
	public boolean deleteByUser(int userId) {
		try (Connection conn = ConnectionFactory.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(
					"delete from project0.users_accounts where userid = ?");
			ps.setInt(1, userId);
			
			ps.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			//log the warning
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean deleteByAccount(int accountId) {
		try (Connection conn = ConnectionFactory.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(
					"delete from project0.users_accounts where userid = ?");
			ps.setInt(1, accountId);
			
			ps.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			//log the warning
			e.printStackTrace();
		}
		return false;
	}

}
