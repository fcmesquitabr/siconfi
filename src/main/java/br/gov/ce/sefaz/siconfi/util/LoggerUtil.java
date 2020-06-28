package br.gov.ce.sefaz.siconfi.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerUtil {

	public static Logger createLogger(Class<?> classe) {
		return LogManager.getLogger(classe);
	}
}
