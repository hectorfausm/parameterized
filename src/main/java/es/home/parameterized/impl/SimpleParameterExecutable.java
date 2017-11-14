package es.home.parameterized.impl;

import es.home.parameterized.interfaces.ParameterExecutable;

/**
 * Ejecución simple de parámetro
 * */
public class SimpleParameterExecutable implements ParameterExecutable {
	/**
	 * Devuelve el valor pasado como argumento
	 * @param value Valor
	 * @return Devuelve el valor pasado como argumento
	 * */
	public Object executeFunction(String value) {
		return value;
	}
}
