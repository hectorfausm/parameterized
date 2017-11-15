# Framework para la parametrización de aplicaciones de consola

Este pequeño framework permite generar aplicaciones parametrizadas de consola en java de una forma sencilla. Se utilizan anotaciones en un enumerado que generan la definición de los parámetros y la obtención de sus valores, facilitando además, la sigueinte funcionalidad:
- Permite obtener los valores introducidos por línea de comandos de una forma clara y sencilla.
- Permite validar los valores introducidos por línea de comandos con validaciones predeterminadas o configurando tus propias valdiaciones.
- Genera y lanza automáticamente la ayuda del script cuando es necesario.

Ejemplo de uso:

- **Parameter.java**:
```java
public enum Parameter implements Parameterized{
	@ParameterOption(shortKey = "h")
	HELP(new HelpParameterExecutable()),
	
	@ParameterOption(shortKey="pa", hasArgs=true)
	@ParameterNotEmpty
	PARAM_A(new SimpleParameterExecutable()),
	
	@ParameterOption(shortKey="pb", description="El programa saluda al mundo")
	PARAM_B(new ParameterExecutable(){
		public Object executeFunction(String value) {
			System.out.println("Hola mundo");
			return null;
		}
	});

	/** Constructor */
	private Parameter(ParameterExecutable exedcutable) {
		this.executable = exedcutable;
	}

	/** Función a ejecutar si se incluye el comando definido en el enumerado*/
	private ParameterExecutable executable;
	
	// ACCEDENTES
	public String getName() {
		return this.name().toLowerCase();
	}
	public ParameterExecutable getParameterExecutable() {
		return executable;
	}
}
```

- **HelloWorldParser.java**:
```java
public class HelloWorldParser extends Parser<Parameter>{
	
	/**
	 * Coonstructor
	 * @param args Argumetnos psados en el main
	 * @throws ParameterizedParserException
	 */
	public HelloWorldParser(String[] args) throws ParameterizedParserException {
		super(args);
	}

	@Override
	public String getHelpHeader() {
		return "Cabecera de ejemplo";
	}

	@Override
	public String getHelpFooter() {
		return "Footer de ejemplo";
	}

	@Override
	public String getAppName() {
		return "MyApp";
	}
}
```

- **Main.java**:
```java
public class Main {

    public static void main(String[] args) {

        try {
            Parser<Parameter> parser = new HelloWorldParser(args);

            if(parser.validParams()){
                System.out.println(parser.getValue(Parameter.PARAM_A));
            }else{
                System.out.println(parser.getFailedValidations());
            }
        } catch (ParameterizedParserException e) {
            e.printStackTrace();
        }
    }
}
```

## Anotación @ParameterOption
Esta anotación se incorpora a los distintos elementos del enumerado para definir el comportamiento del comando. Puede definir los siguientes atributos:

Nombre | Tipo | Descripción | Valor por defecto
------ | ---- | ----------- | -----------------
shortKey | String | Clave corta del parámetro | S/V
description | String | Descripción del parámetro | 
hasArgs | String | Determina si la operación tiene o no argumentos | false

## Anotación @CustomValidation
Las validaciones de los parámetros se realizan a través de anotaciones que permite desvincular la lógica de validación de la lógica de parseo. Para poder generar una valdiación es necesario generar una anotación que contenga la anotación CustomValidation. Esta anotación contiene un único parámetro: 

 - **`validationClass`**: Admite un valor del tipo `Class<? extends ParameterValidation>` que será una implenetación de la interfaz `ParameterValidation` que contiene la función encargada de la validación. Se pueden incluir de 0 a N validaciones por cada parámetro del enumerado. Existen algunas validaciones desarrolladas:
    - ** @ParameterNotEmpty **: Valida que la cadena del parámetro no sea nula ni vacía.
    - ** @ParameterNotNull **: Valida que la cadena del parámetro no sea nula.
    
    Ejemplo de la implementación de `@ParameterNotNul`:
    
    ``` java
    @CustomValidation(validationClass = ParameterNotNullValidation.class)
    @Retention(value = RetentionPolicy.RUNTIME)
    @Target(value = { ElementType.FIELD })
    public @interface ParameterNotNull {}
    ```
    
        

## Interfaces
El Framework define dos interfaces:
 - **Parametericed:** Esta interfaz debe implementarse en el enumerado y añade dos métodos:
    - **getName():** Obtiene el nombre del atributo ejecutable en línea de comandos
    - **getParameterExecutable():** Obtiene la función a ejecutar si aparece el parámetro en la línea de comandos.
 - **ParameterExecutable:** Esta interfaz es la que devuelve el método getParametericedExecutable() por cada uno de los elementos del enumerado. Define el método **Object executeFunction(String value)** Que permite añadir funcionalidad a un parámetro y tratar los valores introducidos en la línea de comandos. 
 Implementaciones por defecto:
    - **SimpleParameterExecutable()**: Devuelve la cadena introducida por comando.
    - **HelpParameterExecutable()**: Ejecuta la ayuda del script.
 - **ParameterValidation:** Permite generar validaciones personalizadas a través de Anotaciones, implementando el único método de la validación `public abstract boolean isParamValid(String paramValue);` Como parámetro recibe la cadena introducida por argumento en la ejecución del método main para una atributo. Devolviendo true si el atributo es válido o false en caso contrario. Existen dos implementaciones que se pueden utilziar en las aplicaciones:
    - **ParameterNotEmptyValidation**: Valida que el valor no sea nulo ni vacío.
    - **ParameterNotNullValidation**: Valida que el valor no sea nulo.

##  Parser
La clase abstracta `Parser` supone la base del framwork y permite construir la instancia del objeto que maneja los elementos de la línea de comandos. En ella se han de definir tres métodos:
- **`String getHelpHeader()`:** Devuelve la cabecera que se imprime en la ayuda del aplicativo de línea de comandos.
- **`String getHelpFooter()`:** Permite definir un pié para la ayuda del aplicativo de línea de comandos.
- **`String getAppName()`:** Permite definir el nombre que se mostrará en la auida del aplicativo de la línea de comandos

Además, contiene los sigeuintes métodos públicos:
- **`public boolean validParams()`**: Comprueba si los valores del parseador son válidos
- **`public Map<Enum<? extends Parameterized>, Object> getValues()`** Obtiene una copia de los valores del parseador
- **`public Object getValue(Parameterized parameter)`**: Obtiene uno de los valores del parseador a partir de una de las opciones del parseador.
- **`public Map<Enum<? extends Parameterized, List<CustomValidation>> getFailedValidations()`**: Obtiene la lista de validaciones no superadas por los datos del parseador.

