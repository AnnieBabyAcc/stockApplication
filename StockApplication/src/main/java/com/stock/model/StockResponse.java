package com.stock.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3452495145437447139L;
	private List<StockDto> stockDto;
	private String statusMessage;
	private double avgPrice;

	private double maxPrice;

	private double minPrice;
}
