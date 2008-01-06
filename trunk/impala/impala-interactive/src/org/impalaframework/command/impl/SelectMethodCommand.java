package org.impalaframework.command.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.impalaframework.command.Command;
import org.impalaframework.command.CommandInput;
import org.impalaframework.command.CommandSpec;
import org.impalaframework.command.CommandState;
import org.springframework.util.Assert;


public class SelectMethodCommand implements Command {

	private Class<?> testClass;

	private String methodName;

	public SelectMethodCommand(Class<?> testClass) {
		super();
		Assert.notNull(testClass);
		this.testClass = testClass;
	}

	public boolean execute(CommandState commandState) {
		
		Method[] methods = testClass.getMethods();

		while (methodName == null) {

			List<String> foundMethods = new ArrayList<String>();
			for (Method method : methods) {
				if (method.getParameterTypes().length == 0 && method.getName().startsWith("test")) {
					foundMethods.add(method.getName());
				}
			}

			if (foundMethods.size() >= 2) {
				AlternativeInputCommand altInputCommand = new AlternativeInputCommand(foundMethods
						.toArray(new String[foundMethods.size()]));

				CommandInput input = commandState.capture(altInputCommand);
				if (input.isGoBack()) {
					// start again
					continue;
				}

				altInputCommand.execute(commandState);
				methodName = altInputCommand.getSelectedAlternative();
			}
			else if (foundMethods.size() == 1) {
				methodName = foundMethods.get(0);
			}
			else {
				System.out.println("No method found");
			}

		}
		return true;
	}

	public CommandSpec getCommandSpec() {
		return CommandSpec.EMPTY;
	}

	public String getMethodName() {
		return methodName;
	}

}
