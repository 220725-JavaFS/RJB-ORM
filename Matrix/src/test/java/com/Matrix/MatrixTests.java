package com.Matrix;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

// tomcat version 9
// i will be using reflection to examine my fields and methods
//@SpringBootTest
class MatrixTests {
	
	private int id;
	private String ObjectName;
	private String ObjectType;
	private static Matrix matrix;

	@BeforeAll
	static void GetObjects() {
		System.out.println("All Objects Will Be Loaded Soon");
	}
	
	@BeforeEach
	void ResetObjects() {
		System.out.println("All Objects Will Be Loaded Soon");
		id = 1;
		ObjectName = "Robot";
		ObjectType = "Metal";
		matrix = new Matrix();
	}
	
	@Test
	void TrueObjectName() {
		matrix.setObjectName(ObjectName);
		assertEquals("Robot", matrix.getObjectName());
	}
	
	@Test
	void FalseObjectName() {
		matrix.setObjectName(ObjectName);
		assertNotEquals("Dolphin", matrix.getObjectName());
	}
	
	@Test
	void TrueObjectType() {
		matrix.setObjectType(ObjectType);
		assertEquals("Metal", matrix.getObjectType());
	}
	
	@Test
	void FalseObjectType() {
		matrix.setObjectType(ObjectType);
		assertNotEquals("Wood", matrix.getObjectType());
	}
	
	@Test
	void TrueObjectID() {
		matrix.setId(id);
		assertEquals(1, matrix.getId());
	}
	
	@Test
	void FalseObjectID() {
		matrix.setId(id);
		assertNotEquals(2, matrix.getId());
	}
	
	

}
