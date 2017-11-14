package es.home.parameterized.interfaces;

/**
 * Datos de un parámetro
 * */
public interface ParameterExecutable {
	
	/**
	 * Función a ejecutar para el parámetro
	 * @param value Valor recogido de la línea de coandos para un parámetro
	 * @return Valor devuelto al usuario
	 * */
	public Object executeFunction(String value);
}
