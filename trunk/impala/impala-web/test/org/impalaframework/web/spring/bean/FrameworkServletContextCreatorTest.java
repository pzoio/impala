package org.impalaframework.web.spring.bean;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.easymock.classextension.EasyMock;
import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.facade.Impala;
import org.impalaframework.module.ModuleDefinitionSource;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.builder.InternalModuleDefinitionSource;
import org.impalaframework.module.type.TypeReaderRegistryFactory;
import org.impalaframework.web.WebConstants;
import org.impalaframework.web.servlet.ExternalModuleServlet;
import org.impalaframework.web.spring.helper.FrameworkServletContextCreator;

public class FrameworkServletContextCreatorTest extends TestCase {

	private FrameworkServletContextCreator creator;
	private ExternalModuleServlet servlet;
	private ServletConfig servletConfig;
	private ServletContext servletContext;

	@Override
	protected void setUp() throws Exception {
		Impala.clear();
		servlet = createMock(ExternalModuleServlet.class);
		creator = new FrameworkServletContextCreator(servlet);
		servletConfig = EasyMock.createMock(ServletConfig.class);
		servletContext = EasyMock.createMock(ServletContext.class);
	}
	
	public void testImpalaNotConfigured() {
		expectGetServletContext();
		expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(null);
		
		replayMocks();
		
		try {
			creator.createWebApplicationContext();
			fail();
		}
		catch (ConfigurationException e) {
			assertEquals("Unable to load org.impalaframework.web.helper.FrameworkServletContextCreator as no attribute 'org.springframework.web.context.WebApplicationContext.FACTORY_HOLDER' has been set up. Have you set up your Impala ContextLoader correctly?", e.getMessage());
		}

		verifyMocks();
	}
	
	public void testWrongServletName() {
		Impala.init();
		Impala.init(new Test1());
		
		expectGetServletContext();
		expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(Impala.getFacade().getModuleManagementFacade());
		expectGetServletName("another");
		
		replayMocks();
		
		try {
			creator.createWebApplicationContext();
		}
		catch (ConfigurationException e) {
			assertEquals("No module registered under the name of servlet 'another'", e.getMessage());
		}

		verifyMocks();
	}
	
	public void testMustBeWeb() {
		Impala.init();
		Impala.init(new Test1());
		
		expectGetServletContext();
		expect(servletContext.getAttribute(WebConstants.IMPALA_FACTORY_ATTRIBUTE)).andReturn(Impala.getFacade().getModuleManagementFacade());
		expectGetServletName("impala-core");
		
		replayMocks();
		
		try {
			creator.createWebApplicationContext();
		}
		catch (ConfigurationException e) {
			assertEquals("Module registered under name of servlet 'impala-core' needs to be an instance of org.springframework.web.context.WebApplicationContext", e.getMessage());
		}

		verifyMocks();
	}


	private void verifyMocks() {
		verify(servlet);
		verify(servletConfig);
		verify(servletContext);
	}

	private void replayMocks() {
		replay(servlet);
		replay(servletConfig);
		replay(servletContext);
	}

	private void expectGetServletContext() {
		expect(servlet.getServletConfig()).andReturn(servletConfig);
		expect(servlet.getServletConfig()).andReturn(servletConfig);
		expect(servletConfig.getServletContext()).andReturn(servletContext);
	}

	private void expectGetServletName(String name) {
		expect(servlet.getServletConfig()).andReturn(servletConfig);
		expect(servlet.getServletConfig()).andReturn(servletConfig);
		expect(servletConfig.getServletName()).andReturn(name);
	}

}

class Test1 implements ModuleDefinitionSource {
	ModuleDefinitionSource source = new InternalModuleDefinitionSource(TypeReaderRegistryFactory.getTypeReaderRegistry(), 
			Impala.getFacade().getModuleManagementFacade().getModuleLocationResolver(), 
			new String[] { "impala-core" });

	public RootModuleDefinition getModuleDefinition() {
		return source.getModuleDefinition();
	}
}

