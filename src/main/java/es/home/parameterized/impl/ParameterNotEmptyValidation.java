package es.home.parameterized.impl;

import es.home.parameterized.interfaces.ParameterValidation;

/**
 * Se debe añadir valor para el atributo, y además, ese valor no puede ser vacío
 */
public class ParameterNotEmptyValidation  implements ParameterValidation{

	/**
	 * {@inheritDoc}
	 * Devuelve true si el valor es distinto de nulo y no es vacío
	 */
	@Override
	public boolean isParamValid(String paramValue) {
		return paramValue!=null && !paramValue.isEmpty();
	}
}
