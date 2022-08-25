package com.Matrix.ORM;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.Matrix.Matrix;

class MatrixJsonMappingTests {
	
	static Matrix matrix;
	static MatrixMapper MM;
	
	@BeforeAll
	static void EvolveMatrix() {
		matrix = new Matrix(100, "Demi Matrix", "Galaxy");
		MatrixMapper MM = new MatrixJsonMapper();
	}

	@Test
	void MatrixToString() {
		String json = MM.serialize(matrix);
		System.out.println(json);
//		assertEquals(json, "");
		assertEquals("", "");
	}
	
//	@Test
//	void StringToMatrix() {
//		Matrix NewMatrix = MM.deSerialize(
//		"{ \"id\": \"1\", \"Name\": \"New Matrix\", \"Type\": \"Nebula\" }", 
//		Matrix.class);
//		assertEquals(NewMatrix, Matrix.class);
//	}

}
