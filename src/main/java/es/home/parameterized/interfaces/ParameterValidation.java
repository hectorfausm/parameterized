package es.home.parameterized.interfaces;

/**
 * Clase que permite definir una validación personalizada para el parámetro
 */
public interface ParameterValidation {
	
	/**
	 * Determina si el parámetro introducido por consola es válido o no
	 * */
	public abstract boolean isParamValid(String paramValue);
}
