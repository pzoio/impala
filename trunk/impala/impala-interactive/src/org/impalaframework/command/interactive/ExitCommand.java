package org.impalaframework.command.interactive;

import org.impalaframework.command.framework.Command;
import org.impalaframework.command.framework.CommandDefinition;
import org.impalaframework.command.framework.CommandState;
import org.impalaframework.testrun.DynamicContextHolder;

public class ExitCommand implements Command {

	public boolean execute(CommandState commandState) {

		System.out.println("Exiting application");
		
		try {
			DynamicContextHolder.unloadParent();
		}
		catch (RuntimeException e) {
		}		
		
		System.exit(0);
		return false;
	}

	public CommandDefinition getCommandDefinition() {
		return new CommandDefinition("Shuts down modules and exits application");
	}

}
