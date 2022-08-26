package com.Matrix.ORM;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.Matrix.Matrix;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/*		NOTES
 * Using mockito and junit .. logback is in the Matrix Service class
 */

public class MatrixServiceTests {
	
	private Connection MockConnection = Mockito.mock(Connection.class);
	private ResultSet MockResultSet = Mockito.mock(ResultSet.class);
	private PreparedStatement MockPreparedStatement = Mockito.mock(PreparedStatement.class);
	private MatrixService matrixORM = new MatrixService(MockConnection);
	private static Matrix TestMatrix = new Matrix(1, "UnknownMatrix", "UnStudied");
	
	@BeforeAll
	static void AwakenMatrix() {
		System.out.println(
		"Update On Matrix: \n"+"{\t"+TestMatrix+"\t}"
		);
	}
	
	@BeforeEach
	void MockMatrix() {
		try {
			Mockito.when(MockConnection.prepareStatement(Mockito.anyString())).thenReturn(MockPreparedStatement);
//			^^ "when" is used to mock something for an particular return
//			thenReturn to return specified value
			Mockito.when(MockPreparedStatement.executeQuery()).thenReturn(MockResultSet);
			Mockito.when(MockResultSet.next()).thenReturn(true).thenReturn(false);
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void TestStoreMatrix() {

		try {
			Mockito.when(MockResultSet.getInt("id")).thenReturn(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int id = matrixORM.StoreMatrix(TestMatrix);
		assertEquals(TestMatrix.getId(), id);
	}
	
	@Test
	void TestRetrieveMatrix(){
		try {
			Mockito.when(MockResultSet.getString("id")).thenReturn("1");
			Mockito.when(MockResultSet.getString("ObjectName")).thenReturn("UnknownMatrix");
			Mockito.when(MockResultSet.getString("ObjectType")).thenReturn("UnStudied");
//			^^^ testing against the TestMatrix with mockito
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Matrix> MatrixTestList = matrixORM.RetrieveMatrix(Matrix.class);

		Matrix test = MatrixTestList.get(0);

		assertEquals(TestMatrix, test);
	}
	
	@Test
	public void testDeleteObject() {
		Boolean updated = matrixORM.DeleteMatrix(TestMatrix);
		assertTrue(updated);
	}
	
	@Test
	public void testUpdateObject() {
		Boolean updated = matrixORM.MatrixUpdate(TestMatrix);
		assertTrue(updated);
	}
	
	

}
