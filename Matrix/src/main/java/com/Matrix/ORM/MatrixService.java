package com.Matrix.ORM;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
	
	public <T> List<T> RetrieveMatrix(Class<T> DaClass){
		ArrayList<?> Matrices = new ArrayList<T>();
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
						
//						looking for the type of setter parameter, prioritizing for field type
						Class<?> SetterType = DaClass.getDeclaredField(fieldname).getType();
					}
					// obtain the getter method from the class we are mapping 
					Method getterMethod = objectClass.getMethod(getterName);
					
					// invoke that method on the object that we are mapping
					Object fieldValue = getterMethod.invoke(o);
				
//					System.out.println(fieldValue.getClass());
									
					// construct a key value pair for each field name and field value
					String jsonKeyValuePair = " \""+fieldName +"\""+" : \""+ fieldValue + "\",";
					
					// combine all of the key value pairs into a result string
					jsonBuilder.append(jsonKeyValuePair);
				
				
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Matrices.add(NewMatrix);
		} catch(SQLException e) {
//			return object
			System.out.println(e);
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
	
	
}
