package com.stock.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StockDto {
	
	
	private Long stockId;
	
	
	private Long companyCode;
	

	private Date stockDate;
	
	
	private double stockPrice;
	
	
	
	
	

}
