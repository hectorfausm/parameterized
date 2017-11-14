package es.home.parameterized.test;

import es.home.parameterized.exception.ParameterizedParserException;
import es.home.parameterized.parser.Parser;

/**
 * Paseador
 */
public class HelloWorldParser extends Parser<Parameter>{
	
	/**
	 * Coonstructor
	 * @param args Argumetnos psados en el main
	 * @throws ParameterizedParserException
	 */
	public HelloWorldParser(String[] args) throws ParameterizedParserException {
		super(args);
	}

	@Override
	public String getHelpHeader() {
		return "Cabecera de ejemplo";
	}

	@Override
	public String getHelpFooter() {
		return "Footer de ejemplo";
	}

	@Override
	public String getAppName() {
		return "MyApp";
	}
}
