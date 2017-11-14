package es.home.parameterized.impl;

import es.home.parameterized.interfaces.ParameterValidation;

/**
 * Añade al atributo obligatoriedad, de tal forma, que se debe introducir valor para ese atributo
 */
public final class ParameterNotNullValidation implements ParameterValidation{

	/**
	 * {@inheritDoc}
	 * Devuelve true, si el atributo no es nulo
	 */
	@Override
	public boolean isParamValid(String paramValue) {
		return paramValue!=null;
	}
}
