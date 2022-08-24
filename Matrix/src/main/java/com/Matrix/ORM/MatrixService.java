package com.Matrix.ORM;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
//	LogBack functionality
	static Logger Log = LoggerFactory.getLogger(Matrix.class);
	public <T> List<T> RetrieveMatrix(Class<T> DaClass){
		ArrayList<T> Matrices = new ArrayList<>();
//		an array of strings grabbed from class objects
		String[] MatrciesNames = DaClass.getName().split("/");
//		All Accessible FIelds
		Field[] fields = DaClass.getFields();
//		getting class name from MatricesNames array
		String ClassName = MatrciesNames[MatrciesNames.length - 1];
//		Adding boilerplate code to select from class name
//		where the class name could be anything
		String sql = "SELECT * from " + ClassName + ";";
		
		try {
//			Lets prepare the statement .. this is too create and send
//			to the database
			PreparedStatement st = connection.prepareStatement(sql);
			ResultSet result = st.executeQuery();
//			Now i want to find all results in the query so ill use a while statement
			while(result.next()) {
				try {
//					The "newInstance" is to initialize the constructor. with the specified parameters
					T NewMatrix = DaClass.getDeclaredConstructor().newInstance();
					for(Field EachField : fields) {
//						intializing a string field name
						String fieldname = EachField.getName();
						
//						Looking for the Approximate getter with the "fieldname"
						String GetterName = "get"+fieldname.substring(0, 1).toUpperCase()+fieldname.substring(1);
//															^^^ "0," is beginning of index & "1" is the endIndex (which is optional)
						String setterName = "set" + fieldname.substring(0, 1).toUpperCase() + fieldname.substring(1);
						try {
//						looking for the type of setter parameter, prioritizing for field type
						Class<?> SetterType = DaClass.getDeclaredField(fieldname).getType();
						
						Method setter = DaClass.getMethod(setterName, SetterType);
						
//						Finding the appropriate Type
						Object FieldValue = convertStringToFieldType(result.getString(fieldname), SetterType);
						
//						I use the invoke method to populate the field
						setter.invoke(NewMatrix, FieldValue);
						
						} catch (NoSuchFieldException e) {
							Log.error(e.getLocalizedMessage(), e);
						} catch (NoSuchMethodException e) {
							Log.error(e.getLocalizedMessage(), e);
						} catch (IllegalAccessException e) {
							Log.error(e.getLocalizedMessage(), e);
						} catch (InvocationTargetException | InstantiationException e) {
							Log.error(e.getLocalizedMessage(), e);
						}
					}
					Matrices.add(NewMatrix);
				} catch (InstantiationException | IllegalAccessException | InvocationTargetException
						| NoSuchMethodException e) {
					Log.error(e.getLocalizedMessage(), e);
				}
			}
			return (List<T>) Matrices;
		} catch(SQLException e) {
//			return object
			System.out.println(e);
			Log.error(e.getLocalizedMessage());
		}
//		if nothing is reached
		return null;
	}
	
//	Using a object type
	private static void printMatrixType(Object o) {
		if(o.getClass() == Matrix.class) {
			System.out.println("You have entered the matrix");
		} else {
			System.out.println("This program doesn't support the omniverse");
		}
	}
	
	protected Object convertStringToFieldType(String input, Class<?> type)
			throws IllegalAccessException, InstantiationException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		switch (type.getName()) {
		case "byte":
			return Byte.valueOf(input);
		case "short":
			return Short.valueOf(input);
		case "int":
			return Integer.valueOf(input);
		case "long":
			return Long.valueOf(input);
		case "double":
			return Double.valueOf(input);
		case "float":
			return Float.valueOf(input);
		case "boolean":
			return Boolean.valueOf(input);
		case "java.lang.String":
			return input;
		case "java.time.LocalDate":
			return LocalDate.parse(input);
		default:
			return type.getDeclaredConstructor().newInstance();
		}
	}
	
}
