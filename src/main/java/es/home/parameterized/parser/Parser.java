package es.home.parameterized.parser;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;

import es.home.parameterized.annotations.CustomValidation;
import es.home.parameterized.annotations.ParameterOption;
import es.home.parameterized.exception.ParameterizedParserException;
import es.home.parameterized.impl.HelpParameterExecutable;
import es.home.parameterized.interfaces.ParameterValidation;
import es.home.parameterized.interfaces.Parameterized;
import es.home.parameterized.utils.Util;

/**
 * Parseador de opciones de línea de comandos. Tiene implementada por defecto la opción
 * HELP de shortName "h". Su funcionalidad es la de mostrar todas las opciones pasdas en el 
 * constructor y terminar la ejecución del programa
 * */
public abstract class Parser <T extends Enum<? extends Parameterized>> {
	
	/** Argumetnos pasados por línea de comandos */
	private String[] args = null;
	
	/** Opciones */
	private T[] options = null;
	
	/** Valores parseados */
	private Map<T, Object> parsedValues = null;
	
	/** Instancia de la línea de comeandos */
	private CommandLine cmd;
	
	/**
	 * Constructor
	 * @param args Argumetnos pasados por línea de comandos
	 * @throws ParameterizedParserException 
	 * */
	@SuppressWarnings("unchecked")
	public Parser(String[] args) throws ParameterizedParserException {
		this.args = args;
		if(((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments().length>0){
			Class<?> parametericedOptionsClass = (Class<?>)((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
			this.options = (T[]) parametericedOptionsClass.getEnumConstants();
		}
		
		// Si los argumentos contienen ayuda, se imprime la ayuda y se finaliza el programa
		if(containsHelp()){
			help();
			System.exit(0);
		}
	}
	
	/**
	 * Obtiene la lista de valdiaciones que no son válidas para lso parámetros pasados por el usuario
	 * @return Si no hay fallos, se devuevle una lista vacía, en caso contrario, se devuelve la lista de validaciones que no fueron superadas
	 * @throws ParameterizedParserException 
	 */
	public Map<T, List<CustomValidation>> getFailedValidations() throws ParameterizedParserException{
		
		Map<T, List<CustomValidation>> result = new HashMap<>();
		try{
			
			// Se recorren las ocpiones
			for (T option : options) {
				
				// Lista de validaciones no psuperadas para una opción
				List<CustomValidation> validationInOption = new ArrayList<>();
				
				// Se obtiene el valor asociado a cada opción
				String value = getCmd().getOptionValue(((Parameterized)option).getName());
				
				// Se recorren las validaciones
				List<CustomValidation> validations = Util.getValdiations(option);
				for (CustomValidation customValidation : validations) {
					ParameterValidation validationClass = customValidation.validationClass().getConstructor().newInstance();
					if(!validationClass.isParamValid(value)){
						validationInOption.add(customValidation);
					}
				}
				
				// En caso de existan validaciones no superadas, se añaden al resultado
				if(!validationInOption.isEmpty()){
					result.put(option, validationInOption);
				}
			}
			return result;
		
		}catch(ParameterizedParserException pe){
			throw pe;
		}catch(Exception e){
			StringBuilder string = new StringBuilder("Excepción producida al obtener las validaciones: ");
			throw new ParameterizedParserException(string.toString(), e);
		}
	}
	
	/**
	 * Determina si los valores introducidos en la línea de comandos son valores válidos
	 * @return true si los valores cumplen todos los requerimientos. false en caso contrario
	 */
	public boolean validParams() throws ParameterizedParserException {
		return getFailedValidations().isEmpty();
	}

	/**
	 * Devuelve una copia de los valores asignados en el parseador
	 * @throws ParameterizedParserException 
	 * */
	public Map<T, Object> getValues() throws ParameterizedParserException {
		
		// Si no hay datos, se generan
		if(parsedValues==null){
			generateValues();
		}
		
		return new HashMap<>(parsedValues);
	}

	/**
	 * Obtiene el valor que introdujo el usuario para el parámetro pasado en la función
	 * @param parameter Parámetro del cual se quiere obtener el valor
	 * @return Devuelve el objeto referente al parámetro o null en caso de no tener valor almacenado para ese parámetro
	 * @throws ParameterizedParserException 
	 */
	public Object getValue(Parameterized parameter) throws ParameterizedParserException{
		Map<T, Object> values = getValues();
		if(values!=null){
			return values.get(parameter);
		}
		return null;
	}

	/**
	 * Obtiene la cadena que se mostrará en cabecera de la ayuda
	 * @return Cabecera de ayuda mostrada al usuario
	 * */
	public abstract String getHelpHeader();
	
	/**
	 * Obtiene la cadena que se mostrará en el pie de la ayuda
	 * @return Pie de ayuda mostrado al usuario
	 * */
	public abstract String getHelpFooter();
	
	/**
	 * Obtiene el nombre de la aplicación mostrado al usuario
	 * @return Nombre de la aplicación mostrado al usuario
	 * */
	public abstract String getAppName();

	/**
	 * Permite obtener un parámetro ejecutable por nombre
	 * @param name Nombre a buscar
	 * @return Devuelve la opción ejecutable o null si no se encuntra ninguan coincidencia para
	 * ese nombre
	 * */
	private T getElementByName(String name) {
		for (T option : options) {
			if(((Parameterized) option).getName().equals(name)){
				return option;
			}
		}
		return null;
	}

	/**
	 * Imprime la ayuda al usuario
	 * y termina la ejecución del programa
	 * */
	private void help() {
		HelpFormatter formater = new HelpFormatter();
		try {
			if(getAppName()==null || getAppName().length()<=0){
				System.err.println(
					"Para mostrar la ayuda, el nombre de la aplicación no puede ser nulo ni vacío. "
					+ "Implemente correctamente el método "+Parser.class.getCanonicalName()+".getAppName(java.lang.String)"
				);
			}else{
				formater.printHelp(
					getAppName(),
					getHelpHeader(),
					Util.getParameterOptions(options),
					getHelpFooter(),
					true
				);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Obtiene la instancia de {@link CommandLine} a partir de la que ejecutar el resto de oeprativas
	 * @return Devuelv la isntancia de {@link CommandLine} asociada al parseador
	 * @throws ParameterizedParserException
	 */
	private CommandLine getCmd() throws ParameterizedParserException {
		
		if(cmd==null){
			
			// Parseador
			CommandLineParser parser = new DefaultParser();
			
			try {
				cmd = parser.parse(Util.getParameterOptions(options),args);
			} catch (ParseException e) {
				StringBuilder string = new StringBuilder("Excepción producida al procesar los datos de la línea de comandos: ");
				throw new ParameterizedParserException(string.toString(), e);
			}
		}
		return cmd;
	}
	
	/**
	 * Genera los valores del parseador
	 * @throws ParameterizedParserException
	 */
	private void generateValues() throws ParameterizedParserException{
		try {
			
			// Resultado
			Map<T, Object> values = new HashMap<>();
				
			Iterator<Option> itertor  = getCmd().iterator();
			while(itertor.hasNext()){
				Option option = itertor.next();
				T executable = getElementByName(option.getLongOpt());
				
				// Ayuda
				processOption(option, executable, getCmd(), values);
			}
			
			parsedValues = values;
		
		}catch(ParameterizedParserException pe){
			throw pe;
		}catch(Exception e){
			StringBuilder string = new StringBuilder("Excepción producida al procesar los datos de la línea de comandos: ");
			throw new ParameterizedParserException(string.toString(), e);
		}
	}
	
	/**
	 * Determina si los argumentos pasados por parámetros contienen ayuda
	 * @param options
	 * @param args
	 * @return
	 * @throws ParameterizedParserException
	 */
	private boolean containsHelp() throws ParameterizedParserException {
		
		// Se recorren los argumentos
		for (String arg : args) {
			for (T option : options) {
				ParameterOption paramOption = Util.getParameterOptionsFromEnum(option);
				if((paramOption.shortKey().equals(Util.pruneArg(arg)) || ((Parameterized)option).getName().equals(Util.pruneArg(arg))) && ((Parameterized)option).getParameterExecutable() instanceof HelpParameterExecutable){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Procesa una opión de forma individual
	 * @param option
	 * @param executable
	 * @param cmd
	 * @param values
	 */
	private void processOption(Option option, T executable, CommandLine cmd, Map<T, Object> values) {
		
		if(executable==null){
			return;
		}
		
		// La opción es de las registradas
		if(option.hasArgs() || option.hasArg()){
			String value = cmd.getOptionValue(option.getOpt());
			if(value==null && option.isRequired()){
				System.err.println("El campo: "+option.getOpt()+"("+option.getLongOpt()+") es obligatorio");
				help();
			}
			values.put(
				executable,
				((Parameterized) executable).getParameterExecutable().executeFunction(value)
			);
		}else{
			((Parameterized) executable).getParameterExecutable().executeFunction(null);
		}
	}
}
