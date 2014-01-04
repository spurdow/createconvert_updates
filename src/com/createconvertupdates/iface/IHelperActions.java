package com.createconvertupdates.iface;

import java.util.List;

public interface IHelperActions<T> {
	
	/**
	 *  add Object to table
	 *  returns either true or false
	 * @param <T>
	 */
	public long add(T object);
	
	/**
	 * 
	 * get Object from the table
	 * @param <E>
	 */
	public T get(long id);
	
	/**
	 *  update object from the table
	 * @param <T>
	 */
	public boolean update(long id , T object);
	
	/**
	 * note: should never used , never to delete data from database
	 * removes data from the database
	 * @param id
	 * @return
	 */
	public T delete(long id , T object);
	
	/**
	 *  get all objects from the table
	 * @param <E>
	 */
	public List<T> getAll();
	
}
