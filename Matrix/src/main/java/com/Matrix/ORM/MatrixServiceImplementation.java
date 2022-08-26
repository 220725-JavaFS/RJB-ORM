package com.Matrix.ORM;

import java.sql.Connection;
import java.util.List;

public interface MatrixServiceImplementation {
	void EnterTheMatrix(Connection connection);
	
	<T> List<T> RetrieveMatrix(Class<T> DaClass);
	
	int StoreMatrix(Object o);
	
	boolean DeleteMatrix(Object o);
	
	boolean MatrixUpdate(Object o);
}
