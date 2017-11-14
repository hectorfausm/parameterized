package es.home.parameterized.exception;

/**
 * Excepción producida durante el parseo de los parámertos
 * */
@SuppressWarnings("serial")
public class ParameterizedParserException extends Exception {
	
	/**
	 * Constructor
	 * */
	public ParameterizedParserException(String msg) {
		super (msg);
	}
	
	/**
	 * Constructor
	 * */
	public ParameterizedParserException(String msg,Exception e) {
		super (msg,e);
	}
}
