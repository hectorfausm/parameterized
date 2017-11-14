package es.home.parameterized.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Opciones de un parámetro
 * */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD })
public @interface ParameterOption {
	
	/** Clave corta del parámetro */
	String shortKey();
	
	/** Descripción del parámetro. Por defecto cadena vacía */
	String description() default "";
	
	/** Determina si la opción tiene o no argumentos por defecto false */
	boolean hasArgs() default false;
}
