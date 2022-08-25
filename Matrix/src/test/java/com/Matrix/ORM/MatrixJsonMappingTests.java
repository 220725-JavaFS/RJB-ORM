package com.Matrix.ORM;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.Matrix.Matrix;

class MatrixJsonMappingTests {
	
	static Matrix matrix;
	static MatrixMapper MM;
	static String Test;
	
	@BeforeAll
	static void EvolveMatrix() {
		matrix = new Matrix(100, "Demi Matrix", "Galaxy");
		MM = new MatrixJsonMapper();
		Test = "{ \"id\": \"100\", \"ObjectName\": \"Demi Matrix\", \"ObjectType\": \"Galaxy\" }";
	}

	@Test
	void MatrixToString() {
		String json = MM.serialize(matrix);
		System.out.println(json);
		System.out.println(Test);
		assertEquals(json, Test);
//		assertEquals("", "");
	}
	
	@Test
	void StringToMatrix() {
		Matrix NewMatrix = MM.deSerialize(
		"{ \"id\": \"1\", \"ObjectName\": \"New Matrix\", \"ObjectType\": \"Nebula\" }", 
		Matrix.class);
		Matrix MatrixTest = new Matrix(1, "New Matrix", "Nebula");
		assertEquals(NewMatrix, MatrixTest);
	}

}
