package com.company.safekyc.util;

import com.company.safekyc.enumeration.StatusCodes;
import com.company.safekyc.type.FaultMessage;
import com.company.safekyc.type.FaultType;
import org.apache.log4j.Logger;

public class ExceptionHandling {

	private static Logger logger = Logger.getLogger(ExceptionHandling.class);

	public static FaultMessage handleTicketException(StatusCodes statusCode) {
		logger.error(statusCode.toString());

		FaultType processingFault = new FaultType();

		processingFault.setCode(statusCode.getCode());
		processingFault.setMessage(statusCode.getMessage());
		return new FaultMessage(statusCode.getMessage(), processingFault);
	}

	public static FaultMessage handleTicketException(StatusCodes statusCode, Throwable e) {
		logger.error(statusCode.toString(), e);

		FaultType processingFault = new FaultType();

		processingFault.setCode(statusCode.getCode());
		processingFault.setMessage(statusCode.getMessage());
		return new FaultMessage(statusCode.getMessage(), processingFault);
	}

	public static FaultMessage handleTicketException(String message, Throwable e) {
		logger.error(message, e);

		FaultType processingFault = new FaultType();
		return new FaultMessage(message, processingFault);
	}

	public static Throwable handleException(Throwable e) {
		logger.error("Error !", e);
		e.printStackTrace();
		return e;
	}

	public static Throwable handleException(Throwable e, String message) {
		logger.error("Error !" + message, e);
		e.printStackTrace();
		return e;
	}

}
