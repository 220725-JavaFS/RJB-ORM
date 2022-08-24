package com.Matrix.ORM;

public interface MatrixMapper {
	String serialize(Object o);
	
	<T> T deSerialize(String string, Class<T> DaClass);
	
}
