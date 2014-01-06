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
	 * update object from the table
	 * @param id
	 * @param object
	 * @return
	 */
	public boolean update(long id , T object);
	
	/**
	 * note: should never used , never to delete data from database
	 * removes data from the database
	 * @param id
	 * @param object
	 * @return
	 */
	public T delete(long id , T object);

	
}
