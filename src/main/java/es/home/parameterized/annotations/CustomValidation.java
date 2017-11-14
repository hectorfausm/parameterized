package es.home.parameterized.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import es.home.parameterized.interfaces.ParameterValidation;

/**
 * Validación customizada. Se utiliza en otras anotaciones para generar validaciones personalizadas
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.ANNOTATION_TYPE })
public @interface CustomValidation {
	
	/** Clase que contiene la implementación de la validación */
	Class<? extends ParameterValidation> validationClass(); 
}
