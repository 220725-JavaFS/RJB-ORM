package com.Matrix.Controllers;

import java.io.PrintWriter;

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
	}
}
