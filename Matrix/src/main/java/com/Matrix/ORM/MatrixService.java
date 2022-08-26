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

/*		NOTES
 * Based on any type of class that is used i 
 * will append json (Object Strings) to be read as sql statements
 * for any webapp.
 */

public class MatrixService implements MatrixServiceImplementation {
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
/*
* READING 
*/
//	LogBack functionality
	static Logger Log = LoggerFactory.getLogger(Matrix.class);
	public <T> List<T> RetrieveMatrix(Class<T> DaClass){
		ArrayList<T> Matrices = new ArrayList<T>();
//		an array of strings grabbed from class objects
		String[] MatrciesNames = DaClass.getName().split("\\.");
//		"/" was previously in my split method. Now I have "\\."
//		All Accessible FIelds
		Field[] fields = DaClass.getFields();
//		getting class name from MatricesNames array
		String ClassName = MatrciesNames[MatrciesNames.length - 1];
//		Adding boilerplate code to select from class name
//		where the class name could be anything
		String sql = "SELECT * FROM " + ClassName + ";";
		Log.info(sql);
//		adding a logging method here
		
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
						String MatrixFieldname = EachField.getName();
						
//						Looking for the Approximate getter with the "MatrixFieldname"
//						String GetterName = "get"+MatrixFieldname.substring(0, 1).toUpperCase()+MatrixFieldname.substring(1);
//															^^^ "0," is beginning of index & "1" is the endIndex (which is optional)
//						^^^^^ USING THIS AS AN EXAMPLE
						String SetterName = "set" + MatrixFieldname.substring(0, 1).toUpperCase() + MatrixFieldname.substring(1);
						try {
//						looking for the type of setter parameter, prioritizing for field type
						Class<?> SetterType = DaClass.getDeclaredField(MatrixFieldname).getType();
						
						Method MatrixSetter = DaClass.getMethod(SetterName, SetterType);
						
//						Finding the appropriate Type
						Object MatrixFieldValue = convertStringToFieldType(result.getString(MatrixFieldname), SetterType);
						
//						I use the invoke method to populate the field
						MatrixSetter.invoke(NewMatrix, MatrixFieldValue);
						
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
			Log.info("Matrix Retrived");
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
//	^^^^^ USING THIS AS AN EXAMPLE
	
	private static void AppendingFieldToBuilder(Object O, StringBuilder SB) {
		if (O.getClass() == String.class) {
			SB.append("'" + O + "',");
		} else {
			SB.append(O + ",");
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
/*
 * CREATING 
 */
	public int StoreMatrix(Object o) {

		StringBuilder MatrixStatementBuilder = new StringBuilder();

//		Piecing out the SQL statement
		MatrixStatementBuilder.append("INSERT INTO ");
		// obtain the matrix names of the fields in the object
		Class<?> MatrixClass = o.getClass();
		String[] tokens = MatrixClass.getName().split("\\.");
		String MatrixName = tokens[tokens.length - 1];
//		the name from index of tokens
		MatrixStatementBuilder.append(MatrixName + "(");

		Field[] MatrixFields = MatrixClass.getFields();

		StringBuilder MatrixValueBuilder = new StringBuilder();
		StringBuilder MatrixFieldBuilder = new StringBuilder();
		for (Field field : MatrixFields) {

			String fieldName = field.getName();

			if (fieldName == "id") {
				continue;
			}

			MatrixFieldBuilder.append(fieldName + ",");

			// obtain the appropriate getter (using the field name)
			String MatrixGetterName = "get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
//			System.out.println(getterName);

			try {
				// obtain the getter method from the class we are mapping
				Method MatrixGetterMethod = MatrixClass.getMethod(MatrixGetterName);

				// invoke that method on the object that we are mapping
				Object MatrixFieldValue = MatrixGetterMethod.invoke(o);
				
				AppendingFieldToBuilder(MatrixFieldValue, MatrixValueBuilder);

			} catch (NoSuchMethodException e) {
				Log.error(e.getLocalizedMessage(), e);
			} catch (SecurityException e) {
				Log.error(e.getLocalizedMessage(), e);
			} catch (IllegalAccessException e) {
				Log.error(e.getLocalizedMessage(), e);
			} catch (IllegalArgumentException e) {
				Log.error(e.getLocalizedMessage(), e);
			} catch (InvocationTargetException e) {
				Log.error(e.getLocalizedMessage(), e);
			}

		}

		MatrixFieldBuilder.deleteCharAt(MatrixFieldBuilder.length() - 1);
		MatrixValueBuilder.deleteCharAt(MatrixValueBuilder.length() - 1);

		MatrixStatementBuilder.append(MatrixFieldBuilder + ") VALUES (" + MatrixValueBuilder + ") RETURNING id;");
/*
 * 		RETURNING - 
 * The returning clause specifies the values return 
 * from DELETE , EXECUTE IMMEDIATE , INSERT , and UPDATE 
 * statements.
 */

		String SQL = MatrixStatementBuilder.toString();
		Log.info(SQL);
		try {
			PreparedStatement statement = connection.prepareStatement(SQL);
			ResultSet result = statement.executeQuery();
			Log.info("Matrix Stored.");
			if (result.next()) {
				return result.getInt("id");
			}

		} catch (SQLException e) {
			Log.error(e.getLocalizedMessage(), e);
		}
//		If nothing is witnessed
		return -1;
	}
/*
* DELETING 
*/
	
	public boolean DeleteMatrix(Object o) {

		StringBuilder MatrixStatementBuilder = new StringBuilder();
		MatrixStatementBuilder.append("DELETE FROM ");

		// obtain the names of the fields in the object
		Class<?> MatrixClass = o.getClass();
		String[] MatrixTokens = MatrixClass.getName().split("\\.");
		String MatrixClassName = MatrixTokens[MatrixTokens.length - 1];

		MatrixStatementBuilder.append(MatrixClassName + " WHERE  id  =");

		String MatrixGetterName = "getId";

		try {
			// obtain the MatrixGetterMethod from the class we are mapping
			Method MatrixGetterMethod = MatrixClass.getMethod(MatrixGetterName);

			// invoke that method on the object that we are mapping
			Object MatrixfieldValue = MatrixGetterMethod.invoke(o);

			MatrixStatementBuilder.append(MatrixfieldValue + ";");

		} catch (NoSuchMethodException e) {
			Log.error(e.getLocalizedMessage(), e);
		} catch (SecurityException e) {
			Log.error(e.getLocalizedMessage(), e);
		} catch (IllegalAccessException e) {
			Log.error(e.getLocalizedMessage(), e);
		} catch (IllegalArgumentException e) {
			Log.error(e.getLocalizedMessage(), e);
		} catch (InvocationTargetException e) {
			Log.error(e.getLocalizedMessage(), e);
		}

		String SQL = MatrixStatementBuilder.toString();
		Log.info(SQL);
		try {
			PreparedStatement statement = connection.prepareStatement(SQL);
			statement.executeQuery();
			Log.info("Matrix Deleted.");
			return true;
		} catch (SQLException e) {
			Log.error(e.getLocalizedMessage(), e);
		}
		return false;

	}
	
/*
* UPDATING 
*/
	
	public boolean MatrixUpdate(Object o) {

		StringBuilder MatrixBuilder = new StringBuilder();
		MatrixBuilder.append("UPDATE ");

		// ^^^ Updating Matrix builder is made here 
		Class<?> MatrixClass = o.getClass();
		String[] MatrixTokens = MatrixClass.getName().split("\\.");
		String MatrixClassName = MatrixTokens[MatrixTokens.length - 1];

		MatrixBuilder.append(MatrixClassName + " SET ");

		Field[] MatrixFields = MatrixClass.getFields();

		String id = "";
		for (Field field : MatrixFields) {

			String MatrixFieldName = field.getName();

			// obtain the appropriate getter (using the MatrixFieldName)
			String MatrixGetterName = "get" + MatrixFieldName.substring(0, 1).toUpperCase() + MatrixFieldName.substring(1);
//			System.out.println(getterName);

			try {
				// obtain the MatrixGetterMethod from the class we are mapping
				Method MatrixGetterMethod = MatrixClass.getMethod(MatrixGetterName);

				// invoke that method on the object that we are mapping
				Object MatrixFieldValue = MatrixGetterMethod.invoke(o);

				if (MatrixFieldName == "id") {
					id = MatrixFieldValue.toString();
					continue;
				}
				if (MatrixFieldValue.getClass() == String.class) {
					MatrixBuilder.append(MatrixFieldName + " = '" + MatrixFieldValue + "',");
				} else {
					MatrixBuilder.append(MatrixFieldName + " = " + MatrixFieldValue + ",");
				}
//				^^^^ same as "AppendingFieldToBuilder" method but a little different
//				I've added  " = " & commas to append to the builder. to than over have my sql statement

			} catch (NoSuchMethodException e) {
				Log.error(e.getLocalizedMessage(), e);
			} catch (SecurityException e) {
				Log.error(e.getLocalizedMessage(), e);
			} catch (IllegalAccessException e) {
				Log.error(e.getLocalizedMessage(), e);
			} catch (IllegalArgumentException e) {
				Log.error(e.getLocalizedMessage(), e);
			} catch (InvocationTargetException e) {
				Log.error(e.getLocalizedMessage(), e);
			}

		}

		MatrixBuilder.deleteCharAt(MatrixBuilder.length() - 1);
		MatrixBuilder.append(" WHERE id = " + id + ";");
//		Putting everything altogether

		String sql = MatrixBuilder.toString();
		Log.info(sql);
		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.executeQuery();
			Log.info("Object updated.");
			return true;
		} catch (SQLException e) {
			Log.error(e.getLocalizedMessage(), e);
		}
		return false;
	}
	
}
