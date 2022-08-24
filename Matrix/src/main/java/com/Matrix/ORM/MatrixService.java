package com.Matrix.ORM;

import java.lang.reflect.Field;
import java.sql.Connection;

import com.Matrix.Matrix;

public class MatrixService {
	Matrix matrix = new Matrix(0, "Vortex", "Space");
	private Connection connection;
	Class<?> ClassOne = matrix.getClass();
	Field[] fields = ClassOne.getFields();
	
	public MatrixService() {
		
	}
	
	public MatrixService(Connection connection) {
		this.connection = connection;
	}
	
	public void EnterTheMatrix(Connection connection) {
//		To Use As A Method For Objects
		this.connection = connection;
	}
	
	
}
