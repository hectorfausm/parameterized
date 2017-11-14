package es.home.parameterized.utils;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import es.home.parameterized.annotations.CustomValidation;
import es.home.parameterized.annotations.ParameterOption;
import es.home.parameterized.exception.ParameterizedParserException;
import es.home.parameterized.interfaces.Parameterized;

/**
 * Utilidades para parsear parámetros de línea de comandos
 * */
public class Util {
	
	/**
	 * Constructor sin argumentos
	 */
	private Util(){}
	
	/**
	 * Permite obtener una lista de opciones de commons cli a partir de una lista de 
	 * enumerados parametrizados
	 * @param parameters Objetos parametrizados
	 * @return Devuelve una instancia de {@link Options}
	 * */
	public static Options getParameterOptions(Enum<? extends Parameterized> [] parameters) throws ParameterizedParserException{
		Options options = new Options();
		for (Enum<? extends Parameterized> t : parameters) {
			if(Util.getParameterOptionsFromEnum(t)!=null){
				options.addOption(getParameterOption(t));
			}
		}
		return options;
	}
	
	/**
	 * Permite obtener una opción de commons cli a partir de un enumerado parametrizado
	 * @param parameter Objeto parametrizado
	 * @return Devuelve una instancia de {@link Option}
	 * */
	public static Option getParameterOption(Enum<? extends Parameterized> parameter) throws ParameterizedParserException{
		ParameterOption option = Util.getParameterOptionsFromEnum(parameter);
		return Option.builder(option.shortKey())
	     //.required(option.required())
	     .longOpt(((Parameterized) parameter).getName())
	     .desc(option.description())
	     .hasArg(option.hasArgs())
	     .build();
	}
	
	/**
	 * Permite obtener las validaciones de un campo
	 * @param parameter Parámetro del que obtener las validaciones
	 * @return Devuelve la lista de validacion añadida a un campo. Si el campo no tiene opciones de valdiación, se devuelve uan lista vacía
	 * @throws ParameterizedParserException
	 */
	public static List<CustomValidation> getValdiations(Enum<? extends Parameterized> parameter) throws ParameterizedParserException{
		 List<CustomValidation> result = new ArrayList<>();
		try {
			
			// Se obtienen las anotaciones
			Annotation[] annotations = parameter.getClass().getField(parameter.name()).getAnnotations();
			
			// inyecta las anotaciones valdiadas en el resultado
			internGetValidations(result, annotations);
			
			return result;
		} catch (Exception e) {
			StringBuilder string = new StringBuilder("Excepción producida durante la obtención de las validaciones de la opción: ");
			string.append(parameter.name());
			throw new ParameterizedParserException(string.toString(), e);
		}
	}
	
	/**
	 * Inyecta de forma recursiva las anotaciones de tipo Custom en una lista de anotaciones customizadas
	 * @param result Lista en la que inyectar las anotaciones
	 * @param annotations Lista de anotaciones a comprobar
	 */
	private static void internGetValidations(List<CustomValidation> result, Annotation[] annotations) {
		if(annotations!=null){
		
			// Se recorren para obtener las validaciones customizadas
			for (Annotation annotation : annotations) {
				if(CustomValidation.class.equals(annotation.annotationType())){
					result.add((CustomValidation)annotation);
				}else if(!Documented.class.equals(annotation.annotationType()) && !Target.class.equals(annotation.annotationType()) && !Retention.class.equals(annotation.annotationType())){
					internGetValidations(result, annotation.annotationType().getAnnotations());
				}
			}
		}
	}

	/**
	 * Permite obtener las opciones de un enumerado
	 * @param Enumerado del cual se quiere extraer las opciones
	 * @return Devuleve la anotación {@link ParameterOption} Que pudiera contener el enumerado o null
	 * en caso contrario
	 * */
	public static ParameterOption getParameterOptionsFromEnum(Enum<? extends Parameterized> enumerator) throws ParameterizedParserException {
		try {
			return enumerator.getClass().getField(enumerator.name()).getAnnotation(ParameterOption.class);
		} catch (Exception e) {
			StringBuilder string = new StringBuilder("Excepción producida durante la obtención de las opciones de la opción: ");
			string.append(enumerator.name());
			throw new ParameterizedParserException(string.toString(), e);
		}
	}

	/**
	 * Permite dejar el argumento limpio de guiones iniciales
	 * @param arg Argumento introducido desde la consola
	 * @return Devuelve el arg introducido desde la consola sin guiones iniciales
	 */
	public static String pruneArg(String arg) {
		String result = arg;
		if(result.startsWith("-")){
			result = result.substring(1);
			return pruneArg(result);
		}else{
			return arg;
		}
	}
}
