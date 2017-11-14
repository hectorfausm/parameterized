package es.home.parameterized.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import es.home.parameterized.impl.ParameterNotEmptyValidation;

/**
 * Gnera una valdiación de cadena no vacía
 */
@CustomValidation(validationClass = ParameterNotEmptyValidation.class)
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD })
public @interface ParameterNotEmpty {}
