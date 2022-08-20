package com.Matrix.Controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectController extends HttpServlet {
	private MatrixService matrixService = new MatrixService();
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	protected void GetMatrixList(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
			List<Matrix> list = matrixService.configureMatrix();
			
			String json = objectMapper.writeValueAsString(list);
			System.out.println(json);
			
			PrintWriter printWriter = response.getWriter();
			printWriter.print(json);
			
			response.setStatus(200);
			
			response.setContentType("application/json");
//			passing information to backend
	}
}
