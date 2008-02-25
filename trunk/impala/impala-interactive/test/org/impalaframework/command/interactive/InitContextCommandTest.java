package org.impalaframework.command.interactive;

import junit.framework.TestCase;

import org.impalaframework.command.framework.GlobalCommandState;
import org.impalaframework.exception.NoServiceException;
import org.impalaframework.facade.DynamicContextHolder;
import org.impalaframework.resolver.LocationConstants;

public class InitContextCommandTest extends TestCase {

	private InitContextCommand command;

	public void setUp() {
		System.setProperty(LocationConstants.ROOT_PROJECTS_PROPERTY, "impala");
		GlobalCommandState.getInstance().reset();
		DynamicContextHolder.clear();
		command = new InitContextCommand();
	}

	public final void testExecuteNoModuleDefinitionSource() {
		command.execute(null);
		try {
			DynamicContextHolder.getRootContext();
			fail();
		}
		catch (NoServiceException e) {
			assertEquals("No root application has been loaded", e.getMessage());
		}
	}

	public final void testExecuteWithModuleDefinitionSource() {
		GlobalCommandState.getInstance().addValue(CommandStateConstants.MODULE_DEFINITION_SOURCE, new Test1());
		command.execute(null);
		assertNotNull(DynamicContextHolder.getRootContext());

	}
}
