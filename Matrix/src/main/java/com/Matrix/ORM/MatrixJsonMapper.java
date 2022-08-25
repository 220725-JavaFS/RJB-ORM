package com.Matrix.ORM;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;

import com.Matrix.Exception.JsonMapperException;

public class MatrixJsonMapper implements MatrixMapper {
	MatrixService ServicePointer = new MatrixService();
	// the goal of this method is to take any object and provide the string JSON representation of that object
	@Override
	public String serialize(Object o) {
		
		// obtain the names of the fields in the object
		Class<?> matrixClass = o.getClass();
		Field[] fields = matrixClass.getDeclaredFields();
		
		StringBuilder jsonBuilder = new StringBuilder("{");
		
		for(Field field: fields) {		
			
			String fieldName = field.getName();
			
			// obtain the appropriate getter (using the field name)
			String getterName = "get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
//			System.out.println(getterName);
		
			try {
				// obtain the getter method from the class we are mapping 
				Method getterMethod = matrixClass.getMethod(getterName);
				
				// invoke that method on the object that we are mapping
				Object fieldValue = getterMethod.invoke(o);
			
//				System.out.println(fieldValue.getClass());
								
				// construct a key value pair for each field name and field value
				String jsonKeyValuePair = " \""+fieldName+"\""+": \""+ fieldValue+"\",";
				
				// combine all of the key value pairs into a result string
				jsonBuilder.append(jsonKeyValuePair);
			
			
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			
		}
				
		return jsonBuilder.substring(0, jsonBuilder.length()-1) + " }";
	}

	
	// the goal of this method is to take a JSON string and convert it into an object 
		// to do this, we declare a generic type <T> to be used in the method
		// this type is the type of the class we're providing to the method as well as the type we're going to return from the method
	/**
	 * @param 	input	the JSON string to be deserialized
	 * @param	Matrix	the reference type that the JSON string is being converted into
	 * @return	the Java object created by the JSON string 
	 */
	@Override
    public <T> T deSerialize(String input, Class<T> Matrix) {
        if(input==null || input.equals("")){
            return null;
        }
        
        // remove the curly brackets from the JSON string
        String partialJson = input.substring(1,input.length()-1);
        // split the remaining string by the comma to get key value pair string pairs 
        String[] keyValueStrings = partialJson.split(",");
        
        // declare an object to be created
        T newObject = null;

        try {
        	// create a new instance of the class being constructed
            newObject = Matrix.getDeclaredConstructor().newInstance();
        
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        // we need to extract the field name and field value from each string key value pair 
        for(String s: keyValueStrings){
            // splitting a key value pair (ex. "name" : "Sally") by the colon should give us an array of size 2 ( ["name":"Sally"] )
        	String[] keyValueArr = s.split(":");
            if(keyValueArr.length != 2){
            	// if we don't get an array of size 2, the json is formatted incorrectly 
            		// here we tell the developer why the method fails with a custom exception
                throw new JsonMapperException("Improperly formatted JSON");
            }
            
            // first we deal with the field name - trim the whitespace and remove the quotes
            String keyString = keyValueArr[0].trim();
            keyString = keyString.substring(1, keyString.length()-1);
            
            // next the value - we also trim the whitespace and remove the quotes
            String valueString = keyValueArr[1].trim();
            valueString = valueString.substring(1, valueString.length()-1);
//            System.out.println("key string: "+keyString + "   - value string: "+valueString );

            //obtain each setter method we need to set the values
            String setterName = "set"+keyString.substring(0,1).toUpperCase() + keyString.substring(1);
            try {
                // getting the type of the setter parameter, based on the field type
                Class<?> setterParamType = Matrix.getDeclaredField(keyString).getType();

                // obtain the setter method using the setter name and setter parameter type
                Method setter = Matrix.getMethod(setterName, setterParamType);

                // below we define a utility method to convert the string field value to the appropriate type for the field  
                Object fieldValue = ServicePointer.convertStringToFieldType(valueString, setterParamType);

                // we invoke the setter to populate the field of the object that's being created
                setter.invoke(newObject,fieldValue);
                
            } catch (NoSuchFieldException  e) {
                throw new JsonMapperException(keyString+" field does not exist in class "+Matrix);
            } catch ( NoSuchMethodException e){
                throw new JsonMapperException("no valid setter for: "+keyString);
            } catch (IllegalAccessException e) {
                throw new JsonMapperException("cannot access setter for: "+keyString);
            } catch (InvocationTargetException | InstantiationException e) {
                throw new JsonMapperException("issue invoking setter for: "+keyString);
            }

        }
        // once we iterate over each field, the object we're attempting to create is fully populated and ready to return 
        return newObject;
    }
	
}