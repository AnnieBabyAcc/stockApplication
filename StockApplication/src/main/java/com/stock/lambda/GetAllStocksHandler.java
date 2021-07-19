package com.stock.lambda;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.model.StockDbConnection;
import com.stock.model.StockDto;
import com.stock.model.StockResponse;



/**
 * Handler for requests to Lambda function.
 */

public class GetAllStocksHandler {
	
    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context)
    		throws JsonProcessingException {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");

        
        StockResponse stockResponse = new StockResponse();
        try {
        	Map<String, String> pathParameters = input.getPathParameters();
        	Integer companyCode =Integer.parseInt(pathParameters.get("companyCode"));
        	String startDateString =pathParameters.get("startDate");
        	String endDateString =pathParameters.get("endDate");
        	
        	List<StockDto> stockList =new ArrayList<>();
        	List<Double> priceList=new ArrayList<>();
        	Connection con= StockDbConnection.getConnection();
        	Statement st=con.createStatement();
        	String query="Select stockId,companyCode,stockDate,price from stockschema.stock where companyCode="+companyCode + 
        			
        			" and DATE(stockDate)>='"+startDateString+"' and DATE(stockDate)<='"+endDateString+"'  order by stockId desc";
        	ResultSet rs=st.executeQuery(query);
        	while(rs.next())  {
        		
        		StockDto dto = new StockDto(rs.getLong(1),rs.getLong(2),rs.getDate(3),rs.getDouble(4));
        		stockList.add(dto);
        		priceList.add(rs.getDouble("price"));
        	}
        	rs.close();
        	st.close();
        	con.close();
        	OptionalDouble average=priceList
                    .stream()
                    .mapToDouble(a -> a).average();
        	OptionalDouble max=priceList
                    .stream()
                    .mapToDouble(a -> a).max();
        	OptionalDouble min=priceList
                    .stream()
                    .mapToDouble(a -> a).min();
        	
                    
        	System.out.println(stockList.toString()+"  "); 
        	stockResponse.setStockDto(stockList);
        	stockResponse.setStatusMessage("SUCCESS");
        	stockResponse.setAvgPrice(average.isPresent() ? average.getAsDouble() : 0);
        	stockResponse.setMaxPrice(max.isPresent() ? max.getAsDouble() : 0);
        	stockResponse.setMinPrice(min.isPresent() ? min.getAsDouble() : 0);
        	
        } catch (Exception e) {
        	System.out.println(e + "error");
        	stockResponse.setStatusMessage("FAIURE");
            
        }
		ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(stockResponse);
		return new APIGatewayProxyResponseEvent().withStatusCode(200).withHeaders(headers).withBody(jsonString);
    }
    
    
    
	
	/*
	 * public static void main(String args[]) throws JsonProcessingException {
	 * Map<String, String> pathParameters= new HashMap<>();
	 * 
	 * pathParameters.put("companyCode", "1"); pathParameters.put("startDate",
	 * "2021-07-13"); pathParameters.put("endDate", "2021-07-14");
	 * APIGatewayProxyRequestEvent req=new APIGatewayProxyRequestEvent();
	 * req.setPathParameters(pathParameters); GetAllStocksHandler obj= new
	 * GetAllStocksHandler(); obj.handleRequest(req, null); }
	 */
	
   
}
