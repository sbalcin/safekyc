package com.company.safekyc.enumeration;

public enum ExchangeMarkets {


	Bitfinex("Bitfinex"),
	Binance("Binance");


	private String value;

	private ExchangeMarkets(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}


}
