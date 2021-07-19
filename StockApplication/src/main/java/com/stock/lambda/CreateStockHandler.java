package com.stock.lambda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.model.StockDbConnection;
import com.stock.model.StockDto;
import com.stock.model.StockResponse;

public class CreateStockHandler {

	public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context)
			throws JsonProcessingException {
		Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		headers.put("X-Custom-Header", "application/json");
		StockResponse stockResponse = new StockResponse();
		ObjectMapper mapper = new ObjectMapper();
		try {
			StockDto stockRequest = mapper.readValue(input.getBody(), StockDto.class);
			Connection con = StockDbConnection.getConnection();
			PreparedStatement st = con
					.prepareStatement("insert into stockschema.stock(stockDate,companyCode,price) values(?,?,?)");
			st.setDate(1, new java.sql.Date(new Date().getTime()));
			st.setLong(2, stockRequest.getCompanyCode());
			st.setDouble(3, stockRequest.getStockPrice());
			int rs = st.executeUpdate();

			st.close();
			con.close();
			stockResponse.setStatusMessage("SUCCESS");

		} catch (Exception e) {
			System.out.println(e + "error");
			stockResponse.setStatusMessage("FAIURE");
		}

		String jsonString = mapper.writeValueAsString(stockResponse);
		return new APIGatewayProxyResponseEvent().withStatusCode(200).withHeaders(headers).withBody(jsonString);

	}

	/*
	 * public static void main(String args[]) throws JsonProcessingException {
	 * 
	 * APIGatewayProxyRequestEvent req = new APIGatewayProxyRequestEvent(); StockDto
	 * dto = new StockDto(); dto.setCompanyCode(Long.valueOf(2));
	 * dto.setStockDate(new Date()); dto.setStockPrice(123);
	 * 
	 * ObjectMapper mapper = new ObjectMapper(); String jsonString =
	 * mapper.writeValueAsString(dto); req.setBody(jsonString); CreateStockHandler
	 * obj = new CreateStockHandler(); obj.handleRequest(req, null); }
	 */

}
