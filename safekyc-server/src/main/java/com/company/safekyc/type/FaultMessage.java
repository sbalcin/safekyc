package com.company.safekyc.type;

import javax.xml.ws.WebFault;

@WebFault(name = "Fault")
public class FaultMessage extends Exception {

	private static final long serialVersionUID = 1L;

	private FaultType faultInfo;

	public FaultMessage(String message, FaultType faultInfo) {
		super(message);
		this.faultInfo = faultInfo;
	}

	public FaultMessage(String message, FaultType faultInfo, Throwable cause) {
		super(message, cause);
		this.faultInfo = faultInfo;
	}


	public FaultType getFaultInfo() {
		return faultInfo;
	}

}
