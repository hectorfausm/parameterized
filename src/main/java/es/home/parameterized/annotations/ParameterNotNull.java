package es.home.parameterized.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import es.home.parameterized.impl.ParameterNotNullValidation;

/**
 * Genera una valdiación de cadena no nula
 */
@CustomValidation(validationClass = ParameterNotNullValidation.class)
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD })
public @interface ParameterNotNull {}
