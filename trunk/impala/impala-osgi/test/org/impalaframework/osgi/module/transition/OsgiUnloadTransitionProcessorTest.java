package org.impalaframework.osgi.module.transition;

import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import junit.framework.TestCase;

import org.impalaframework.exception.ExecutionException;
import org.impalaframework.module.definition.ModuleDefinition;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

public class OsgiUnloadTransitionProcessorTest extends TestCase {

	private BundleContext bundleContext;
	private OsgiUnloadTransitionProcessor processor;
	private Bundle bundle;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		bundleContext = createMock(BundleContext.class);
		bundle = createMock(Bundle.class);
		initProcessor(bundle);
	}

	private void initProcessor(Bundle bundle) {
		processor = new TestUnloadProcessor(bundle);
		processor.setBundleContext(bundleContext);
	}
	
	public void testFindAndUnloadBundle() throws BundleException {
		bundle.stop();
		
		replayMocks();
		
		processor.findAndUnloadBundle(new SimpleModuleDefinition("myModule"));
		
		verifyMocks();
	}
	
	public void testFindAndUnloadBundleNull() throws BundleException {
		initProcessor(null);
		
		replayMocks();
		
		processor.findAndUnloadBundle(new SimpleModuleDefinition("myModule"));
		
		verifyMocks();
	}
	
	public void testFindAndUnloadBundleThrowException() throws BundleException {
		bundle.stop();
		expectLastCall().andThrow(new BundleException("Stop failed"));
		
		replayMocks();
		
		try {
			processor.findAndUnloadBundle(new SimpleModuleDefinition("myModule"));
			fail();
		} catch (ExecutionException e) {
		}
		
		verifyMocks();
	}
	
	private void replayMocks() {
		replay(bundleContext);
		replay(bundle);
	}
	
	private void verifyMocks() {
		verify(bundleContext);
		verify(bundle); 
	}
}

class TestUnloadProcessor extends OsgiUnloadTransitionProcessor {

	private Bundle bundle;

	public TestUnloadProcessor(Bundle bundle) {
		super();
		this.bundle = bundle;
	}

	@Override
	Bundle findBundle(ModuleDefinition moduleDefinition) {
		return bundle;
	}
	
}
