package com.revature.dao;

import java.util.List;

public interface GenericDao<T> {

	public int create(T t); //Create new entry
	
	public T getById(int id); //Retrieve entry by id
	
	public boolean update(T t); //Update entry in database
	
	public boolean delete(T t); //Delete entry from database
	
	public List<T> getAll(); //Retrieve a list of all entries
}
