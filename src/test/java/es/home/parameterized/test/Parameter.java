package es.home.parameterized.test;

import es.home.parameterized.annotations.ParameterNotEmpty;
import es.home.parameterized.annotations.ParameterOption;
import es.home.parameterized.impl.HelpParameterExecutable;
import es.home.parameterized.impl.SimpleParameterExecutable;
import es.home.parameterized.interfaces.ParameterExecutable;
import es.home.parameterized.interfaces.Parameterized;

/**
 * Enumerado con los comandos de la aplicación
 * */
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
