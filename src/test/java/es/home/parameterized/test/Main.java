package es.home.parameterized.test;

import es.home.parameterized.exception.ParameterizedParserException;
import es.home.parameterized.parser.Parser;

public class Main {

	public static void main(String[] args) {
		
		try {
			Parser<Parameter> parser = new HelloWorldParser(args);
			
			if(parser.validParams()){
				System.out.println(parser.getValue(Parameter.PARAM_A));
			}else{
				System.out.println(parser.getFailedValidations());
			}
		} catch (ParameterizedParserException e) {
			e.printStackTrace();
		}
	}
}
