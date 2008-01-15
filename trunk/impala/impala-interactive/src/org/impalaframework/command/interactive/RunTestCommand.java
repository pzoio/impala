package org.impalaframework.command.interactive;

import java.io.File;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.impalaframework.classloader.ModuleTestClassLoader;
import org.impalaframework.classloader.TestClassLoader;
import org.impalaframework.command.basic.SelectMethodCommand;
import org.impalaframework.command.framework.Command;
import org.impalaframework.command.framework.CommandDefinition;
import org.impalaframework.command.framework.CommandState;
import org.impalaframework.command.framework.GlobalCommandState;
import org.impalaframework.resolver.ModuleLocationResolver;
import org.impalaframework.resolver.StandaloneModuleLocationResolverFactory;
import org.impalaframework.testrun.DynamicContextHolder;
import org.impalaframework.util.PathUtils;
import org.impalaframework.util.ResourceUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

public class RunTestCommand implements Command {

	//FIXME test
	
	private ModuleLocationResolver moduleLocationResolver;
	public RunTestCommand() {
		super();
		ModuleLocationResolver moduleLocationResolver = new StandaloneModuleLocationResolverFactory()
				.getClassLocationResolver();
		this.moduleLocationResolver = moduleLocationResolver;
	}

	public boolean execute(CommandState commandState) {
				
		Class<?> testClass = (Class<?>) GlobalCommandState.getInstance().getValue(CommandStateConstants.TEST_CLASS);
		if (testClass == null) {
			System.out.println("No test class set.");
			return false;
		}
		
		SelectMethodCommand command = new SelectMethodCommand(testClass);
		command.execute(commandState);
		String methodName = command.getMethodName();
		
		String testClassName = testClass.getName();
		ClassLoader testClassLoader = getTestClassLoader(testClassName);
		ClassLoader existingClassLoader = ClassUtils.getDefaultClassLoader();
		
		try {
		
			Thread.currentThread().setContextClassLoader(testClassLoader);
		
			Class<?> loadedTestClass = testClassLoader.loadClass(testClassName);
			GlobalCommandState.getInstance().addValue(CommandStateConstants.TEST_CLASS, loadedTestClass);
			
			TestRunner runner = new TestRunner();
		
			System.out.println("Running test " + methodName);
			Test test = TestSuite.createTest(loadedTestClass, methodName);
			runner.doRun(test);
		
		}
		catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		finally {
			Thread.currentThread().setContextClassLoader(existingClassLoader);
		}
		
		return true;
	}

	private ClassLoader getTestClassLoader(String testClassName) {
		final ApplicationContext context = DynamicContextHolder.get();

		final ClassLoader parentClassLoader = context.getClassLoader();
		ClassLoader testClassLoader = getTestClassLoader(parentClassLoader, testClassName);
		return testClassLoader;
	}

	private ClassLoader getTestClassLoader(ClassLoader parentClassLoader, String name) {
		String currentDirectoryName = PathUtils.getCurrentDirectoryName();

		Resource[] locationResources = moduleLocationResolver.getModuleTestClassLocations(currentDirectoryName);
		File[] locations = ResourceUtils.getFiles(locationResources);

		String parentProjectName = System.getProperty("impala.parent.project");
		if (parentProjectName != null && !currentDirectoryName.equals(parentProjectName)) {
			// if parent project has been specified and is not the same as the
			// current directory
			return new ModuleTestClassLoader(parentClassLoader, locations, name);
		}
		else {
			return new TestClassLoader(parentClassLoader, locations, name);
		}

	}

	public CommandDefinition getCommandDefinition() {
		return new CommandDefinition("Runs test");
	}

}
