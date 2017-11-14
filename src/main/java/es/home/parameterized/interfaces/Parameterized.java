package es.home.parameterized.interfaces;

/**
 * Interfaz que permite parametrizar un enumerado
 * */
public interface Parameterized{
	
	/**
	 * Obtiene el nombre del atributo ejecutable
	 * @return Nombre del atributo de línea de comandos
	 * */
	public String getName();
	
	/**
	 * Permite obtener el objeto con la ejecución del parámetro para una cadena
	 * @return Devuelve una instancia de {@link ParameterExecutable} 
	 * */
	public ParameterExecutable getParameterExecutable();

}
