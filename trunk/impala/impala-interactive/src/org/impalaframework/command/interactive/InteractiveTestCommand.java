package org.impalaframework.command.interactive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.impalaframework.command.framework.Command;
import org.impalaframework.command.framework.CommandDefinition;
import org.impalaframework.command.framework.CommandInfo;
import org.impalaframework.command.framework.CommandPropertyValue;
import org.impalaframework.command.framework.CommandState;
import org.impalaframework.command.framework.GlobalCommandState;
import org.impalaframework.command.framework.TerminatedApplicationException;
import org.impalaframework.command.framework.TerminatedCommandException;
import org.impalaframework.command.framework.TextParsingCommand;
import org.impalaframework.command.listener.TestCommandListener;

public class InteractiveTestCommand implements Command {
	
	private Map<String, Command> commandMap = new HashMap<String, Command>();
	private Map<String, String> aliasMap = new HashMap<String, String>();
	private List<TestCommandListener> listeners = new ArrayList<TestCommandListener>(); 
	
	public boolean execute(CommandState commandState) {
		
		CommandPropertyValue fullCommandText = commandState.getProperties().get(CommandStateConstants.COMMAND_TEXT);
		GlobalCommandState.getInstance().addProperty(CommandStateConstants.LAST_COMMAND, fullCommandText);
		
		String commandText = fullCommandText.getValue();
		String[] commandTerms = commandText.split(" ");
		String[] extraTerms = new String[commandTerms.length -1];
		
		if (commandTerms.length > 1) {
			System.arraycopy(commandTerms, 1, extraTerms, 0, extraTerms.length);
		}
		
		final String commandName = commandTerms[0];
		Command command = commandMap.get(commandName);
		
		if (command == null && aliasMap != null) {
			//treat command name as an alias
			String aliasName = commandName;
			String aliasCommandName = aliasMap.get(aliasName);
			command = commandMap.get(aliasCommandName);
		}
		
		if (command != null) {
			if (extraTerms.length > 0 && command instanceof TextParsingCommand) {
				TextParsingCommand t = (TextParsingCommand) command;
				t.extractText(extraTerms, commandState);
			}
		
			try {
				commandState.captureInput(command);
				command.execute(commandState);
			}
			catch (TerminatedCommandException e) {
				//a terminated application exception should be silently caught and ignored
			}
			catch (TerminatedApplicationException e) {
				//a terminated application exception should be rethrown
				throw e;
			}
			catch (RuntimeException e) {
				System.out.println("Error executing command " + fullCommandText);
				InteractiveCommandUtils.printException(e);
			}
		} else {
			System.out.println("Unrecognised command or alias " + commandName);
		}
		
		for (TestCommandListener testCommandListener : listeners) {
			testCommandListener.commandExecuted(commandText);
		}
		
		return false;
	}

	public void addCommand(String name, Command command) {
		commandMap.put(name, command);
	}
	
	public void setCommandMap(Map<String, Command> commandMap) {
		this.commandMap.clear();
		this.commandMap.putAll(commandMap);
	}

	public void addAlias(String name, String alias) {
		aliasMap.put(name, alias);
	}
	
	public void setAliasMap(Map<String, String> aliasMap) {
		this.aliasMap.clear();
		this.aliasMap.putAll(aliasMap);
	}

	public void addTestListener(TestCommandListener listener) {
		this.listeners.add(listener);
	}
	
	public CommandDefinition getCommandDefinition() {
		CommandInfo commandInfo = new CommandInfo("commandText", "Command text", "Please enter your command text",
				null, null, false, false, false, false);

		CommandDefinition commandDefinititon = new CommandDefinition();
		commandDefinititon.add(commandInfo);
		return commandDefinititon;	
	}

}
