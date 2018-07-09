package com.trendyol.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServiceLogger {

	private static Logger logger = LogManager.getLogger(ServiceLogger.class);

	public static <T> void logSaveObjectRequest(String jsonData, Class<T> type) {
		String template = "handling save request: type:%s, data:%s";
		logger.info(String.format(template, type.getSimpleName(), jsonData));
	}

	public static <T> void logSaveObjectError(Object jsonData, Class<T> type, Exception e) {
		String template = "error during save request: type:%s, data:%s";
		logger.error(String.format(template, type.getSimpleName(), jsonData), e);
	}

	public static <T> void logaddProductToShoppingCartRequest(long shoppingCartId, long productId, int quantity,
			Class<T> type) {
		String template = "unexpected error during adding product to shopping cart: {cartId:%s, productId:%s, quantity:%s}";
		logger.info(String.format(template, type.getSimpleName(), shoppingCartId, productId, quantity));

	}

	public static <T> void logAddShoppingCartObjectError(String jsonData, Class<T> type, Exception e) {
		String template = "error during adding product to shopping cart: type:%s, data:%s";
		logger.error(String.format(template, type.getSimpleName(), jsonData), e);
	}

	public static <T> void logPrintObjectRequest(Class<T> type, long id) {
		String template = "handling print request: type:%s, id:%s";
		logger.info(String.format(template, type.getSimpleName(), id));

	}

	public static <T> void logPrintObjectError(Class<T> type, long id, Exception e) {
		String template = "error during handling print request: type:%s, id:%s";
		logger.info(String.format(template, type.getSimpleName(), id));
	}
}
